package com.nexts.gs.mars.nexts_gs_mars_field_service.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
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

  @Query("SELECT sa FROM StaffAttendance sa " +
      "JOIN FETCH sa.shift s " +
      "LEFT JOIN FETCH sa.saleReport " +
      "LEFT JOIN FETCH sa.oosReport " +
      "LEFT JOIN FETCH sa.samplingReport " +
      "WHERE sa.staff.id = :staffId AND s.id = :shiftId")
  Optional<StaffAttendance> findByStaffAndShift(@Param("staffId") Long staffId,
      @Param("shiftId") Long shiftId);

  List<StaffAttendance> findAllByShiftId(Long shiftId);

  @Query("""
          SELECT a FROM StaffAttendance a
          WHERE (a.shift.outlet.saleRep.id = :saleProfileId
                 OR a.shift.outlet.saleSupervisor.id = :saleProfileId
                 OR a.shift.outlet.keyAccountManager.id = :saleProfileId)
            AND a.checkinTime BETWEEN :startDateTime AND :endDateTime
      """)
  List<StaffAttendance> findAllBySaleProfileIdAndCheckinTimeBetween(Long saleProfileId, LocalDateTime startDateTime,
      LocalDateTime endDateTime);

}
