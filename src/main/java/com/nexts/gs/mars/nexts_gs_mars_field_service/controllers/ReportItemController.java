package com.nexts.gs.mars.nexts_gs_mars_field_service.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

import com.nexts.gs.mars.nexts_gs_mars_field_service.services.ReportItemService;
import com.nexts.gs.mars.nexts_gs_mars_field_service.dto.request.ReportItemRequest;
import com.nexts.gs.mars.nexts_gs_mars_field_service.dto.response.ApiResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("${api.version}/report-items")
@RequiredArgsConstructor
public class ReportItemController {
  private final ReportItemService reportItemService;

  @PostMapping
  public ResponseEntity<ApiResponse<Object>> create(@RequestBody ReportItemRequest request) {
    return ResponseEntity.ok(
        ApiResponse.builder()
            .message("Report Item created successfully")
            .status(HttpStatus.CREATED.value())
            .data(reportItemService.create(request))
            .build());
  }

  @GetMapping("/by-report-type")
  public ResponseEntity<ApiResponse<Object>> getByReportType(@RequestParam String type) {
    return ResponseEntity.ok(
        ApiResponse.builder()
            .message("Report Items fetched by type successfully")
            .status(HttpStatus.OK.value())
            .data(reportItemService.getByReportType(type))
            .build());
  }

  @GetMapping()
  public ResponseEntity<ApiResponse<Object>> getAll() {
    return ResponseEntity.ok(
        ApiResponse.builder()
            .message("Report Items fetched successfully")
            .status(HttpStatus.OK.value())
            .data(reportItemService.getAll())
            .build());
  }

  @PutMapping("/{id}")
  public ResponseEntity<ApiResponse<Object>> update(@PathVariable String id, @RequestBody ReportItemRequest request) {
    return ResponseEntity.ok(
        ApiResponse.builder()
            .message("Report Item updated successfully")
            .status(HttpStatus.OK.value())
            .data(reportItemService.update(id, request))
            .build());
  }
}
