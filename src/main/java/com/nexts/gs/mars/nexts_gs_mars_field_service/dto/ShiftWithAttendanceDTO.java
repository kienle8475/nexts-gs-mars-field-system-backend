package com.nexts.gs.mars.nexts_gs_mars_field_service.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import com.nexts.gs.mars.nexts_gs_mars_field_service.models.StaffAttendance;

@Builder
@Getter
@Setter
public class ShiftWithAttendanceDTO {
  private Long shiftId;
  private String shiftName;
  private LocalDateTime startTime;
  private LocalDateTime endTime;

  private OutletBasicDTO outlet;
  private List<StaffAttendance> attendances;
}
