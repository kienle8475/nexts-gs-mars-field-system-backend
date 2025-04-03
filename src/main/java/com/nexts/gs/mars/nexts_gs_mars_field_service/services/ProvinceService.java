package com.nexts.gs.mars.nexts_gs_mars_field_service.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.nexts.gs.mars.nexts_gs_mars_field_service.models.Province;
import com.nexts.gs.mars.nexts_gs_mars_field_service.repositories.ProvinceRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProvinceService {
  private final ProvinceRepository provinceRepository;

  public List<Province> getAllProvinces() {
    return provinceRepository.findAll();
  }

  public Province getProvinceById(Long id) {
    return provinceRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Province not found with id: " + id));
  }

  public Province createProvince(Province province) {
    if (provinceRepository.existsByNameIgnoreCase(province.getName())) {
      throw new RuntimeException("Province already exists with name: " + province.getName());
    }
    return provinceRepository.save(province);
  }

}