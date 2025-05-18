package com.nexts.gs.mars.nexts_gs_mars_field_service.dto.request;

import lombok.Data;

@Data
public class CreateOutletRequest {
  private String code;
  private String name;
  private String address;
  private Double latitude;
  private Double longitude;
  private Integer checkinRadiusMeters;

  private Long adminUnitId;
  private Long saleRepId;
  private Long saleSupervisorId;
  private Long keyAccountManagerId;
}
