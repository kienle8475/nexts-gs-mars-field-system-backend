package com.nexts.gs.mars.nexts_gs_mars_field_service.dto.request;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class WorkingshiftUpdateRequest {
  private String name;

  private LocalDateTime startTime;

  private LocalDateTime endTime;
}
