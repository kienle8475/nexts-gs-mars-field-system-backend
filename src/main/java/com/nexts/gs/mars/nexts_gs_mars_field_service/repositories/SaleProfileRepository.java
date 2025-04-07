package com.nexts.gs.mars.nexts_gs_mars_field_service.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.nexts.gs.mars.nexts_gs_mars_field_service.dto.response.SaleSimpleOptionResponse;
import com.nexts.gs.mars.nexts_gs_mars_field_service.models.SaleProfile;

public interface SaleProfileRepository extends JpaRepository<SaleProfile, Long> {

  Optional<SaleProfile> findByAccountId(Long id);

  @Query("""
      SELECT new com.nexts.gs.mars.nexts_gs_mars_field_service.dto.response.SaleSimpleOptionResponse(s.id, CONCAT(s.fullName, ' - ', s.role))
      FROM SaleProfile s
      """)
  List<SaleSimpleOptionResponse> findSimple();
}
