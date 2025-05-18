package com.nexts.gs.mars.nexts_gs_mars_field_service.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nexts.gs.mars.nexts_gs_mars_field_service.dto.response.ApiResponse;
import com.nexts.gs.mars.nexts_gs_mars_field_service.services.AdministrativeUnitService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("${api.version}/administrative-units")
@RequiredArgsConstructor
public class AdministrativeUnitController {
  private final AdministrativeUnitService administrativeUnitService;

  @GetMapping
  public ResponseEntity<ApiResponse<Object>> getAllAdministrativeUnits() {
    return ResponseEntity.ok(
        ApiResponse.builder()
            .message("Administrative units fetched successfully")
            .status(HttpStatus.OK.value())
            .data(administrativeUnitService.getAllAdministrativeUnits())
            .build());
  }
}
