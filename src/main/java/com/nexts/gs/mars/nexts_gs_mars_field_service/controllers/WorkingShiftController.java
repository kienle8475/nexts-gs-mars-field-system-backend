package com.nexts.gs.mars.nexts_gs_mars_field_service.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import com.nexts.gs.mars.nexts_gs_mars_field_service.dto.response.ApiResponse;
import com.nexts.gs.mars.nexts_gs_mars_field_service.services.WorkingShiftService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("${api.version}/working-shifts")
@RequiredArgsConstructor
public class WorkingShiftController {
  private final WorkingShiftService workingShiftService;

  @GetMapping("/{outletId}/{staffId}")
  public ResponseEntity<ApiResponse<Object>> getShiftsByOutletAndStaffStatus(@PathVariable Long outletId,
      @PathVariable Long staffId) {
    return ResponseEntity.ok(
        ApiResponse.builder()
            .message("Shifts fetched successfully")
            .status(HttpStatus.OK.value())
            .data(workingShiftService.getShiftsByOutletAndStaffStatus(outletId, staffId))
            .build());
  }

  @GetMapping("/{outletId}")
  public ResponseEntity<ApiResponse<Object>> getTodaysShiftsByOutlet(@PathVariable Long outletId) {
    return ResponseEntity.ok(
        ApiResponse.builder()
            .message("Shifts fetched successfully")
            .status(HttpStatus.OK.value())
            .data(workingShiftService.getTodaysShiftsByOutlet(outletId))
            .build());
  }
}
