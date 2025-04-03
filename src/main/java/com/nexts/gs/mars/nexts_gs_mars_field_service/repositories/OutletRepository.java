package com.nexts.gs.mars.nexts_gs_mars_field_service.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nexts.gs.mars.nexts_gs_mars_field_service.models.Outlet;

@Repository
public interface OutletRepository extends JpaRepository<Outlet, Long> {
  List<Outlet> findByProvinceId(Long provinceId);
}
