package com.nexts.gs.mars.nexts_gs_mars_field_service.export;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import com.nexts.gs.mars.nexts_gs_mars_field_service.models.ReportItem;

public interface GenericExcelExporter {
  ByteArrayInputStream exportFromTemplate(
      List<Map<String, Object>> rows,
      InputStream templateStream,
      int keyRowIndex,
      int dataStartRowIndex);

  ByteArrayInputStream exportFromTemplateWithDynamicHeaders(
      List<Map<String, Object>> rows,
      InputStream templateStream,
      Map<String, List<ReportItem>> itemsByBrand,
      int fixedColumnCount,
      int brandRowIndex,
      int keyRowIndex);
}