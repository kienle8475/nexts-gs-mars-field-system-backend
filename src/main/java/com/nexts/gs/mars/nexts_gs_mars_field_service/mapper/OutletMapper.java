package com.nexts.gs.mars.nexts_gs_mars_field_service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.nexts.gs.mars.nexts_gs_mars_field_service.dto.OutletBasicDTO;
import com.nexts.gs.mars.nexts_gs_mars_field_service.dto.request.CreateOutletRequest;
import com.nexts.gs.mars.nexts_gs_mars_field_service.dto.response.OutletResponse;
import com.nexts.gs.mars.nexts_gs_mars_field_service.models.Outlet;

@Mapper(componentModel = "spring")
public interface OutletMapper {
  // map DTO → Entity
  @Mapping(target = "province.id", source = "provinceId")
  @Mapping(target = "saleRep.id", source = "saleRepId")
  @Mapping(target = "saleSupervisor.id", source = "saleSupervisorId")
  @Mapping(target = "keyAccountManager.id", source = "keyAccountManagerId")
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "updatedAt", ignore = true)
  Outlet toEntity(CreateOutletRequest dto);

  // map Entity → Response
  @Mapping(target = "provinceName", source = "province.name")
  @Mapping(target = "saleRepName", source = "saleRep.fullName")
  @Mapping(target = "saleSupervisorName", source = "saleSupervisor.fullName")
  @Mapping(target = "keyAccountManagerName", source = "keyAccountManager.fullName")
  OutletResponse toResponse(Outlet outlet);

  @Mapping(target = "province", source = "province.name")
  OutletBasicDTO toBasicDTO(Outlet outlet);

}
