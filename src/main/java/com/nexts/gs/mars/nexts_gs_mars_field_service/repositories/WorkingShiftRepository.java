package com.nexts.gs.mars.nexts_gs_mars_field_service.repositories;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nexts.gs.mars.nexts_gs_mars_field_service.models.WorkingShift;

public interface WorkingShiftRepository extends JpaRepository<WorkingShift, Long> {

  List<WorkingShift> findByOutletId(Long outletId);

  List<WorkingShift> findByOutletIdAndStartTimeBetween(Long outletId, LocalDateTime start, LocalDateTime end);
}
