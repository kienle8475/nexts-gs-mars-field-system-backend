package com.nexts.gs.mars.nexts_gs_mars_field_service.services;

import java.time.LocalDateTime;
import java.util.List;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ExportAsyncService {
  private final ExportJobService jobService;
  private final SimpMessagingTemplate messagingTemplate;

  @Async
  public void exportToFile(Long jobId) {
    try {
      Thread.sleep(15000); // giả lập xử lý 15s

      String filename = "report_" + jobId + ".txt";
      String filepath = "exports/" + filename;
      Files.createDirectories(Paths.get("exports"));

      Files.write(Paths.get(filepath), List.of("Export report jobId=" + jobId + " done at " + LocalDateTime.now()));

      jobService.updateJobDone(jobId, filename, filepath);

      messagingTemplate.convertAndSend("/topic/export-status/" + jobId, "DONE");

    } catch (Exception e) {
      jobService.markFailed(jobId);
      messagingTemplate.convertAndSend("/topic/export-status/" + jobId, "FAILED");
    }
  }
}