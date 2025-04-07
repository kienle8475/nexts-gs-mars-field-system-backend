package com.nexts.gs.mars.nexts_gs_mars_field_service.export;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import com.nexts.gs.mars.nexts_gs_mars_field_service.models.ReportItem;

@Service
public class GenericExcelExporterImpl implements GenericExcelExporter {

  @Override
  public ByteArrayInputStream exportFromTemplate(
      List<Map<String, Object>> rows,
      InputStream templateStream,
      int keyRowIndex,
      int dataStartRowIndex) {
    try (Workbook workbook = new XSSFWorkbook(templateStream);
        ByteArrayOutputStream out = new ByteArrayOutputStream()) {

      Sheet sheet = workbook.getSheetAt(0);

      // Step 1: đọc key theo cột
      Row keyRow = sheet.getRow(keyRowIndex);
      Map<String, Integer> keyToColumn = new LinkedHashMap<>();
      for (Cell cell : keyRow) {
        String key = cell.getStringCellValue();
        if (key != null && !key.isBlank()) {
          keyToColumn.put(key.trim(), cell.getColumnIndex());
        }
      }

      // Copy style from styleSourceRow to all rows
      Row styleSourceRow = sheet.getRow(keyRowIndex);

      // Step 2: fill dữ liệu từ dòng dataStartRowIndex
      int currentRow = dataStartRowIndex;
      for (Map<String, Object> rowData : rows) {
        Row row = sheet.createRow(currentRow++);
        for (Map.Entry<String, Object> entry : rowData.entrySet()) {
          Integer colIndex = keyToColumn.get(entry.getKey());
          if (colIndex != null) {
            Cell cell = row.createCell(colIndex);
            Object value = entry.getValue();
            cell.setCellValue(value != null ? value.toString() : "");

            // ✅ Copy style từ hàng key
            Cell templateCell = styleSourceRow.getCell(colIndex);
            if (templateCell != null) {
              cell.setCellStyle(templateCell.getCellStyle());
            }
          }

        }
      }

      workbook.write(out);
      return new ByteArrayInputStream(out.toByteArray());
    } catch (IOException e) {
      throw new RuntimeException("Export Excel failed", e);
    }
  }

  public ByteArrayInputStream exportFromTemplateWithDynamicHeaders(
      List<Map<String, Object>> rows,
      InputStream templateStream,
      Map<String, List<ReportItem>> itemsByBrand,
      int fixedColumnCount,
      int brandRowIndex,
      int keyRowIndex) {
    try (Workbook workbook = new XSSFWorkbook(templateStream);
        ByteArrayOutputStream out = new ByteArrayOutputStream()) {

      Sheet sheet = workbook.getSheetAt(0);

      // Build brand header (row 0) and SKU header (row 1)
      Row brandRow = sheet.createRow(brandRowIndex);
      Row keyRow = sheet.createRow(keyRowIndex);

      CellStyle headerStyle = workbook.createCellStyle();
      Font font = workbook.createFont();
      font.setBold(true);
      headerStyle.setFont(font);
      headerStyle.setAlignment(HorizontalAlignment.CENTER);
      headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
      headerStyle.setWrapText(true);

      // Write fixed headers
      AtomicInteger colIndex = new AtomicInteger(0);
      rows.stream().findFirst().ifPresent(firstRow -> {
        for (String key : firstRow.keySet()) {
          if (colIndex.get() >= fixedColumnCount)
            break;
          Cell brandCell = brandRow.createCell(colIndex.get());
          brandCell.setCellStyle(headerStyle);

          Cell keyCell = keyRow.createCell(colIndex.get());
          keyCell.setCellValue(key);
          keyCell.setCellStyle(headerStyle);
          colIndex.getAndIncrement();
        }
      });

      // Write dynamic headers with brand merge
      for (Map.Entry<String, List<ReportItem>> entry : itemsByBrand.entrySet()) {
        String brand = entry.getKey();
        List<ReportItem> items = entry.getValue();

        int startCol = colIndex.get();
        for (ReportItem item : items) {
          Cell keyCell = keyRow.createCell(colIndex.get());
          keyCell.setCellValue(item.getSkuCode());
          keyCell.setCellStyle(headerStyle);
          colIndex.getAndIncrement();
        }
        int endCol = colIndex.get() - 1;
        if (startCol <= endCol) {
          Cell brandCell = brandRow.createCell(startCol);
          brandCell.setCellValue(brand);
          brandCell.setCellStyle(headerStyle);
          sheet.addMergedRegion(new CellRangeAddress(
              brandRowIndex, brandRowIndex, startCol, endCol));
        }
      }

      // Write data rows
      int dataStartRow = keyRowIndex + 1;
      for (int i = 0; i < rows.size(); i++) {
        Map<String, Object> rowData = rows.get(i);
        Row row = sheet.createRow(dataStartRow + i);

        int ci = 0;
        for (String key : rowData.keySet()) {
          Cell cell = row.createCell(ci++);
          Object value = rowData.get(key);
          cell.setCellValue(value != null ? value.toString() : "");
        }
      }

      workbook.write(out);
      return new ByteArrayInputStream(out.toByteArray());
    } catch (Exception e) {
      throw new RuntimeException("Failed to export Excel", e);
    }
  }
}