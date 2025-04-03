package com.nexts.gs.mars.nexts_gs_mars_field_service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WorkShiftOutletResponse {
  private Long id;
  private String name;
  private String code;
}
