package com.nexts.gs.mars.nexts_gs_mars_field_service.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nexts.gs.mars.nexts_gs_mars_field_service.models.StaffLeave;

@Repository
public interface StaffLeaveRepository extends JpaRepository<StaffLeave, Long> {
  List<StaffLeave> findByAttendance_Shift_Id(Long shiftId);
}
