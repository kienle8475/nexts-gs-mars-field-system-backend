package com.nexts.gs.mars.nexts_gs_mars_field_service.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nexts.gs.mars.nexts_gs_mars_field_service.dto.request.StaffLeaveRequest;
import com.nexts.gs.mars.nexts_gs_mars_field_service.dto.response.ApiResponse;
import com.nexts.gs.mars.nexts_gs_mars_field_service.services.StaffLeaveService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("${api.version}/staff-leaves")
@RequiredArgsConstructor
public class StaffLeaveController {
  private final StaffLeaveService staffLeaveService;

  @PostMapping
  public ResponseEntity<ApiResponse<Object>> create(@RequestBody StaffLeaveRequest request) {
    return ResponseEntity.ok(ApiResponse.builder()
        .message("Staff leave created successfully")
        .status(HttpStatus.CREATED.value())
        .data(staffLeaveService.create(request))
        .build());
  }

  @PutMapping("/{id}")
  public ResponseEntity<ApiResponse<Object>> update(@PathVariable Long id) {
    return ResponseEntity.ok(ApiResponse.builder()
        .message("Staff leave updated successfully")
        .status(HttpStatus.OK.value())
        .data(staffLeaveService.update(id))
        .build());
  }

  @GetMapping("/{attendanceId}")
  public ResponseEntity<ApiResponse<Object>> getByShiftId(@PathVariable Long attendanceId) {
    return ResponseEntity.ok(ApiResponse.builder()
        .message("Staff leave fetched successfully")
        .status(HttpStatus.OK.value())
        .data(staffLeaveService.getByAttendanceId(
            attendanceId))
        .build());
  }
}
