package com.nexts.gs.mars.nexts_gs_mars_field_service.services;

import org.springframework.stereotype.Service;

import com.nexts.gs.mars.nexts_gs_mars_field_service.repositories.ReportItemRepository;

import lombok.RequiredArgsConstructor;

import com.nexts.gs.mars.nexts_gs_mars_field_service.mapper.ReportItemMapper;
import com.nexts.gs.mars.nexts_gs_mars_field_service.dto.request.ReportItemRequest;
import com.nexts.gs.mars.nexts_gs_mars_field_service.dto.response.ReportItemResponse;
import com.nexts.gs.mars.nexts_gs_mars_field_service.enums.ReportType;
import com.nexts.gs.mars.nexts_gs_mars_field_service.models.ReportItem;
import com.nexts.gs.mars.nexts_gs_mars_field_service.models.ReportItemUsage;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportItemService {
  private final ReportItemRepository reportItemRepository;
  private final ReportItemMapper mapper;

  public ReportItemResponse create(ReportItemRequest request) {

    if (reportItemRepository.findBySkuCode(request.getSkuCode()).isPresent()) {
      throw new RuntimeException("Report Item already exists");
    }

    ReportItem item = ReportItem.builder()
        .name(request.getName())
        .skuCode(request.getSkuCode())
        .unit(request.getUnit())
        .description(request.getDescription())
        .build();

    List<ReportItemUsage> usages = request.getReportTypes().stream()
        .map(type -> ReportItemUsage.builder()
            .item(item)
            .reportType(ReportType.valueOf(type))
            .build())
        .toList();

    item.setUsages(usages);

    ReportItem saved = reportItemRepository.save(item);

    return mapper.toResponse(saved);
  }

  public List<ReportItemResponse> getByReportType(String type) {
    ReportType reportType = ReportType.valueOf(type.toUpperCase());
    return reportItemRepository.findByReportType(reportType).stream()
        .sorted(Comparator.comparing(ReportItem::getOrder))
        .map(mapper::toResponse)
        .toList();
  }

  public List<ReportItemResponse> getAll() {
    return reportItemRepository.findAll().stream()
        .map(mapper::toResponse)
        .toList();
  }

  public ReportItemResponse update(String id, ReportItemRequest request) {
    ReportItem item = reportItemRepository.findById(Long.parseLong(id))
        .orElseThrow(() -> new RuntimeException("Report Item not found"));

    if (reportItemRepository.findBySkuCode(request.getSkuCode()).isPresent()) {
      throw new RuntimeException("Report Item already exists");
    }

    item.setSkuCode(request.getSkuCode());
    item.setName(request.getName());
    item.setUnit(request.getUnit());
    item.setDescription(request.getDescription());
    item.setCategory(request.getCategory());
    item.setBrand(request.getBrand());

    ReportItem saved = reportItemRepository.save(item);

    return mapper.toResponse(saved);
  }
}
