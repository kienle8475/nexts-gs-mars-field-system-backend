package com.nexts.gs.mars.nexts_gs_mars_field_service.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nexts.gs.mars.nexts_gs_mars_field_service.dto.request.StaffProfileUpdateRequest;
import com.nexts.gs.mars.nexts_gs_mars_field_service.dto.response.ApiResponse;
import com.nexts.gs.mars.nexts_gs_mars_field_service.models.StaffProfile;
import com.nexts.gs.mars.nexts_gs_mars_field_service.services.StaffProfileService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api/staff-profiles")
@RequiredArgsConstructor
public class StaffProfileController {
  private final StaffProfileService staffProfileService;

  @PutMapping("/{id}")
  public ResponseEntity<ApiResponse<Object>> updateStaffProfile(
      @PathVariable Long id,
      @ModelAttribute StaffProfileUpdateRequest request,
      @RequestParam(required = false) MultipartFile[] files) {
    StaffProfile staffProfile = staffProfileService.updateStaffProfile(id, request, files);
    return ResponseEntity.ok(ApiResponse.builder()
        .message("Staff profile updated successfully")
        .data(staffProfile)
        .build());
  }
}
