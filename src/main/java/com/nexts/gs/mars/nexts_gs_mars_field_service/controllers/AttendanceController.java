package com.nexts.gs.mars.nexts_gs_mars_field_service.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.nexts.gs.mars.nexts_gs_mars_field_service.dto.request.CheckInRequest;
import com.nexts.gs.mars.nexts_gs_mars_field_service.dto.request.CheckOutRequest;
import com.nexts.gs.mars.nexts_gs_mars_field_service.dto.response.ApiResponse;
import com.nexts.gs.mars.nexts_gs_mars_field_service.models.StaffAttendance;
import com.nexts.gs.mars.nexts_gs_mars_field_service.services.AttendanceService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/attendance")
@RequiredArgsConstructor
public class AttendanceController {
  private final AttendanceService attendanceService;

  @PostMapping("/checkin")
  public ResponseEntity<ApiResponse<Object>> checkIn(
      @ModelAttribute CheckInRequest request,
      @RequestParam MultipartFile file) {
    attendanceService.checkIn(request, file);
    return ResponseEntity.ok(
        ApiResponse.builder()
            .message("Checked in successfully")
            .status(HttpStatus.OK.value())
            .data(null)
            .build());
  }

  @PostMapping("/checkout")
  public ResponseEntity<ApiResponse<Object>> checkOut(
      @ModelAttribute CheckOutRequest request,
      @RequestParam MultipartFile file) {
    attendanceService.checkOut(request, file);
    return ResponseEntity.ok(
        ApiResponse.builder()
            .message("Checked out successfully")
            .status(HttpStatus.OK.value())
            .data(null)
            .build());
  }

  @GetMapping("/current-shift")
  public ResponseEntity<ApiResponse<Object>> getCurrentAttendance(@RequestParam(required = false) Long staffId,
      Authentication authentication) {
    StaffAttendance attendance = attendanceService.getCurrentAttendance(staffId, authentication);
    return ResponseEntity.ok(
        ApiResponse.builder()
            .message("Current attendance fetched successfully")
            .status(HttpStatus.OK.value())
            .data(attendance)
            .build());
  }
}