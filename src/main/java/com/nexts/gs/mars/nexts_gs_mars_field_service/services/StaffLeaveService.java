package com.nexts.gs.mars.nexts_gs_mars_field_service.services;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.nexts.gs.mars.nexts_gs_mars_field_service.dto.request.StaffLeaveRequest;
import com.nexts.gs.mars.nexts_gs_mars_field_service.dto.response.StaffLeaveResponse;
import com.nexts.gs.mars.nexts_gs_mars_field_service.mapper.StaffLeaveMapper;
import com.nexts.gs.mars.nexts_gs_mars_field_service.models.StaffAttendance;
import com.nexts.gs.mars.nexts_gs_mars_field_service.models.StaffLeave;
import com.nexts.gs.mars.nexts_gs_mars_field_service.repositories.StaffAttendanceRepository;
import com.nexts.gs.mars.nexts_gs_mars_field_service.repositories.StaffLeaveRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StaffLeaveService {
  private final StaffLeaveRepository staffLeaveRepository;
  private final StaffAttendanceRepository staffAttendanceRepository;
  private final StaffLeaveMapper mapper;

  public StaffLeaveResponse create(StaffLeaveRequest request) {
    StaffAttendance attendance = staffAttendanceRepository.findById(request.getAttendanceId())
        .orElseThrow(() -> new RuntimeException("Attendance not found"));
    StaffLeave leave = new StaffLeave();
    leave.setAttendance(attendance);
    leave.setLeaveType(request.getLeaveType());
    leave.setStartTime(LocalDateTime.now());
    leave.setNote(request.getNote());
    return mapper.toResponse(staffLeaveRepository.save(leave));
  }

  public StaffLeaveResponse update(Long id) {
    StaffLeave leave = staffLeaveRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Leave not found"));
    leave.setEndTime(LocalDateTime.now());
    return mapper.toResponse(staffLeaveRepository.save(leave));
  }

  public List<StaffLeaveResponse> getByAttendanceId(Long shiftId) {
    List<StaffLeave> leaves = staffLeaveRepository.findByAttendance_Shift_Id(shiftId);
    return leaves.stream().map(mapper::toResponse).toList();
  }

}
