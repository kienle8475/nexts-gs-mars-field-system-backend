package com.nexts.gs.mars.nexts_gs_mars_field_service.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StaffLeaveRequest {
  private Long attendanceId;
  private String leaveType;
  private String note;
}
