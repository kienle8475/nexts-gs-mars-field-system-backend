package com.nexts.gs.mars.nexts_gs_mars_field_service.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nexts.gs.mars.nexts_gs_mars_field_service.models.AdministrativeUnit;

public interface AdministrativeUnitRepository extends JpaRepository<AdministrativeUnit, Long> {
  boolean existsByNameAndType(String name, String type);
}
