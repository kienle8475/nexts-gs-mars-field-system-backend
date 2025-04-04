package com.nexts.gs.mars.nexts_gs_mars_field_service.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nexts.gs.mars.nexts_gs_mars_field_service.dto.request.ReportRequest;
import com.nexts.gs.mars.nexts_gs_mars_field_service.dto.response.ApiResponse;
import com.nexts.gs.mars.nexts_gs_mars_field_service.dto.response.ReportResponse;
import com.nexts.gs.mars.nexts_gs_mars_field_service.services.ReportService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/reports")
public class ReportController {

  private final ReportService reportService;

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

}
