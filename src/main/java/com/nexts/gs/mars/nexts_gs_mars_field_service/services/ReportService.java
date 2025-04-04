package com.nexts.gs.mars.nexts_gs_mars_field_service.services;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.nexts.gs.mars.nexts_gs_mars_field_service.models.OosReport;
import com.nexts.gs.mars.nexts_gs_mars_field_service.models.SaleReport;
import com.nexts.gs.mars.nexts_gs_mars_field_service.models.SamplingReport;
import com.nexts.gs.mars.nexts_gs_mars_field_service.models.StaffAttendance;
import com.nexts.gs.mars.nexts_gs_mars_field_service.dto.request.ReportRequest;
import com.nexts.gs.mars.nexts_gs_mars_field_service.dto.response.ReportResponse;
import com.nexts.gs.mars.nexts_gs_mars_field_service.mapper.ReportMapper;
import com.nexts.gs.mars.nexts_gs_mars_field_service.repositories.OosReportRepository;
import com.nexts.gs.mars.nexts_gs_mars_field_service.repositories.SaleReportRepository;
import com.nexts.gs.mars.nexts_gs_mars_field_service.repositories.SamplingReportRepository;
import com.nexts.gs.mars.nexts_gs_mars_field_service.repositories.StaffAttendanceRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReportService {
  private final SaleReportRepository saleReportRepository;
  private final OosReportRepository oosReportRepository;
  private final SamplingReportRepository samplingReportRepository;
  private final StaffAttendanceRepository attendanceRepository;
  private final ReportMapper reportMapper;

  private StaffAttendance getAttendance(Long id) {
    return attendanceRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("Attendance not found"));
  }

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

}
