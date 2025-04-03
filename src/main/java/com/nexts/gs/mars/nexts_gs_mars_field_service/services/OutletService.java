package com.nexts.gs.mars.nexts_gs_mars_field_service.services;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.nexts.gs.mars.nexts_gs_mars_field_service.dto.request.CreateOutletRequest;
import com.nexts.gs.mars.nexts_gs_mars_field_service.exceptions.NotFoundException;
import com.nexts.gs.mars.nexts_gs_mars_field_service.mapper.OutletMapper;
import com.nexts.gs.mars.nexts_gs_mars_field_service.models.Outlet;
import com.nexts.gs.mars.nexts_gs_mars_field_service.repositories.OutletRepository;
import com.nexts.gs.mars.nexts_gs_mars_field_service.utils.PageResponseUtil;
import com.nexts.gs.mars.nexts_gs_mars_field_service.dto.response.PageResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OutletService {

  private final OutletRepository outletRepository;

  private final OutletMapper outletMapper;

  public List<Outlet> getAllOutlets() {
    return outletRepository.findAll();
  }

  public PageResponse<Outlet> getAllOutlets(int page, int size) {
    Pageable pageable = PageRequest.of(page, size);
    Page<Outlet> outletPage = outletRepository.findAll(pageable);
    return PageResponseUtil.toPageResponse(outletPage);
  }

  public Outlet getOutletById(Long id) {
    return outletRepository.findById(id)
        .orElseThrow(() -> new NotFoundException("Outlet not found with id: " + id));
  }

  public Outlet createOutlet(CreateOutletRequest outletRequest) {
    Outlet outlet = outletMapper.toEntity(outletRequest);
    outlet.setCreatedAt(LocalDateTime.now());
    outlet.setUpdatedAt(LocalDateTime.now());
    return outletRepository.save(outlet);
  }

  public Outlet updateOutlet(Long id, Outlet updatedOutlet) {
    Outlet existing = getOutletById(id);
    updatedOutlet.setId(existing.getId());
    updatedOutlet.setUpdatedAt(LocalDateTime.now());
    return outletRepository.save(updatedOutlet);
  }

  public void deleteOutlet(Long id) {
    outletRepository.deleteById(id);
  }

  public List<Outlet> getOutletsByProvince(Long provinceId) {
    return outletRepository.findByProvinceId(provinceId);
  }
}