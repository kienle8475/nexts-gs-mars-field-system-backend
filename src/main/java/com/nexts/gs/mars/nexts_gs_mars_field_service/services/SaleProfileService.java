package com.nexts.gs.mars.nexts_gs_mars_field_service.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.nexts.gs.mars.nexts_gs_mars_field_service.dto.response.SaleSimpleOptionResponse;
import com.nexts.gs.mars.nexts_gs_mars_field_service.repositories.SaleProfileRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SaleProfileService {
  private final SaleProfileRepository saleProfileRepository;

  public List<SaleSimpleOptionResponse> getSimple() {
    return saleProfileRepository.findSimple();
  }
}