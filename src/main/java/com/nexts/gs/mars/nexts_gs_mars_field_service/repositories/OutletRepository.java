package com.nexts.gs.mars.nexts_gs_mars_field_service.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.nexts.gs.mars.nexts_gs_mars_field_service.dto.response.OutletSimpleOptionReponse;
import com.nexts.gs.mars.nexts_gs_mars_field_service.models.Outlet;

@Repository
public interface OutletRepository extends JpaRepository<Outlet, Long> {
  List<Outlet> findByProvinceId(Long provinceId);

  @Query("""
      SELECT new com.nexts.gs.mars.nexts_gs_mars_field_service.dto.response.OutletSimpleOptionReponse(o.id, o.name)
      FROM Outlet o
      WHERE (:provinceId IS NULL OR o.province.id = :provinceId)
      """)
  List<OutletSimpleOptionReponse> findSimpleByProvince(@Param("provinceId") Long provinceId);
}
