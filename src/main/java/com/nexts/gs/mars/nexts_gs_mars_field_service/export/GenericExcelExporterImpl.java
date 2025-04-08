package com.nexts.gs.mars.nexts_gs_mars_field_service.export;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
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
      int brandRowIndex,
      int keyRowIndex) {
    try (Workbook workbook = new XSSFWorkbook(templateStream);
        ByteArrayOutputStream out = new ByteArrayOutputStream()) {

      Sheet sheet = workbook.getSheetAt(0);

      // Reference existing rows from template
      Row brandRow = sheet.getRow(brandRowIndex);
      Row displayRow = sheet.getRow(brandRowIndex + 1);
      Row keyRow = sheet.getRow(keyRowIndex);

      // Auto detect fixed columns by scanning key row
      int fixedColumnCount = 0;
      short lastCell = keyRow.getLastCellNum();
      for (int i = 0; i < lastCell; i++) {
        Cell keyCell = keyRow.getCell(i);
        if (keyCell == null || keyCell.getStringCellValue().isBlank())
          break;
        fixedColumnCount++;
      }

      // Clone the style from the last fixed column (if available)
      Cell referenceStyleCell = keyRow.getCell(fixedColumnCount - 1);
      CellStyle clonedStyle = referenceStyleCell != null ? referenceStyleCell.getCellStyle() : null;
      if (clonedStyle != null) {
        clonedStyle.setBorderTop(BorderStyle.THIN);
        clonedStyle.setBorderBottom(BorderStyle.THIN);
        clonedStyle.setBorderLeft(BorderStyle.THIN);
        clonedStyle.setBorderRight(BorderStyle.THIN);
      }
      // Write dynamic headers only (after fixed columns)
      AtomicInteger colIndex = new AtomicInteger(fixedColumnCount);
      for (Map.Entry<String, List<ReportItem>> entry : itemsByBrand.entrySet()) {
        String brand = entry.getKey();
        List<ReportItem> items = entry.getValue();

        int startCol = colIndex.get();
        for (ReportItem item : items) {
          Cell displayCell = displayRow.createCell(colIndex.get());
          displayCell.setCellValue(item.getName());
          if (clonedStyle != null)
            displayCell.setCellStyle(clonedStyle);

          Cell keyCell = keyRow.createCell(colIndex.get());
          keyCell.setCellValue(item.getSkuCode());
          if (clonedStyle != null)
            keyCell.setCellStyle(clonedStyle);

          colIndex.getAndIncrement();
        }
        int endCol = colIndex.get() - 1;
        if (startCol <= endCol) {
          Cell brandCell = brandRow.createCell(startCol);
          brandCell.setCellValue(brand);
          if (clonedStyle != null)
            brandCell.setCellStyle(clonedStyle);
          if (startCol < endCol) {
            sheet.addMergedRegion(new CellRangeAddress(
                brandRowIndex, brandRowIndex, startCol, endCol));
          }
        }
      }

      // Build ordered keys from final key row
      Map<Integer, String> columnKeyMap = new LinkedHashMap<>();
      for (int i = 0; i < keyRow.getLastCellNum(); i++) {
        Cell keyCell = keyRow.getCell(i);
        if (keyCell != null && !keyCell.getStringCellValue().isBlank()) {
          columnKeyMap.put(i, keyCell.getStringCellValue());
        }
      }

      // Get style of last row (for row formatting)
      int lastHeaderRowIndex = keyRowIndex;
      Row sampleRow = sheet.getRow(lastHeaderRowIndex);
      Map<Integer, CellStyle> styleMap = new LinkedHashMap<>();
      for (Cell cell : sampleRow) {
        styleMap.put(cell.getColumnIndex(), cell.getCellStyle());
      }

      // Write data rows
      int dataStartRow = keyRowIndex + 1;
      for (int i = 0; i < rows.size(); i++) {
        Map<String, Object> rowData = rows.get(i);
        Row row = sheet.createRow(dataStartRow + i);

        for (Map.Entry<Integer, String> entry : columnKeyMap.entrySet()) {
          int col = entry.getKey();
          String key = entry.getValue();
          Object value = rowData.get(key);
          Cell cell = row.createCell(col);
          cell.setCellValue(value != null ? value.toString() : "");

          if (styleMap.containsKey(col)) {
            cell.setCellStyle(styleMap.get(col));
          }
        }
      }

      workbook.write(out);
      return new ByteArrayInputStream(out.toByteArray());
    } catch (Exception e) {
      throw new RuntimeException("Failed to export Excel", e);
    }
  }
}