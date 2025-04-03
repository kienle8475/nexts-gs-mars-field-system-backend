package com.nexts.gs.mars.nexts_gs_mars_field_service.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

import com.nexts.gs.mars.nexts_gs_mars_field_service.enums.ReportType;
import com.nexts.gs.mars.nexts_gs_mars_field_service.models.ReportItem;

public interface ReportItemRepository extends JpaRepository<ReportItem, Long> {
  @Query("SELECT DISTINCT ri FROM ReportItem ri JOIN ri.usages u WHERE u.reportType = :type")
  List<ReportItem> findByReportType(@Param("type") ReportType reportType);
}
