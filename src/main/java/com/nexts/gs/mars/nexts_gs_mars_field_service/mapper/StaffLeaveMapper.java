package com.nexts.gs.mars.nexts_gs_mars_field_service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.nexts.gs.mars.nexts_gs_mars_field_service.dto.response.StaffLeaveResponse;
import com.nexts.gs.mars.nexts_gs_mars_field_service.models.StaffLeave;

@Mapper(componentModel = "spring")
public interface StaffLeaveMapper {
  @Mapping(source = "attendance.id", target = "attendanceId")
  @Mapping(source = "attendance.staff.id", target = "staffId")
  @Mapping(source = "attendance.staff.fullName", target = "staffName")
  StaffLeaveResponse toResponse(StaffLeave staffLeave);

}
