package com.nexts.gs.mars.nexts_gs_mars_field_service.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nexts.gs.mars.nexts_gs_mars_field_service.dto.request.CreateOutletRequest;
import com.nexts.gs.mars.nexts_gs_mars_field_service.dto.response.ApiResponse;
import com.nexts.gs.mars.nexts_gs_mars_field_service.models.Outlet;
import com.nexts.gs.mars.nexts_gs_mars_field_service.services.OutletService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/outlets")
@RequiredArgsConstructor
public class OutletController {
  private final OutletService outletService;

  @GetMapping
  public ResponseEntity<ApiResponse<Object>> getPaginated(
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size) {
    return ResponseEntity.ok(
        ApiResponse.builder()
            .message("Outlets fetched successfully")
            .status(HttpStatus.OK.value())
            .data(outletService.getAllOutlets(page, size))
            .build());
  }

  @GetMapping("/all")
  public ResponseEntity<ApiResponse<Object>> getAll() {
    return ResponseEntity.ok(
        ApiResponse.builder()
            .message("Outlets fetched successfully")
            .status(HttpStatus.OK.value())
            .data(outletService.getAllOutlets())
            .build());
  }

  @GetMapping("/{id}")
  public ResponseEntity<ApiResponse<Object>> getById(@PathVariable Long id) {
    return ResponseEntity.ok(
        ApiResponse.builder()
            .message("Outlet fetched successfully")
            .status(HttpStatus.OK.value())
            .data(outletService.getOutletById(id))
            .build());
  }

  @PostMapping
  public ResponseEntity<ApiResponse<Object>> create(@RequestBody CreateOutletRequest request) {
    return ResponseEntity.ok(
        ApiResponse.builder()
            .message("Outlet created successfully")
            .status(HttpStatus.CREATED.value())
            .data(outletService.createOutlet(request))
            .build());
  }

  @PutMapping("/{id}")
  public ResponseEntity<ApiResponse<Object>> update(@PathVariable Long id, @RequestBody Outlet outlet) {
    return ResponseEntity.ok(
        ApiResponse.builder()
            .message("Outlet updated successfully")
            .status(HttpStatus.OK.value())
            .data(outletService.updateOutlet(id, outlet))
            .build());
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<ApiResponse<Object>> delete(@PathVariable Long id) {
    outletService.deleteOutlet(id);
    return ResponseEntity.ok(
        ApiResponse.builder()
            .message("Outlet deleted successfully")
            .status(HttpStatus.NO_CONTENT.value())
            .data(null)
            .build());
  }

  @GetMapping("/by-province/{provinceId}")
  public ResponseEntity<ApiResponse<Object>> getByProvince(@PathVariable Long provinceId) {
    return ResponseEntity.ok(
        ApiResponse.builder()
            .message("Outlets fetched successfully")
            .status(HttpStatus.OK.value())
            .data(outletService.getOutletsByProvince(provinceId))
            .build());
  }
}
