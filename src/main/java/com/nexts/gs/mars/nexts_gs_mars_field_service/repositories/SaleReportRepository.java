package com.nexts.gs.mars.nexts_gs_mars_field_service.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import com.nexts.gs.mars.nexts_gs_mars_field_service.models.SaleReport;

@Repository
public interface SaleReportRepository extends JpaRepository<SaleReport, Long> {
  Optional<SaleReport> findByAttendanceId(Long attendanceId);
}
