package com.nexts.gs.mars.nexts_gs_mars_field_service.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nexts.gs.mars.nexts_gs_mars_field_service.models.SamplingReport;

@Repository
public interface SamplingReportRepository extends JpaRepository<SamplingReport, Long> {
  Optional<SamplingReport> findByAttendanceId(Long attendanceId);
}
