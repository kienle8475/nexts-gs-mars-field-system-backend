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
import com.nexts.gs.mars.nexts_gs_mars_field_service.repositories.StaffAttendanceRepository;
import com.nexts.gs.mars.nexts_gs_mars_field_service.dto.response.WorkShiftGroupResponse;
import com.nexts.gs.mars.nexts_gs_mars_field_service.dto.response.WorkShiftResponse;
import com.nexts.gs.mars.nexts_gs_mars_field_service.mapper.WorkingShiftMapper;
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
}
