package com.nexts.gs.mars.nexts_gs_mars_field_service.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nexts.gs.mars.nexts_gs_mars_field_service.models.OutletKpi;
import com.nexts.gs.mars.nexts_gs_mars_field_service.services.kpi.OutletKpiService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("${api.version}/outlet-kpis")
@RequiredArgsConstructor
public class OutletKpiController {
  private final OutletKpiService outletKpiService;

  @GetMapping("/{outletId}")
  public List<OutletKpi> getOutletKpis(@PathVariable Long outletId) {
    return outletKpiService.getOutletKpis(outletId);
  }

  @PostMapping
  public OutletKpi createOutletKpi(@RequestBody OutletKpi outletKpi) {
    return outletKpiService.createOutletKpi(outletKpi);
  }
}
