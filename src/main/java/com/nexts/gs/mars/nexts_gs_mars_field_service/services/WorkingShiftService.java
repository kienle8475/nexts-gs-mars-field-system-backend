package com.nexts.gs.mars.nexts_gs_mars_field_service.services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.nexts.gs.mars.nexts_gs_mars_field_service.repositories.WorkingShiftRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Root;

import jakarta.persistence.criteria.Predicate;

import com.nexts.gs.mars.nexts_gs_mars_field_service.repositories.OutletRepository;
import com.nexts.gs.mars.nexts_gs_mars_field_service.repositories.StaffAttendanceRepository;
import com.nexts.gs.mars.nexts_gs_mars_field_service.dto.request.WorkingshiftUpdateRequest;
import com.nexts.gs.mars.nexts_gs_mars_field_service.dto.request.WorkshiftCriteriaRequest;
import com.nexts.gs.mars.nexts_gs_mars_field_service.dto.response.WorkShiftGroupResponse;
import com.nexts.gs.mars.nexts_gs_mars_field_service.dto.response.WorkShiftResponse;
import com.nexts.gs.mars.nexts_gs_mars_field_service.mapper.WorkingShiftMapper;
import com.nexts.gs.mars.nexts_gs_mars_field_service.models.Outlet;
import com.nexts.gs.mars.nexts_gs_mars_field_service.models.OutletWorkingTime;
import com.nexts.gs.mars.nexts_gs_mars_field_service.models.StaffAttendance;
import com.nexts.gs.mars.nexts_gs_mars_field_service.models.WorkingShift;

import java.util.ArrayList;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WorkingShiftService {
  private final WorkingShiftRepository workingShiftRepository;
  private final WorkingShiftMapper workingShiftMapper;
  private final StaffAttendanceRepository staffAttendanceRepository;
  private final EntityManager entityManager;
  private final OutletRepository outletRepository;

  public WorkShiftGroupResponse getShiftsByOutletAndStaffStatus(Long outletId, Long staffId) {
    LocalDate today = LocalDate.now();
    List<WorkingShift> outletShifts = workingShiftRepository.findByOutletId(outletId);
    List<StaffAttendance> allAttendances = staffAttendanceRepository.findByStaffIdAndCheckoutTimeIsNull(staffId);

    List<WorkShiftResponse> pendingCheckout = new ArrayList<>();
    List<WorkShiftResponse> available = new ArrayList<>();

    // Ca chưa checkout (từ tất cả outlet)
    for (StaffAttendance attendance : allAttendances) {
      WorkingShift shift = attendance.getShift();
      WorkShiftResponse dto = workingShiftMapper.toResponse(shift);
      dto.setCheckedIn(true);
      dto.setTotalCheckedIn(staffAttendanceRepository.countByShiftId(shift.getId()));
      pendingCheckout.add(dto);
    }

    // Ca hôm nay (từ outlet hiện tại), loại bỏ các ca đã nằm trong pendingCheckout
    Set<Long> pendingShiftIds = pendingCheckout.stream()
        .map(WorkShiftResponse::getId)
        .collect(Collectors.toSet());

    for (WorkingShift shift : outletShifts) {
      LocalDate shiftDate = shift.getStartTime().toLocalDate();
      if (shiftDate.isEqual(today) && !pendingShiftIds.contains(shift.getId())) {
        Optional<StaffAttendance> attendanceOpt = staffAttendanceRepository.findByShiftIdAndStaffId(shift.getId(),
            staffId);

        boolean checkedIn = attendanceOpt.isPresent();

        WorkShiftResponse dto = workingShiftMapper.toResponse(shift);
        dto.setCheckedIn(checkedIn);
        dto.setTotalCheckedIn(staffAttendanceRepository.countByShiftId(shift.getId()));
        available.add(dto);
      }
    }

    return new WorkShiftGroupResponse(pendingCheckout, available);
  }

  public List<WorkingShift> getTodaysShiftsByOutlet(Long outletId) {
    LocalDate today = LocalDate.now();
    LocalDateTime startOfDay = today.atStartOfDay();
    LocalDateTime endOfDay = today.atTime(LocalTime.MAX);
    return workingShiftRepository.findByOutletIdAndStartTimeBetween(outletId, startOfDay, endOfDay);
  }

  private List<Predicate> buildWorkingShiftPredicates(Root<WorkingShift> root, CriteriaBuilder cb,
      WorkshiftCriteriaRequest request) {
    List<Predicate> predicates = new ArrayList<>();
    if (request.getOutletId() != null) {
      predicates.add(cb.equal(root.get("outlet").get("id"), request.getOutletId()));
    }

    if (request.getProvinceId() != null) {
      predicates.add(cb.equal(root.get("outlet").get("province").get("id"), request.getProvinceId()));
    }

    if (request.hasDate()) {
      Expression<LocalDate> shiftDate = cb.function("DATE", LocalDate.class, root.get("startTime"));
      predicates.add(cb.equal(shiftDate, request.getDate()));
    }
    return predicates;
  }

  public List<WorkShiftResponse> getWorkingShiftsByCriteria(WorkshiftCriteriaRequest request) {
    CriteriaBuilder cb = entityManager.getCriteriaBuilder();
    CriteriaQuery<WorkingShift> cq = cb.createQuery(WorkingShift.class);
    Root<WorkingShift> root = cq.from(WorkingShift.class);

    List<Predicate> predicates = buildWorkingShiftPredicates(root, cb, request);
    cq.where(predicates.toArray(new Predicate[0]));
    cq.orderBy(cb.asc(root.get("outlet").get("name")));

    List<WorkingShift> shifts = entityManager.createQuery(cq).getResultList();
    List<WorkShiftResponse> responses = new ArrayList<>();
    for (WorkingShift shift : shifts) {
      WorkShiftResponse response = workingShiftMapper.toResponse(shift);
      response.setCheckedIn(shift.getStaffAttendances().size() > 0);
      response.setTotalCheckedIn(shift.getStaffAttendances().size());
      responses.add(response);
    }
    return responses;
  }

  public WorkingShift updateWorkingShift(Long id, WorkingshiftUpdateRequest request) {
    WorkingShift shift = workingShiftRepository.findById(id).orElseThrow(() -> new RuntimeException("Shift not found"));
    shift.setName(request.getName());
    shift.setStartTime(request.getStartTime());
    shift.setEndTime(request.getEndTime());
    return workingShiftRepository.save(shift);
  }

  public List<WorkingShift> generateWorkingShifts(Long outletId) {
    Outlet outlet = outletRepository.findById(outletId).orElseThrow(() -> new RuntimeException("Outlet not found"));
    OutletWorkingTime workingTime = outlet.getWorkingTime();
    if (workingTime == null) {
      throw new RuntimeException("Working time not found");
    }
    LocalTime startTime = workingTime.getStartTime();
    LocalTime endTime = workingTime.getEndTime();
    LocalDate today = LocalDate.now();

    List<WorkingShift> shifts = new ArrayList<>();

    WorkingShift shift = new WorkingShift();
    shift.setOutlet(outlet);
    shift.setStartTime(today.atTime(startTime));
    shift.setEndTime(today.atTime(endTime));
    shift.setName("Ca " + today.getDayOfMonth());
    shift.setStaffAttendances(new ArrayList<>());
    shifts.add(shift);

    return shifts;
  }
}
