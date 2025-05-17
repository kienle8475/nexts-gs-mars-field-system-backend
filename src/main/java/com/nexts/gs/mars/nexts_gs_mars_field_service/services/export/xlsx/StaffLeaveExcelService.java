package com.nexts.gs.mars.nexts_gs_mars_field_service.services.export.xlsx;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import org.springframework.stereotype.Service;

import com.nexts.gs.mars.nexts_gs_mars_field_service.enums.LeaveType;
import com.nexts.gs.mars.nexts_gs_mars_field_service.export.GenericExcelExporter;
import com.nexts.gs.mars.nexts_gs_mars_field_service.models.Outlet;
import com.nexts.gs.mars.nexts_gs_mars_field_service.models.StaffAttendance;
import com.nexts.gs.mars.nexts_gs_mars_field_service.models.StaffLeave;
import com.nexts.gs.mars.nexts_gs_mars_field_service.models.StaffProfile;
import com.nexts.gs.mars.nexts_gs_mars_field_service.models.WorkingShift;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StaffLeaveExcelService {
  private final GenericExcelExporter genericExcelExporter;

  public List<Map<String, Object>> toExportRowMaps(List<StaffLeave> leaves) {
    return IntStream.range(0, leaves.size())
        .mapToObj(i -> {
          StaffLeave a = leaves.get(i);
          StaffAttendance attendance = a.getAttendance();
          StaffProfile s = attendance.getStaff();
          WorkingShift shift = attendance.getShift();
          Outlet o = shift.getOutlet();

          Map<String, Object> row = new LinkedHashMap<>();
          row.put("index", i + 1);
          row.put("date", a.getStartTime() != null ? a.getStartTime().toLocalDate() : null);
          row.put("shiftName", shift.getName());
          row.put("staffCode", s.getStaffCode() != null ? s.getStaffCode() : "");
          row.put("fullName", s.getFullName() != null ? s.getFullName() : "");
          row.put("outletName", o.getName());
          row.put("startTime",
              a.getStartTime() != null ? a.getStartTime().format(DateTimeFormatter.ofPattern("HH:mm")) : "");
          row.put("endTime", a.getEndTime() != null ? a.getEndTime().format(DateTimeFormatter.ofPattern("HH:mm")) : "");
          row.put("leaveType", a.getLeaveType() != null ? LeaveType.valueOf(a.getLeaveType()).getLabel() : "");
          row.put("note", a.getNote());
          return row;
        })
        .toList();
  }

  public ByteArrayInputStream export(List<StaffLeave> leaves, InputStream templateStream) {
    List<Map<String, Object>> rows = toExportRowMaps(leaves);
    return genericExcelExporter.exportFromTemplate(rows, templateStream, 1, 2);
  }

}
