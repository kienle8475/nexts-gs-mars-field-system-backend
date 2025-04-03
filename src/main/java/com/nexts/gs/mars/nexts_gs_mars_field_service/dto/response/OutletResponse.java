package com.nexts.gs.mars.nexts_gs_mars_field_service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OutletResponse {
  private Long id;
  private String code;
  private String name;
  private String address;
  private Double latitude;
  private Double longitude;
  private Integer checkinRadiusMeters;

  private String provinceName;
  private String saleRepName;
  private String saleSupervisorName;
  private String keyAccountManagerName;
}
