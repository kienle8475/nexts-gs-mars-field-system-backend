package com.nexts.gs.mars.nexts_gs_mars_field_service.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class OutletBasicDTO {
  private Long id;
  private String code;
  private String name;
  private String province;
}