package com.nexts.gs.mars.nexts_gs_mars_field_service.controllers;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nexts.gs.mars.nexts_gs_mars_field_service.dto.ShiftWithAttendanceDTO;
import com.nexts.gs.mars.nexts_gs_mars_field_service.dto.request.ReportRequest;
import com.nexts.gs.mars.nexts_gs_mars_field_service.dto.request.StaffLeaveCriteriaRequest;
import com.nexts.gs.mars.nexts_gs_mars_field_service.dto.request.ReportCriteriaRequest;
import com.nexts.gs.mars.nexts_gs_mars_field_service.dto.response.ApiResponse;
import com.nexts.gs.mars.nexts_gs_mars_field_service.dto.response.ReportResponse;
import com.nexts.gs.mars.nexts_gs_mars_field_service.models.StaffAttendance;
import com.nexts.gs.mars.nexts_gs_mars_field_service.models.StaffLeave;
import com.nexts.gs.mars.nexts_gs_mars_field_service.services.ReportService;
import com.nexts.gs.mars.nexts_gs_mars_field_service.mapper.StaffLeaveMapper;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/reports")
public class ReportController {

  private final ReportService reportService;
  private final StaffLeaveMapper staffLeaveMapper;

  @PostMapping("/sale")
  public ResponseEntity<ApiResponse<ReportResponse>> createSaleReport(@RequestBody ReportRequest request) {
    return ResponseEntity.ok(
        ApiResponse.<ReportResponse>builder()
            .status(200)
            .message("Success")
            .data(reportService.saveOrUpdateSaleReport(request))
            .build());
  }

  @PostMapping("/oos")
  public ResponseEntity<ApiResponse<ReportResponse>> createOosReport(@RequestBody ReportRequest request) {
    return ResponseEntity.ok(
        ApiResponse.<ReportResponse>builder()
            .status(200)
            .message("Success")
            .data(reportService.saveOrUpdateOosReport(request))
            .build());
  }

  @PostMapping("/sampling")
  public ResponseEntity<ApiResponse<ReportResponse>> createSamplingReport(@RequestBody ReportRequest request) {
    return ResponseEntity.ok(
        ApiResponse.<ReportResponse>builder()
            .status(200)
            .message("Success")
            .data(reportService.saveOrUpdateSamplingReport(request))
            .build());
  }

  @GetMapping("/sale")
  public ResponseEntity<ApiResponse<ReportResponse>> getSaleReport(@RequestParam Long attendanceId) {
    return reportService.getSaleReport(attendanceId)
        .map(res -> ResponseEntity.ok(
            ApiResponse.<ReportResponse>builder()
                .status(200)
                .message("Success")
                .data(res)
                .build()))
        .orElseGet(() -> ResponseEntity.status(404).body(
            ApiResponse.<ReportResponse>builder()
                .status(204)
                .message("Sale report not found")
                .data(null)
                .build()));
  }

  @GetMapping("/oos")
  public ResponseEntity<ApiResponse<ReportResponse>> getOosReport(@RequestParam Long attendanceId) {
    return reportService.getOosReport(attendanceId)
        .map(res -> ResponseEntity.ok(
            ApiResponse.<ReportResponse>builder()
                .status(200)
                .message("Success")
                .data(res)
                .build()))
        .orElseGet(() -> ResponseEntity.status(404).body(
            ApiResponse.<ReportResponse>builder()
                .status(204)
                .message("Oos report not found")
                .data(null)
                .build()));
  }

  @GetMapping("/sampling")
  public ResponseEntity<ApiResponse<ReportResponse>> getSamplingReport(@RequestParam Long attendanceId) {
    return reportService.getSamplingReport(attendanceId)
        .map(res -> ResponseEntity.ok(
            ApiResponse.<ReportResponse>builder()
                .status(200)
                .message("Success")
                .data(res)
                .build()))
        .orElseGet(() -> ResponseEntity.status(404).body(
            ApiResponse.<ReportResponse>builder()
                .status(204)
                .message("Sampling report not found")
                .data(null)
                .build()));
  }

  @GetMapping("/attendance")
  public ResponseEntity<ApiResponse<Object>> getAttendanceReport(
      @ModelAttribute ReportCriteriaRequest request,
      Pageable pageable) {
    PageImpl<ShiftWithAttendanceDTO> result = reportService.getAttendanceReport(request, pageable);
    return ResponseEntity.ok(ApiResponse.<Object>builder()
        .status(200)
        .message("Success")
        .data(result)
        .build());
  }

  @GetMapping("/attendance/by-sale")
  public ResponseEntity<ApiResponse<Object>> getAttendanceReportBySale(
      @RequestParam Long saleProfileId,
      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate)

  {
    LocalDateTime startDateTime = fromDate.atStartOfDay();
    LocalDateTime endDateTime = toDate.atTime(LocalTime.MAX); // 23:59:59.999999999

    List<StaffAttendance> attendances = reportService.getAttendancesBySaleIdAndDate(saleProfileId, startDateTime,
        endDateTime);
    return ResponseEntity.ok(ApiResponse.<Object>builder()
        .status(200)
        .message("Success")
        .data(attendances)
        .build());
  }

  @GetMapping("/staff-leave/by-sale")
  public ResponseEntity<ApiResponse<Object>> getStaffLeaveReportBySale(
      @RequestParam Long saleProfileId,
      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate) {

    LocalDateTime startDateTime = fromDate.atStartOfDay();
    LocalDateTime endDateTime = toDate.atTime(LocalTime.MAX);

    List<StaffLeave> staffLeaves = reportService.getStaffLeavesBySaleIdAndDate(saleProfileId, startDateTime,
        endDateTime);
    return ResponseEntity.ok(ApiResponse.<Object>builder()
        .status(200)
        .message("Success")
        .data(staffLeaveMapper.toSaleResponseList(staffLeaves))
        .build());
  }

  @GetMapping("/staff-leave")
  public ResponseEntity<ApiResponse<Object>> getStaffLeaves(
      @ModelAttribute StaffLeaveCriteriaRequest request,
      Pageable pageable) {
    Page<StaffLeave> leaves = reportService.getStaffLeaves(request, pageable);
    return ResponseEntity.ok(ApiResponse.<Object>builder()
        .status(200)
        .message("Success")
        .data(leaves.map(staffLeaveMapper::toAdminResponse))
        .build());
  }
}
