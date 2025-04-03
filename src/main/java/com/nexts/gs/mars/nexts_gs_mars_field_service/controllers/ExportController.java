package com.nexts.gs.mars.nexts_gs_mars_field_service.controllers;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nexts.gs.mars.nexts_gs_mars_field_service.models.ExportJob;
import com.nexts.gs.mars.nexts_gs_mars_field_service.services.ExportJobService;
import com.nexts.gs.mars.nexts_gs_mars_field_service.services.ExportAsyncService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/exports")
public class ExportController {
  private final ExportJobService jobService;
  private final ExportAsyncService asyncService;

  @PostMapping("/start")
  public ResponseEntity<Map<String, Object>> startExport() {
    ExportJob job = jobService.createJob();
    asyncService.exportToFile(job.getId());
    return ResponseEntity.ok(Map.of("jobId", job.getId()));
  }

  @GetMapping("/{id}/status")
  public ResponseEntity<ExportJob> getStatus(@PathVariable Long id) {
    return ResponseEntity.ok(jobService.getById(id));
  }

  @GetMapping("/{id}/download")
  public ResponseEntity<Resource> download(@PathVariable Long id) throws IOException {
    ExportJob job = jobService.getById(id);
    if (!"DONE".equals(job.getStatus())) {
      throw new IllegalStateException("Export not ready");
    }

    Path file = Paths.get(job.getFilepath());
    Resource resource = new UrlResource(file.toUri());

    return ResponseEntity.ok()
        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + job.getFilename() + "\"")
        .contentType(MediaType.TEXT_PLAIN)
        .body(resource);
  }
}
