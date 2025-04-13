package com.nexts.gs.mars.nexts_gs_mars_field_service.dto.request;

import lombok.Data;
import java.util.List;

@Data
public class ReportItemRequest {
  private String name;
  private String skuCode;
  private String unit;
  private String description;
  private String category;
  private String brand;
  private List<String> reportTypes;
}
