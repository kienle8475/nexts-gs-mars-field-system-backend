package com.nexts.gs.mars.nexts_gs_mars_field_service.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nexts.gs.mars.nexts_gs_mars_field_service.models.StaffProfile;

public interface StaffProfileRepository extends JpaRepository<StaffProfile, Long> {

  Optional<StaffProfile> findByAccountId(Long id);

}
