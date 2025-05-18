package com.nexts.gs.mars.nexts_gs_mars_field_service.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.nexts.gs.mars.nexts_gs_mars_field_service.models.AdministrativeUnit;
import com.nexts.gs.mars.nexts_gs_mars_field_service.repositories.AdministrativeUnitRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdministrativeUnitService {
  private final AdministrativeUnitRepository administrativeUnitRepository;

  public List<AdministrativeUnit> getAllAdministrativeUnits() {
    return administrativeUnitRepository.findAll();
  }
}
