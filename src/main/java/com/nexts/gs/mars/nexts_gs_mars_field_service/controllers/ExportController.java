package com.nexts.gs.mars.nexts_gs_mars_field_service.controllers;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nexts.gs.mars.nexts_gs_mars_field_service.dto.request.ReportCriteriaRequest;
import com.nexts.gs.mars.nexts_gs_mars_field_service.models.ExportJob;
import com.nexts.gs.mars.nexts_gs_mars_field_service.models.OosReport;
import com.nexts.gs.mars.nexts_gs_mars_field_service.models.SaleReport;
import com.nexts.gs.mars.nexts_gs_mars_field_service.models.SamplingReport;
import com.nexts.gs.mars.nexts_gs_mars_field_service.models.StaffAttendance;
import com.nexts.gs.mars.nexts_gs_mars_field_service.models.StaffLeave;
import com.nexts.gs.mars.nexts_gs_mars_field_service.services.ExportJobService;
import com.nexts.gs.mars.nexts_gs_mars_field_service.services.ReportService;
import com.nexts.gs.mars.nexts_gs_mars_field_service.services.export.pptx.ExportPPTXAsyncService;
import com.nexts.gs.mars.nexts_gs_mars_field_service.services.export.xlsx.AttendanceReportExcelService;
import com.nexts.gs.mars.nexts_gs_mars_field_service.services.export.xlsx.OosReportExcelService;
import com.nexts.gs.mars.nexts_gs_mars_field_service.services.export.xlsx.SaleReportExcelService;
import com.nexts.gs.mars.nexts_gs_mars_field_service.services.export.xlsx.SamplingReportExcelService;
import com.nexts.gs.mars.nexts_gs_mars_field_service.services.export.xlsx.StaffLeaveExcelService;

import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Comparator;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.version}/exports")
public class ExportController {
  private final ExportJobService jobService;
  private final ExportPPTXAsyncService asyncService;
  private final AttendanceReportExcelService attendanceExportExcelService;
  private final SaleReportExcelService saleReportExcelService;
  private final OosReportExcelService oosReportExcelService;
  private final ReportService reportService;
  private final SamplingReportExcelService samplingReportExcelService;
  private final StaffLeaveExcelService staffLeaveExcelService;

  @GetMapping("/generate-pptx")
  public ResponseEntity<Map<String, Object>> startExportPPTX(@ModelAttribute ReportCriteriaRequest request)
      throws IOException {
    ExportJob job = jobService.createJob();
    List<StaffAttendance> attendances = reportService.getAttendanceByCriteria(request);
    attendances = new ArrayList<>(attendances);
    attendances.sort(Comparator.comparing(a -> a.getStaff().getId()));
    InputStream template = getClass().getResourceAsStream("/templates/staff_attendance_template.pptx");
    asyncService.exportAttendancePPTX(job.getId(), attendances, template);
    return ResponseEntity.ok(Map.of("jobId", job.getId()));
  }

  @GetMapping("/{id}/status")
  public ResponseEntity<ExportJob> getStatus(@PathVariable Long id) {
    return ResponseEntity.ok(jobService.getById(id));
  }

  @GetMapping("download-pptx/{id}")
  public ResponseEntity<Resource> download(@PathVariable Long id) throws IOException {
    ExportJob job = jobService.getById(id);
    if (!"DONE".equals(job.getStatus())) {
      throw new IllegalStateException("Export not ready");
    }

    Path file = Paths.get(job.getFilepath());
    Resource resource = new UrlResource(file.toUri());

    String filename = "BAO CAO CHAM CONG - "
        + LocalDateTime.now().format(DateTimeFormatter.ofPattern("ddMMyy_HHmmss"))
        + ".pptx";
    return ResponseEntity.ok()
        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
        .header(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, "Content-Disposition")
        .contentType(
            MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.presentationml.presentation"))
        .body(resource);
  }

  @GetMapping("/attendances")
  public ResponseEntity<Resource> exportAttendanceExcel(
      @ModelAttribute ReportCriteriaRequest request) throws IOException {
    InputStream template = getClass().getResourceAsStream("/templates/staff_attendance_template.xlsx");
    List<StaffAttendance> attendances = reportService.getAttendanceByCriteria(request);
    ByteArrayInputStream stream = attendanceExportExcelService.export(attendances, template);
    String fileName = "BAO CAO CHAM CONG - "
        + LocalDateTime.now().format(DateTimeFormatter.ofPattern("ddMMyy_HHmmss"))
        + ".xlsx";
    return ResponseEntity.ok()
        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName)
        .header(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, "Content-Disposition")
        .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
        .body(new InputStreamResource(stream));
  }

  @GetMapping("/sales")
  public ResponseEntity<InputStreamResource> exportSaleReport(
      @ModelAttribute ReportCriteriaRequest request) throws Exception {
    List<SaleReport> reports = reportService.getSaleReportsByCriteria(request);
    String templatePath = "templates/sale_report_template.xlsx";
    ByteArrayInputStream stream = saleReportExcelService.export(reports, templatePath);
    String filename = "BAO CAO BAN HANG - "
        + LocalDateTime.now().format(DateTimeFormatter.ofPattern("ddMMyy_HHmmss"))
        + ".xlsx";
    return ResponseEntity.ok()
        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
        .header(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, "Content-Disposition")
        .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
        .body(new InputStreamResource(stream));
  }

  @GetMapping("/oos")
  public ResponseEntity<InputStreamResource> exportOOSReport(
      @ModelAttribute ReportCriteriaRequest request) throws Exception {
    List<OosReport> reports = reportService.getOosReportsByCriteria(request);
    String templatePath = "templates/oos_report_template.xlsx";
    ByteArrayInputStream stream = oosReportExcelService.export(reports, templatePath);
    String filename = "BAO CAO OOS - "
        + LocalDateTime.now().format(DateTimeFormatter.ofPattern("ddMMyy_HHmmss"))
        + ".xlsx";
    return ResponseEntity.ok()
        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
        .header(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, "Content-Disposition")
        .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
        .body(new InputStreamResource(stream));
  }

  @GetMapping("/sampling")
  public ResponseEntity<InputStreamResource> exportSamplingReport(
      @ModelAttribute ReportCriteriaRequest request) throws Exception {
    List<SamplingReport> reports = reportService.getSamplingReportsByCriteria(request);
    String templatePath = "templates/sampling_report_template.xlsx";
    ByteArrayInputStream stream = samplingReportExcelService.export(reports, templatePath);
    String filename = "BAO CAO SAMPLING - "
        + LocalDateTime.now().format(DateTimeFormatter.ofPattern("ddMMyy_HHmmss"))
        + ".xlsx";
    return ResponseEntity.ok()
        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
        .header(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, "Content-Disposition")
        .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
        .body(new InputStreamResource(stream));
  }

  @GetMapping("/staff-leave")
  public ResponseEntity<InputStreamResource> exportStaffLeave(
      @ModelAttribute ReportCriteriaRequest request) throws Exception {
    List<StaffLeave> leaves = reportService.getStaffLeavesByCriteria(request);
    InputStream template = getClass().getResourceAsStream("/templates/staff_leave_template.xlsx");
    ByteArrayInputStream stream = staffLeaveExcelService.export(leaves, template);
    String filename = "BAO CAO ROI VI TRI - "
        + LocalDateTime.now().format(DateTimeFormatter.ofPattern("ddMMyy_HHmmss"))
        + ".xlsx";
    return ResponseEntity.ok()
        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
        .header(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, "Content-Disposition")
        .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
        .body(new InputStreamResource(stream));
  }
}
