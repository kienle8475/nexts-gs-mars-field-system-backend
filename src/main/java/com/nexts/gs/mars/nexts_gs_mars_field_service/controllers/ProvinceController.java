package com.nexts.gs.mars.nexts_gs_mars_field_service.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.nexts.gs.mars.nexts_gs_mars_field_service.dto.response.ApiResponse;
import com.nexts.gs.mars.nexts_gs_mars_field_service.models.Province;
import com.nexts.gs.mars.nexts_gs_mars_field_service.services.ProvinceService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("${api.version}/provinces")
@RequiredArgsConstructor
public class ProvinceController {
  private final ProvinceService provinceService;

  @GetMapping
  public ResponseEntity<ApiResponse<Object>> getAllProvinces() {
    return ResponseEntity.ok(
        ApiResponse.builder()
            .message("Provinces fetched successfully")
            .status(HttpStatus.OK.value())
            .data(provinceService.getAllProvinces())
            .build());
  }

  @GetMapping("/{id}")
  public ResponseEntity<ApiResponse<Object>> getProvinceById(@PathVariable Long id) {
    return ResponseEntity.ok(
        ApiResponse.builder()
            .message("Province fetched successfully")
            .status(HttpStatus.OK.value())
            .data(provinceService.getProvinceById(id))
            .build());
  }

  @PostMapping
  public ResponseEntity<ApiResponse<Object>> createProvince(@RequestBody Province province) {
    return ResponseEntity.ok(
        ApiResponse.builder()
            .message("Province created successfully")
            .status(HttpStatus.CREATED.value())
            .data(provinceService.createProvince(province))
            .build());
  }
}
