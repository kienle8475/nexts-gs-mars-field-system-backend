package com.nexts.gs.mars.nexts_gs_mars_field_service.controllers;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nexts.gs.mars.nexts_gs_mars_field_service.dto.request.ReportCriteriaRequest;
import com.nexts.gs.mars.nexts_gs_mars_field_service.models.SaleReport;
import com.nexts.gs.mars.nexts_gs_mars_field_service.models.StaffAttendance;
import com.nexts.gs.mars.nexts_gs_mars_field_service.services.AttendanceExportExcelService;
import com.nexts.gs.mars.nexts_gs_mars_field_service.services.ReportService;
import com.nexts.gs.mars.nexts_gs_mars_field_service.services.SaleReportExcelService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/export")
@RequiredArgsConstructor
public class ExportExcelController {
  private final AttendanceExportExcelService attendanceExportExcelService;
  private final SaleReportExcelService saleReportExcelService;
  private final ReportService reportService;

  @GetMapping("/attendances")
  public ResponseEntity<Resource> exportAttendanceExcel(
      @ModelAttribute ReportCriteriaRequest request) throws IOException {
    InputStream template = getClass().getResourceAsStream("/templates/staff_attendance_template.xlsx");
    List<StaffAttendance> attendances = reportService.getAttendanceByCriteria(request);
    ByteArrayInputStream stream = attendanceExportExcelService.exportAttendance(attendances, template);
    String fileName = "BAO CAO CHAM CONG - "
        + LocalDateTime.now().format(DateTimeFormatter.ofPattern("ddMMyy_HHmmss"))
        + ".xlsx";
    return ResponseEntity.ok()
        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName)
        .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
        .body(new InputStreamResource(stream));
  }

  @GetMapping("/sales")
  public ResponseEntity<InputStreamResource> exportSaleReport(
      @ModelAttribute ReportCriteriaRequest request) throws Exception {

    List<SaleReport> reports = new ArrayList<>();

    String templatePath = "templates/sale_report_template.xlsx";

    ByteArrayInputStream stream = saleReportExcelService.export(reports, templatePath);

    String filename = "BAO CAO BAN HANG - "
        + LocalDateTime.now().format(DateTimeFormatter.ofPattern("ddMMyy_HHmmss"))
        + ".xlsx";
    return ResponseEntity.ok()
        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
        .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
        .body(new InputStreamResource(stream));
  }

}
