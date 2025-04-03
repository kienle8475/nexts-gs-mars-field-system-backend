package com.nexts.gs.mars.nexts_gs_mars_field_service.dto.response;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WorkShiftResponse {
  private Long id;
  private String name;
  private LocalDateTime startTime;
  private LocalDateTime endTime;
  private WorkShiftOutletResponse outlet;
  private boolean isCheckedIn;
  private int totalCheckedIn;
}
