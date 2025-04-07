package com.nexts.gs.mars.nexts_gs_mars_field_service.repositories;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.nexts.gs.mars.nexts_gs_mars_field_service.models.StaffLeave;

@Repository
public interface StaffLeaveRepository extends JpaRepository<StaffLeave, Long> {
  List<StaffLeave> findByAttendance_Shift_Id(Long shiftId);

  @Query("""
          SELECT l FROM StaffLeave l
          WHERE (l.attendance.shift.outlet.saleRep.id = :saleProfileId
              OR l.attendance.shift.outlet.saleSupervisor.id = :saleProfileId
              OR l.attendance.shift.outlet.keyAccountManager.id = :saleProfileId)
            AND l.startTime BETWEEN :fromDate AND :toDate
          ORDER BY l.startTime DESC
      """)
  List<StaffLeave> findAllBySaleProfileIdAndDateBetween(Long saleProfileId, LocalDateTime fromDate,
      LocalDateTime toDate);
}
