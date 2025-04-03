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
public class StaffLeaveResponse {
  private Long id;
  private String leaveType;
  private LocalDateTime startTime;
  private LocalDateTime endTime;
  private String note;
  private Long attendanceId;
  private Long staffId;
  private String staffName;
}
