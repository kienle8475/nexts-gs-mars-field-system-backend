package com.nexts.gs.mars.nexts_gs_mars_field_service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.nexts.gs.mars.nexts_gs_mars_field_service.dto.response.ReportResponse;
import com.nexts.gs.mars.nexts_gs_mars_field_service.models.BaseReport;

@Mapper(componentModel = "spring")
public interface ReportMapper {
  @Mapping(target = "attendanceId", source = "attendance.id")
  ReportResponse toResponse(BaseReport report);
}