package com.nexts.gs.mars.nexts_gs_mars_field_service.services;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.nexts.gs.mars.nexts_gs_mars_field_service.models.ExportJob;
import com.nexts.gs.mars.nexts_gs_mars_field_service.repositories.ExportJobRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ExportJobService {
  private final ExportJobRepository repository;

  public ExportJob createJob() {
    ExportJob job = ExportJob.builder()
        .status("PENDING")
        .createdAt(LocalDateTime.now())
        .build();
    return repository.save(job);
  }

  public void updateJobDone(Long jobId, String filename, String filepath) {
    ExportJob job = repository.findById(jobId).orElseThrow();
    job.setStatus("DONE");
    job.setFilename(filename);
    job.setFilepath(filepath);
    repository.save(job);
  }

  public void markFailed(Long jobId) {
    ExportJob job = repository.findById(jobId).orElseThrow();
    job.setStatus("FAILED");
    repository.save(job);
  }

  public Optional<ExportJob> getLatest() {
    return repository.findTopByOrderByCreatedAtDesc();
  }

  public ExportJob getById(Long id) {
    return repository.findById(id).orElseThrow();
  }
}
