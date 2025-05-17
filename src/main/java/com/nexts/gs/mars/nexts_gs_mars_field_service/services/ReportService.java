package com.nexts.gs.mars.nexts_gs_mars_field_service.services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.nexts.gs.mars.nexts_gs_mars_field_service.models.OosReport;
import com.nexts.gs.mars.nexts_gs_mars_field_service.models.SaleReport;
import com.nexts.gs.mars.nexts_gs_mars_field_service.models.SamplingReport;
import com.nexts.gs.mars.nexts_gs_mars_field_service.models.StaffAttendance;
import com.nexts.gs.mars.nexts_gs_mars_field_service.models.StaffLeave;
import com.nexts.gs.mars.nexts_gs_mars_field_service.models.WorkingShift;
import com.nexts.gs.mars.nexts_gs_mars_field_service.dto.ShiftWithAttendanceDTO;
import com.nexts.gs.mars.nexts_gs_mars_field_service.dto.request.ReportRequest;
import com.nexts.gs.mars.nexts_gs_mars_field_service.dto.request.StaffLeaveCriteriaRequest;
import com.nexts.gs.mars.nexts_gs_mars_field_service.dto.request.ReportCriteriaRequest;
import com.nexts.gs.mars.nexts_gs_mars_field_service.dto.response.ReportResponse;
import com.nexts.gs.mars.nexts_gs_mars_field_service.mapper.OutletMapper;
import com.nexts.gs.mars.nexts_gs_mars_field_service.mapper.ReportMapper;
import com.nexts.gs.mars.nexts_gs_mars_field_service.repositories.OosReportRepository;
import com.nexts.gs.mars.nexts_gs_mars_field_service.repositories.SaleReportRepository;
import com.nexts.gs.mars.nexts_gs_mars_field_service.repositories.SamplingReportRepository;
import com.nexts.gs.mars.nexts_gs_mars_field_service.repositories.StaffAttendanceRepository;
import com.nexts.gs.mars.nexts_gs_mars_field_service.repositories.StaffLeaveRepository;
import com.nexts.gs.mars.nexts_gs_mars_field_service.models.Outlet;
import com.nexts.gs.mars.nexts_gs_mars_field_service.models.StaffProfile;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Join;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReportService {
  private final SaleReportRepository saleReportRepository;
  private final OosReportRepository oosReportRepository;
  private final SamplingReportRepository samplingReportRepository;
  private final StaffAttendanceRepository attendanceRepository;
  private final ReportMapper reportMapper;
  private final EntityManager entityManager;
  private final OutletMapper outletMapper;
  private final StaffLeaveRepository staffLeaveRepository;

  private StaffAttendance getAttendance(Long id) {
    return attendanceRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("Attendance not found"));
  }

  // Save Report
  public ReportResponse saveOrUpdateSaleReport(ReportRequest request) {
    StaffAttendance attendance = getAttendance(request.getAttendanceId());
    SaleReport report = saleReportRepository.findByAttendanceId(request.getAttendanceId())
        .map(existing -> {
          existing.setData(request.getData());
          return existing;
        })
        .orElseGet(() -> new SaleReport(attendance, request.getData()));
    return reportMapper.toResponse(saleReportRepository.save(report));
  }

  public ReportResponse saveOrUpdateOosReport(ReportRequest request) {
    StaffAttendance attendance = getAttendance(request.getAttendanceId());
    OosReport report = oosReportRepository.findByAttendanceId(request.getAttendanceId())
        .map(existing -> {
          existing.setData(request.getData());
          return existing;
        })
        .orElseGet(() -> new OosReport(attendance, request.getData()));
    return reportMapper.toResponse(oosReportRepository.save(report));
  }

  public ReportResponse saveOrUpdateSamplingReport(ReportRequest request) {
    StaffAttendance attendance = getAttendance(request.getAttendanceId());
    SamplingReport report = samplingReportRepository.findByAttendanceId(request.getAttendanceId())
        .map(existing -> {
          existing.setData(request.getData());
          return existing;
        })
        .orElseGet(() -> new SamplingReport(attendance, request.getData()));
    return reportMapper.toResponse(samplingReportRepository.save(report));
  }

  // Get Report
  public Optional<ReportResponse> getSaleReport(Long attendanceId) {
    return saleReportRepository.findByAttendanceId(attendanceId)
        .map(reportMapper::toResponse);
  }

  public Optional<ReportResponse> getOosReport(Long attendanceId) {
    return oosReportRepository.findByAttendanceId(attendanceId)
        .map(reportMapper::toResponse);
  }

  public Optional<ReportResponse> getSamplingReport(Long attendanceId) {
    return samplingReportRepository.findByAttendanceId(attendanceId)
        .map(reportMapper::toResponse);
  }

  // Attendance Report
  private List<Predicate> buildReportAttendancePredicates(Root<WorkingShift> root, CriteriaBuilder cb,
      ReportCriteriaRequest request) {
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
    } else if (request.hasStartDate() && request.hasEndDate()) {
      predicates.add(cb.between(root.get("startTime"), request.getStartDate().atStartOfDay(),
          request.getEndDate().atTime(LocalTime.MAX)));
    }

    return predicates;
  }

  public PageImpl<ShiftWithAttendanceDTO> getAttendanceReport(ReportCriteriaRequest request, Pageable pageable) {

    CriteriaBuilder cb = entityManager.getCriteriaBuilder();

    // ===== Main Query =====
    CriteriaQuery<WorkingShift> cq = cb.createQuery(WorkingShift.class);
    Root<WorkingShift> root = cq.from(WorkingShift.class);
    List<Predicate> predicates = buildReportAttendancePredicates(root, cb, request);

    cq.where(predicates.toArray(new Predicate[0]));
    cq.orderBy(cb.asc(root.get("outlet").get("name")));

    TypedQuery<WorkingShift> query = entityManager.createQuery(cq);
    query.setFirstResult((int) pageable.getOffset());
    query.setMaxResults(pageable.getPageSize());
    List<WorkingShift> shifts = query.getResultList();

    // ===== Count Query =====
    CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
    Root<WorkingShift> countRoot = countQuery.from(WorkingShift.class);
    List<Predicate> countPredicates = buildReportAttendancePredicates(countRoot, cb, request);

    countQuery.select(cb.count(countRoot)).where(countPredicates.toArray(new Predicate[0]));
    Long total = entityManager.createQuery(countQuery).getSingleResult();

    // ===== Mapping =====
    List<ShiftWithAttendanceDTO> content = shifts.stream().map(shift -> {
      List<StaffAttendance> attendances = attendanceRepository.findAllByShiftId(shift.getId());

      return ShiftWithAttendanceDTO.builder()
          .shiftId(shift.getId())
          .shiftName(shift.getName())
          .startTime(shift.getStartTime())
          .endTime(shift.getEndTime())
          .outlet(outletMapper.toBasicDTO(shift.getOutlet()))
          .attendances(attendances)
          .build();
    }).toList();

    return new PageImpl<>(content, pageable, total);
  }

  public List<StaffAttendance> getAttendanceByCriteria(ReportCriteriaRequest request) {
    CriteriaBuilder cb = entityManager.getCriteriaBuilder();
    CriteriaQuery<WorkingShift> cq = cb.createQuery(WorkingShift.class);
    Root<WorkingShift> root = cq.from(WorkingShift.class);

    List<Predicate> predicates = buildReportAttendancePredicates(root, cb, request);
    cq.where(predicates.toArray(new Predicate[0]));
    cq.orderBy(cb.asc(root.get("startTime")));

    List<WorkingShift> shifts = entityManager.createQuery(cq).getResultList();

    return shifts.stream()
        .flatMap(shift -> attendanceRepository.findAllByShiftId(shift.getId()).stream())
        .toList();
  }

  public List<StaffAttendance> getAttendancesBySaleIdAndDate(Long saleProfileId, LocalDateTime startDateTime,
      LocalDateTime endDateTime) {
    return attendanceRepository.findAllBySaleProfileIdAndCheckinTimeBetween(saleProfileId, startDateTime,
        endDateTime);
  }

  // Staff Leave Report
  public List<StaffLeave> getStaffLeavesBySaleIdAndDate(Long saleProfileId, LocalDateTime startDateTime,
      LocalDateTime endDateTime) {
    return staffLeaveRepository.findAllBySaleProfileIdAndDateBetween(saleProfileId, startDateTime, endDateTime);
  }

  public List<Predicate> buildStaffLeavePredicates(CriteriaBuilder cb, Root<StaffLeave> root, Path<?> staffPath,
      Path<?> outletPath, StaffLeaveCriteriaRequest criteria) {
    List<Predicate> predicates = new ArrayList<>();

    if (criteria.getStaffId() != null) {
      predicates.add(cb.equal(staffPath.get("id"), criteria.getStaffId()));
    }

    if (criteria.getOutletId() != null) {
      predicates.add(cb.equal(outletPath.get("id"), criteria.getOutletId()));
    }

    if (criteria.getDate() != null) {
      LocalDateTime startOfDay = criteria.getDate().atStartOfDay();
      LocalDateTime endOfDay = criteria.getDate().atTime(LocalTime.MAX);
      predicates.add(cb.or(
          cb.between(root.get("startTime"), startOfDay, endOfDay),
          cb.between(root.get("endTime"), startOfDay, endOfDay)));
    }

    if (criteria.getCurrentlyLeaving() != null) {
      if (criteria.getCurrentlyLeaving()) {
        predicates.add(cb.isNull(root.get("endTime")));
      } else {
        predicates.add(cb.isNotNull(root.get("endTime")));
      }
    }
    return predicates;
  }

  public Page<StaffLeave> getStaffLeaves(StaffLeaveCriteriaRequest request, Pageable pageable) {
    CriteriaBuilder cb = entityManager.getCriteriaBuilder();
    CriteriaQuery<StaffLeave> cq = cb.createQuery(StaffLeave.class);
    Root<StaffLeave> root = cq.from(StaffLeave.class);

    Join<Object, Object> attJoin = root.join("attendance");
    Join<StaffAttendance, WorkingShift> shiftJoin = attJoin.join("shift");
    Join<WorkingShift, Outlet> outletJoin = shiftJoin.join("outlet");
    Join<StaffAttendance, StaffProfile> staffJoin = attJoin.join("staff");

    List<Predicate> predicates = buildStaffLeavePredicates(cb, root, staffJoin, outletJoin, request);

    cq.where(cb.and(predicates.toArray(new Predicate[0])));
    cq.orderBy(cb.desc(root.get("startTime")));

    TypedQuery<StaffLeave> query = entityManager.createQuery(cq);
    query.setFirstResult((int) pageable.getOffset());
    query.setMaxResults(pageable.getPageSize());

    // Count query
    CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
    Root<StaffLeave> countRoot = countQuery.from(StaffLeave.class);
    Join<StaffLeave, StaffAttendance> countAttJoin = countRoot.join("attendance");
    Join<StaffAttendance, WorkingShift> countShiftJoin = countAttJoin.join("shift");
    Join<WorkingShift, Outlet> countOutletJoin = countShiftJoin.join("outlet");
    Join<StaffAttendance, StaffProfile> countStaffJoin = countAttJoin.join("staff");

    List<Predicate> countPredicates = buildStaffLeavePredicates(cb, countRoot, countStaffJoin, countOutletJoin,
        request);

    countQuery.select(cb.count(countRoot)).where(cb.and(countPredicates.toArray(new Predicate[0])));
    long total = entityManager.createQuery(countQuery).getSingleResult();

    return new PageImpl<>(query.getResultList(), pageable, total);
  }

  // Sale Report
  public List<SaleReport> getSaleReportsByCriteria(ReportCriteriaRequest request) {
    CriteriaBuilder cb = entityManager.getCriteriaBuilder();
    CriteriaQuery<SaleReport> cq = cb.createQuery(SaleReport.class);
    Root<SaleReport> root = cq.from(SaleReport.class);
    Join<SaleReport, StaffAttendance> attendanceJoin = root.join("attendance");
    Join<StaffAttendance, WorkingShift> shiftJoin = attendanceJoin.join("shift");
    Join<WorkingShift, Outlet> outletJoin = shiftJoin.join("outlet");

    List<Predicate> predicates = new ArrayList<>();

    if (request.getOutletId() != null) {
      predicates.add(cb.equal(outletJoin.get("id"), request.getOutletId()));
    }

    if (request.getProvinceId() != null) {
      predicates.add(cb.equal(outletJoin.get("province").get("id"), request.getProvinceId()));
    }

    if (request.hasDate()) {
      Expression<LocalDate> shiftDate = cb.function("DATE", LocalDate.class, shiftJoin.get("startTime"));
      predicates.add(cb.equal(shiftDate, request.getDate()));
    } else if (request.hasStartDate() && request.hasEndDate()) {
      LocalDateTime fromDateTime = request.getStartDate().atStartOfDay();
      LocalDateTime toDateTime = request.getEndDate().atTime(LocalTime.MAX);
      predicates.add(cb.between(shiftJoin.get("startTime"), fromDateTime, toDateTime));
    }

    cq.where(cb.and(predicates.toArray(new Predicate[0])));
    cq.orderBy(cb.asc(shiftJoin.get("startTime")));

    return entityManager.createQuery(cq).getResultList();
  }

  // OOS Report
  public List<OosReport> getOosReportsByCriteria(ReportCriteriaRequest request) {
    CriteriaBuilder cb = entityManager.getCriteriaBuilder();
    CriteriaQuery<OosReport> cq = cb.createQuery(OosReport.class);
    Root<OosReport> root = cq.from(OosReport.class);
    Join<OosReport, StaffAttendance> attendanceJoin = root.join("attendance");
    Join<StaffAttendance, WorkingShift> shiftJoin = attendanceJoin.join("shift");
    Join<WorkingShift, Outlet> outletJoin = shiftJoin.join("outlet");

    List<Predicate> predicates = new ArrayList<>();

    if (request.getOutletId() != null) {
      predicates.add(cb.equal(outletJoin.get("id"), request.getOutletId()));
    }

    if (request.getProvinceId() != null) {
      predicates.add(cb.equal(outletJoin.get("province").get("id"), request.getProvinceId()));
    }

    if (request.hasDate()) {
      Expression<LocalDate> shiftDate = cb.function("DATE", LocalDate.class, shiftJoin.get("startTime"));
      predicates.add(cb.equal(shiftDate, request.getDate()));
    } else if (request.hasStartDate() && request.hasEndDate()) {
      LocalDateTime fromDateTime = request.getStartDate().atStartOfDay();
      LocalDateTime toDateTime = request.getEndDate().atTime(LocalTime.MAX);
      predicates.add(cb.between(shiftJoin.get("startTime"), fromDateTime, toDateTime));
    }

    cq.where(cb.and(predicates.toArray(new Predicate[0])));
    cq.orderBy(cb.asc(shiftJoin.get("startTime")));

    return entityManager.createQuery(cq).getResultList();
  }

  // Sampling Report
  public List<SamplingReport> getSamplingReportsByCriteria(ReportCriteriaRequest request) {
    CriteriaBuilder cb = entityManager.getCriteriaBuilder();
    CriteriaQuery<SamplingReport> cq = cb.createQuery(SamplingReport.class);
    Root<SamplingReport> root = cq.from(SamplingReport.class);
    Join<SamplingReport, StaffAttendance> attendanceJoin = root.join("attendance");
    Join<StaffAttendance, WorkingShift> shiftJoin = attendanceJoin.join("shift");
    Join<WorkingShift, Outlet> outletJoin = shiftJoin.join("outlet");

    List<Predicate> predicates = new ArrayList<>();

    if (request.getOutletId() != null) {
      predicates.add(cb.equal(outletJoin.get("id"), request.getOutletId()));
    }

    if (request.getProvinceId() != null) {
      predicates.add(cb.equal(outletJoin.get("province").get("id"), request.getProvinceId()));
    }

    if (request.hasDate()) {
      Expression<LocalDate> shiftDate = cb.function("DATE", LocalDate.class, shiftJoin.get("startTime"));
      predicates.add(cb.equal(shiftDate, request.getDate()));
    } else if (request.hasStartDate() && request.hasEndDate()) {
      LocalDateTime fromDateTime = request.getStartDate().atStartOfDay();
      LocalDateTime toDateTime = request.getEndDate().atTime(LocalTime.MAX);
      predicates.add(cb.between(shiftJoin.get("startTime"), fromDateTime, toDateTime));
    }

    cq.where(cb.and(predicates.toArray(new Predicate[0])));
    cq.orderBy(cb.asc(shiftJoin.get("startTime")));

    return entityManager.createQuery(cq).getResultList();
  }

  public List<StaffLeave> getStaffLeavesByCriteria(ReportCriteriaRequest request) {
    CriteriaBuilder cb = entityManager.getCriteriaBuilder();
    CriteriaQuery<StaffLeave> cq = cb.createQuery(StaffLeave.class);
    Root<StaffLeave> root = cq.from(StaffLeave.class);

    List<Predicate> predicates = new ArrayList<>();

    if (request.hasDate()) {
      LocalDateTime startOfDay = request.getDate().atStartOfDay();
      LocalDateTime endOfDay = request.getDate().atTime(LocalTime.MAX);
      predicates.add(cb.between(root.get("startTime"), startOfDay, endOfDay));
    }

    cq.where(cb.and(predicates.toArray(new Predicate[0])));
    cq.orderBy(cb.asc(root.get("startTime")));

    return entityManager.createQuery(cq).getResultList();
  }

}
