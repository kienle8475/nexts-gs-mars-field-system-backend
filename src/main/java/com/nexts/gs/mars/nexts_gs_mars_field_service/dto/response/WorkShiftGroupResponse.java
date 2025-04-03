package com.nexts.gs.mars.nexts_gs_mars_field_service.dto.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkShiftGroupResponse {
  private List<WorkShiftResponse> pendingCheckout;
  private List<WorkShiftResponse> available;
}
