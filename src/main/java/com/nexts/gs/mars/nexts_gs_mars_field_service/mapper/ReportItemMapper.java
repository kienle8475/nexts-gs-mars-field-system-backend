package com.nexts.gs.mars.nexts_gs_mars_field_service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.nexts.gs.mars.nexts_gs_mars_field_service.dto.response.ReportItemResponse;
import com.nexts.gs.mars.nexts_gs_mars_field_service.models.ReportItem;

@Mapper(componentModel = "spring")
public interface ReportItemMapper {
  @Mapping(target = "reportTypes", expression = "java(item.getUsages().stream().map(u -> u.getReportType().name()).toList())")
  ReportItemResponse toResponse(ReportItem item);
}
