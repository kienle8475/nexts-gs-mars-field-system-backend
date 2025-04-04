package com.nexts.gs.mars.nexts_gs_mars_field_service.dto.response;

import lombok.Data;
import lombok.Builder;
import java.util.List;

@Data
@Builder
public class ReportItemResponse {
  private Long id;
  private String name;
  private String skuCode;
  private String unit;
  private String description;
  private String category;
  private String brand;
  private List<String> reportTypes;
}
