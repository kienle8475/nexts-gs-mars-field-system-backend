package com.nexts.gs.mars.nexts_gs_mars_field_service.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.nexts.gs.mars.nexts_gs_mars_field_service.dto.response.StaffSimpleOptionResponse;
import com.nexts.gs.mars.nexts_gs_mars_field_service.models.StaffProfile;

public interface StaffProfileRepository extends JpaRepository<StaffProfile, Long> {

  Optional<StaffProfile> findByAccountId(Long id);

  @Query("""
      SELECT new com.nexts.gs.mars.nexts_gs_mars_field_service.dto.response.StaffSimpleOptionResponse(s.id, CONCAT(s.fullName, ' - ', s.staffCode))
      FROM StaffProfile s
      """)
  List<StaffSimpleOptionResponse> findSimple();
}
