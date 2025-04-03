package com.nexts.gs.mars.nexts_gs_mars_field_service.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorResponse {
  private int status;
  private String message;
}