package com.nexts.gs.mars.nexts_gs_mars_field_service.dto.request;

import lombok.Data;

@Data
public class RegisterRequest {
  private String username;
  private String password;
  private String role; // "PS", "SALE", "ADMIN"
}