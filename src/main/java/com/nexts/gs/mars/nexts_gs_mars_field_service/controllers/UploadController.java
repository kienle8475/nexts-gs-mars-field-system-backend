package com.nexts.gs.mars.nexts_gs_mars_field_service.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import com.nexts.gs.mars.nexts_gs_mars_field_service.services.FileStorageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import com.nexts.gs.mars.nexts_gs_mars_field_service.dto.response.ApiResponse;
import org.springframework.http.HttpStatus;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/upload")
public class UploadController {
  private final FileStorageService fileStorageService;

  @PostMapping("/image")
  public ResponseEntity<ApiResponse<Object>> uploadImage(@RequestParam("file") MultipartFile file) {
    String path = fileStorageService.storeFile(file);
    return ResponseEntity.ok(
        ApiResponse.builder()
            .message("Image uploaded successfully")
            .status(HttpStatus.OK.value())
            .data(path)
            .build());
  }

}
