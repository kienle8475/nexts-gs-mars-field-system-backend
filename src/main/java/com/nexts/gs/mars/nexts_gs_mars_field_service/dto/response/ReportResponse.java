package com.nexts.gs.mars.nexts_gs_mars_field_service.dto.response;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReportResponse {
  private Long id;
  private Long attendanceId;
  private Object data;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
}