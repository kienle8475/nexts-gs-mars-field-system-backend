package com.nexts.gs.mars.nexts_gs_mars_field_service.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import com.nexts.gs.mars.nexts_gs_mars_field_service.dto.request.WorkshiftCriteriaRequest;
import com.nexts.gs.mars.nexts_gs_mars_field_service.dto.request.WorkingshiftUpdateRequest;
import com.nexts.gs.mars.nexts_gs_mars_field_service.dto.response.ApiResponse;
import com.nexts.gs.mars.nexts_gs_mars_field_service.services.WorkingShiftService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.validation.Valid;
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

  @GetMapping()
  public ResponseEntity<ApiResponse<Object>> getWorkingShiftsByCriteria(@RequestBody WorkshiftCriteriaRequest request) {
    return ResponseEntity.ok(
        ApiResponse.builder()
            .message("Shifts fetched successfully")
            .status(HttpStatus.OK.value())
            .data(workingShiftService.getWorkingShiftsByCriteria(request))
            .build());
  }

  @PutMapping("/{id}")
  public ResponseEntity<ApiResponse<Object>> updateWorkingShift(@PathVariable Long id,
      @ModelAttribute @Valid WorkingshiftUpdateRequest request) {
    if (request.getStartTime().isAfter(request.getEndTime())) {
      return ResponseEntity.badRequest().body(
          ApiResponse.builder()
              .message("Start time must be before end time")
              .status(HttpStatus.BAD_REQUEST.value())
              .build());
    }
    return ResponseEntity.ok(
        ApiResponse.builder()
            .message("Working shift updated successfully")
            .status(HttpStatus.OK.value())
            .data(workingShiftService.updateWorkingShift(id, request))
            .build());
  }
}
