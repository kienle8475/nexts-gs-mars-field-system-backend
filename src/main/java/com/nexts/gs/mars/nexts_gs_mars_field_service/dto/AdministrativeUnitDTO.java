package com.nexts.gs.mars.nexts_gs_mars_field_service.dto;

import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdministrativeUnitDTO {
  private Long id;
  private String name;
  private String type;
  private Long parentId;
  private AdministrativeUnitDTO parent;
  private List<AdministrativeUnitDTO> children;
}