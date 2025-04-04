package com.nexts.gs.mars.nexts_gs_mars_field_service.dto.request;

import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportRequest {
  private Long attendanceId;
  private List<Map<String, Object>> data;
}