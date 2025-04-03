package com.nexts.gs.mars.nexts_gs_mars_field_service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApiResponse<T> {
  private String message;
  private int status;
  private T data;
}
