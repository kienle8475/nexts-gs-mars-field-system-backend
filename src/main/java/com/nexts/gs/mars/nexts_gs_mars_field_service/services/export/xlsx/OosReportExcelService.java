package com.nexts.gs.mars.nexts_gs_mars_field_service.services.export.xlsx;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import com.nexts.gs.mars.nexts_gs_mars_field_service.models.OosReport;
import com.nexts.gs.mars.nexts_gs_mars_field_service.models.Outlet;
import com.nexts.gs.mars.nexts_gs_mars_field_service.models.ReportItem;
import com.nexts.gs.mars.nexts_gs_mars_field_service.models.StaffAttendance;
import com.nexts.gs.mars.nexts_gs_mars_field_service.models.StaffProfile;
import com.nexts.gs.mars.nexts_gs_mars_field_service.models.WorkingShift;
import com.nexts.gs.mars.nexts_gs_mars_field_service.repositories.ReportItemRepository;
import com.nexts.gs.mars.nexts_gs_mars_field_service.export.GenericExcelExporter;
import com.nexts.gs.mars.nexts_gs_mars_field_service.enums.ReportType;
import lombok.RequiredArgsConstructor;
import java.util.Comparator;

@Service
@RequiredArgsConstructor
public class OosReportExcelService {
  private final ReportItemRepository reportItemRepository;
  private final GenericExcelExporter genericExcelExporter;

  public ByteArrayInputStream export(List<OosReport> reports, String templatePath) throws IOException {

    List<ReportItem> items = reportItemRepository.findByReportType(ReportType.OOS).stream()
        .sorted(Comparator.comparingInt(ReportItem::getOrder))
        .collect(Collectors.toList());

    // Step 2: Group items by brand
    Map<String, List<ReportItem>> itemsByBrand = items.stream()
        .collect(Collectors.groupingBy(ReportItem::getBrand, LinkedHashMap::new, Collectors.toList()));

    // Step 3: Build ordered list of sku
    List<String> orderedSkus = itemsByBrand.values().stream()
        .flatMap(List::stream)
        .map(ReportItem::getSkuCode)
        .toList();

    // Step 4: Build header row keys and display names
    List<String> fixedKeys = List.of("index", "province", "outlet", "ss", "sr", "kam", "date", "shift", "staffCode");
    List<String> fixedHeaders = List.of("STT", "Tỉnh thành", "Outlet", "SS", "SR", "KAM", "Ngày", "Ca", "Staff Code");

    List<String> dynamicKeys = orderedSkus;
    List<String> dynamicHeaders = orderedSkus;

    List<String> allKeys = new ArrayList<>(fixedKeys);
    allKeys.addAll(dynamicKeys);

    List<String> allHeaders = new ArrayList<>(fixedHeaders);
    allHeaders.addAll(dynamicHeaders);

    // Step 5: Build data rows
    List<Map<String, Object>> rows = new ArrayList<>();
    int index = 1;
    for (OosReport report : reports) {
      Map<String, Object> row = new LinkedHashMap<>();
      StaffAttendance a = report.getAttendance();
      StaffProfile s = a.getStaff();
      WorkingShift shift = a.getShift();
      Outlet o = shift.getOutlet();

      row.put("index", index++);
      row.put("province", o.getProvince() != null ? o.getProvince().getName() : "");
      row.put("outlet", o.getName());
      row.put("ss", o.getSaleSupervisor() != null ? o.getSaleSupervisor().getFullName() : "");
      row.put("sr", o.getSaleRep() != null ? o.getSaleRep().getFullName() : "");
      row.put("kam", o.getKeyAccountManager() != null ? o.getKeyAccountManager().getFullName() : "");
      row.put("date", shift.getStartTime().toLocalDate().format(DateTimeFormatter.ISO_DATE));
      row.put("shift", shift.getName());
      row.put("staffCode", s.getStaffCode());

      Map<String, Integer> skuToValue = report.getData().stream()
          .collect(Collectors.toMap(
              d -> d.get("sku").toString(),
              d -> ((Number) d.get("pcs")).intValue(),
              (a1, a2) -> a2));

      for (String sku : dynamicKeys) {
        row.put(sku, skuToValue.getOrDefault(sku, 0));
      }
      rows.add(row);
    }

    // Step 6: Load template and fill data
    try (InputStream templateStream = new ClassPathResource(templatePath).getInputStream()) {
      return genericExcelExporter.exportFromTemplateWithDynamicHeaders(
          rows, templateStream, itemsByBrand, 0, 2);
    }
  }
}