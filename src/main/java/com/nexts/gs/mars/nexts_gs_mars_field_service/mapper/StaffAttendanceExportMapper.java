package com.nexts.gs.mars.nexts_gs_mars_field_service.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.nexts.gs.mars.nexts_gs_mars_field_service.dto.StaffAttendanceExportDTO;
import com.nexts.gs.mars.nexts_gs_mars_field_service.models.StaffAttendance;

@Mapper(componentModel = "spring")
public interface StaffAttendanceExportMapper {

  @Mapping(target = "index", ignore = true) // sẽ set ở ngoài khi tạo danh sách
  @Mapping(source = "staff.profileImage", target = "profileImageUrl")
  @Mapping(expression = "java(attendance.getCheckinTime() != null ? attendance.getCheckinTime().toLocalDate() : null)", target = "date")
  @Mapping(source = "shift.name", target = "shiftName")
  @Mapping(source = "shift.startTime", target = "shiftStart")
  @Mapping(source = "shift.endTime", target = "shiftEnd")
  @Mapping(source = "shift.outlet.name", target = "outletName")
  @Mapping(source = "shift.outlet.address", target = "outletAddress")
  @Mapping(source = "checkinImage", target = "checkinImage")
  @Mapping(source = "checkoutImage", target = "checkoutImage")
  @Mapping(source = "checkinTime", target = "checkinTime")
  @Mapping(source = "checkoutTime", target = "checkoutTime")
  @Mapping(source = "staff.staffCode", target = "staffCode")
  @Mapping(source = "staff.fullName", target = "fullName")
  @Mapping(source = "staff.trainingDate", target = "trainingDate")
  @Mapping(source = "staff.startDate", target = "startDate")
  @Mapping(source = "staff.passProbationDate", target = "passProbationDate")
  StaffAttendanceExportDTO toExportDto(StaffAttendance attendance);

  List<StaffAttendanceExportDTO> toExportDtoList(List<StaffAttendance> attendances);
}