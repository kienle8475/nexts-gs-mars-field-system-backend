package com.nexts.gs.mars.nexts_gs_mars_field_service.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.nexts.gs.mars.nexts_gs_mars_field_service.models.OutletKpi;

@Repository
public interface OutletKpiRepository extends JpaRepository<OutletKpi, Long> {

  @Query("SELECT kpi FROM OutletKpi kpi WHERE kpi.outlet.id = :outletId")
  List<OutletKpi> findByOutletId(Long outletId);
}
