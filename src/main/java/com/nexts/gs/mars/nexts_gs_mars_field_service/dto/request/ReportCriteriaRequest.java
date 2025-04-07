package com.nexts.gs.mars.nexts_gs_mars_field_service.dto.request;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReportCriteriaRequest {

  private Long staffId;
  private Long outletId;
  private Long provinceId;
  private Boolean hasReport;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
  private LocalDate date;

  public boolean hasDate() {
    return date != null;
  }

}
