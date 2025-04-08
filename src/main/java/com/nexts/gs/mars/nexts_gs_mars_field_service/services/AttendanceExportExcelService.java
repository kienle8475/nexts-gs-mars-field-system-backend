package com.nexts.gs.mars.nexts_gs_mars_field_service.services;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import com.nexts.gs.mars.nexts_gs_mars_field_service.models.StaffAttendance;
import com.nexts.gs.mars.nexts_gs_mars_field_service.models.StaffProfile;
import com.nexts.gs.mars.nexts_gs_mars_field_service.models.WorkingShift;
import com.nexts.gs.mars.nexts_gs_mars_field_service.models.Outlet;
import com.nexts.gs.mars.nexts_gs_mars_field_service.export.GenericExcelExporter;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AttendanceExportExcelService {
  private final GenericExcelExporter genericExcelExporter;

  public List<Map<String, Object>> toExportRowMapsAttendance(List<StaffAttendance> attendances) {
    return IntStream.range(0, attendances.size())
        .mapToObj(i -> {
          StaffAttendance a = attendances.get(i);
          StaffProfile s = a.getStaff();
          WorkingShift shift = a.getShift();
          Outlet o = shift.getOutlet();

          Map<String, Object> row = new LinkedHashMap<>();
          row.put("index", i + 1);
          row.put("profileImage", s.getProfileImage() != null ? s.getProfileImage() : "");
          row.put("date", shift.getStartTime() != null ? shift.getStartTime().toLocalDate() : null);
          row.put("shiftName", shift.getName());
          row.put("shiftStart", shift.getStartTime().format(DateTimeFormatter.ofPattern("HH:mm")));
          row.put("shiftEnd", shift.getEndTime().format(DateTimeFormatter.ofPattern("HH:mm")));
          row.put("outletName", o.getName());
          row.put("outletAddress", o.getAddress());
          row.put("checkinImage", a.getCheckinImage() != null ? a.getCheckinImage() : "");
          row.put("checkoutImage", a.getCheckoutImage() != null ? a.getCheckoutImage() : "");
          row.put("checkinTime",
              a.getCheckinTime() != null ? a.getCheckinTime().format(DateTimeFormatter.ofPattern("HH:mm")) : "");
          row.put("checkoutTime",
              a.getCheckoutTime() != null ? a.getCheckoutTime().format(DateTimeFormatter.ofPattern("HH:mm")) : "");
          row.put("staffCode", s.getStaffCode() != null ? s.getStaffCode() : "");
          row.put("fullName", s.getFullName() != null ? s.getFullName() : "");
          row.put("trainingDate", s.getTrainingDate() != null ? s.getTrainingDate() : "");
          row.put("startDate", s.getStartDate() != null ? s.getStartDate() : "");
          row.put("passProbationDate", s.getPassProbationDate() != null ? s.getPassProbationDate() : "");
          return row;
        })
        .toList();
  }

  public ByteArrayInputStream export(List<StaffAttendance> attendances, InputStream templateStream) {
    List<Map<String, Object>> rows = toExportRowMapsAttendance(attendances);
    return genericExcelExporter.exportFromTemplate(rows, templateStream, 1, 2);
  }
}
