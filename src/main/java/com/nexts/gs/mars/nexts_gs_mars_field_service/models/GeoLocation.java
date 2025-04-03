package com.nexts.gs.mars.nexts_gs_mars_field_service.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GeoLocation {
  private Double lat;
  private Double lng;
  private Double acc; // accuracy in meters
}
