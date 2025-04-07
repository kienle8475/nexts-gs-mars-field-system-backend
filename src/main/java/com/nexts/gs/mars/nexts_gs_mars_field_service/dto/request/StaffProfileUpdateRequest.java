package com.nexts.gs.mars.nexts_gs_mars_field_service.dto.request;

import java.time.LocalDate;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StaffProfileUpdateRequest {
  private String password;
  private String staffCode;
  private String fullName;
  private String profileImage;
  private LocalDate trainingDate;
  private LocalDate startDate;
  private LocalDate passProbationDate;
}
