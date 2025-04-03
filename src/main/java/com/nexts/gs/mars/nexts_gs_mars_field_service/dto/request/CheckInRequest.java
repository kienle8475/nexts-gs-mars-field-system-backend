package com.nexts.gs.mars.nexts_gs_mars_field_service.dto.request;

import com.nexts.gs.mars.nexts_gs_mars_field_service.models.GeoLocation;

import lombok.Data;

@Data
public class CheckInRequest {
  private Long staffId;
  private Long shiftId;
  private GeoLocation location;
}
