package com.nexts.gs.mars.nexts_gs_mars_field_service.services.kpi;

import java.util.List;

import org.springframework.stereotype.Service;

import com.nexts.gs.mars.nexts_gs_mars_field_service.models.OutletKpi;
import com.nexts.gs.mars.nexts_gs_mars_field_service.repositories.OutletKpiRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OutletKpiService {
  private final OutletKpiRepository outletKpiRepository;

  public List<OutletKpi> getOutletKpis(Long outletId) {
    return outletKpiRepository.findByOutletId(outletId);
  }

  public OutletKpi createOutletKpi(OutletKpi outletKpi) {
    return outletKpiRepository.save(outletKpi);
  }
}
