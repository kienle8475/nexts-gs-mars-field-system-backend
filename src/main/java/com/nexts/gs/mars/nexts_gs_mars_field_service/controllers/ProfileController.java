package com.nexts.gs.mars.nexts_gs_mars_field_service.controllers;

import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

import com.nexts.gs.mars.nexts_gs_mars_field_service.dto.response.ApiResponse;
import com.nexts.gs.mars.nexts_gs_mars_field_service.services.SaleProfileService;
import com.nexts.gs.mars.nexts_gs_mars_field_service.services.StaffProfileService;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.version}/profiles")
public class ProfileController {
  private final SaleProfileService saleProfileService;
  private final StaffProfileService staffProfileService;

  @GetMapping("/sale/options")
  public ResponseEntity<ApiResponse<Object>> getSaleOptions() {
    return ResponseEntity.ok(
        ApiResponse.builder()
            .message("Sale options fetched successfully")
            .status(HttpStatus.OK.value())
            .data(saleProfileService.getSimple())
            .build());
  }

  @GetMapping("/staff/options")
  public ResponseEntity<ApiResponse<Object>> getStaffOptions() {
    return ResponseEntity.ok(
        ApiResponse.builder()
            .message("Staff options fetched successfully")
            .status(HttpStatus.OK.value())
            .data(staffProfileService.getSimple())
            .build());
  }

  @GetMapping("/staff")
  public ResponseEntity<ApiResponse<Object>> getAllStaff(Pageable pageable) {
    return ResponseEntity.ok(
        ApiResponse.builder()
            .message("Staff fetched successfully")
            .status(HttpStatus.OK.value())
            .data(staffProfileService.getAllStaff(pageable))
            .build());
  }
}
