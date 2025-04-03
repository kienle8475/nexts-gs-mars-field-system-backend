package com.nexts.gs.mars.nexts_gs_mars_field_service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.nexts.gs.mars.nexts_gs_mars_field_service.dto.response.WorkShiftResponse;
import com.nexts.gs.mars.nexts_gs_mars_field_service.models.WorkingShift;

@Mapper(componentModel = "spring")
public interface WorkingShiftMapper {
  @Mapping(target = "outlet.id", source = "outlet.id")
  @Mapping(target = "outlet.name", source = "outlet.name")
  @Mapping(target = "outlet.code", source = "outlet.code")
  @Mapping(target = "isCheckedIn", ignore = true)
  @Mapping(target = "totalCheckedIn", ignore = true)
  WorkShiftResponse toResponse(WorkingShift workingShift);
}
