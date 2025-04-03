package com.nexts.gs.mars.nexts_gs_mars_field_service.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nexts.gs.mars.nexts_gs_mars_field_service.models.Province;

@Repository
public interface ProvinceRepository extends JpaRepository<Province, Long> {
  boolean existsByNameIgnoreCase(String name);
}
