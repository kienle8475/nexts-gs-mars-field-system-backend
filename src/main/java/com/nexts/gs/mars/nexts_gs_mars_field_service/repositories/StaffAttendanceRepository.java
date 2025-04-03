package com.nexts.gs.mars.nexts_gs_mars_field_service.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

import com.nexts.gs.mars.nexts_gs_mars_field_service.models.StaffAttendance;

public interface StaffAttendanceRepository extends JpaRepository<StaffAttendance, Long> {
  boolean existsByShiftIdAndStaffId(Long shiftId, Long staffId);

  int countByShiftId(Long shiftId);

  Optional<StaffAttendance> findByShiftIdAndStaffId(Long id,
      Long staffId);

  List<StaffAttendance> findByStaffIdAndCheckoutTimeIsNull(Long staffId);

  Optional<StaffAttendance> findTopByStaffIdAndCheckoutTimeIsNullOrderByCheckinTimeDesc(Long staffId);
}
