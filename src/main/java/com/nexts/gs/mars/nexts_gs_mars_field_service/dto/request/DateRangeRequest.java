package com.nexts.gs.mars.nexts_gs_mars_field_service.dto.request;

import java.time.LocalDate;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DateRangeRequest {
  private LocalDate dateFrom;
  private LocalDate dateTo;
}
