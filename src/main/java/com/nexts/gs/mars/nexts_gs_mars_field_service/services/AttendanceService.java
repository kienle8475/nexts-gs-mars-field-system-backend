package com.nexts.gs.mars.nexts_gs_mars_field_service.services;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import com.nexts.gs.mars.nexts_gs_mars_field_service.dto.request.CheckInRequest;
import com.nexts.gs.mars.nexts_gs_mars_field_service.dto.request.CheckOutRequest;
import com.nexts.gs.mars.nexts_gs_mars_field_service.exceptions.NotFoundException;
import com.nexts.gs.mars.nexts_gs_mars_field_service.models.Outlet;
import com.nexts.gs.mars.nexts_gs_mars_field_service.models.StaffAttendance;
import com.nexts.gs.mars.nexts_gs_mars_field_service.models.StaffProfile;
import com.nexts.gs.mars.nexts_gs_mars_field_service.models.User;
import com.nexts.gs.mars.nexts_gs_mars_field_service.models.WorkingShift;
import com.nexts.gs.mars.nexts_gs_mars_field_service.repositories.StaffAttendanceRepository;
import com.nexts.gs.mars.nexts_gs_mars_field_service.repositories.StaffProfileRepository;
import com.nexts.gs.mars.nexts_gs_mars_field_service.repositories.UserRepository;
import com.nexts.gs.mars.nexts_gs_mars_field_service.repositories.WorkingShiftRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AttendanceService {
  private final StaffAttendanceRepository staffAttendanceRepository;
  private final StaffProfileRepository staffProfileRepository;
  private final WorkingShiftRepository workingShiftRepository;
  private final FileStorageService fileStorageService;
  private final UserRepository userRepository;

  public StaffAttendance checkIn(CheckInRequest req, MultipartFile file) {
    if (staffAttendanceRepository.findByShiftIdAndStaffId(req.getShiftId(), req.getStaffId()).isPresent()) {
      throw new IllegalStateException("Already checked in for this shift");
    }

    WorkingShift shift = workingShiftRepository.findById(req.getShiftId())
        .orElseThrow(() -> new NotFoundException("Shift not found"));

    StaffProfile staff = staffProfileRepository.findById(req.getStaffId())
        .orElseThrow(() -> new NotFoundException("Staff not found"));

    Outlet outlet = shift.getOutlet();

    String overlayText = String.join("\n",
        "Loại: Check In",
        "Thời gian: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")),
        "Nhân viên: " + staff.getFullName(),
        "Ca: " + shift.getName(),
        "Địa điểm: " + outlet.getName(),
        "Vị trí: " + req.getLocation());

    String imageUrl = fileStorageService.storeFileAttendance(
        file,
        LocalDate.now(),
        outlet.getCode(),
        shift.getId(),
        "checkin",
        overlayText);

    StaffAttendance attendance = StaffAttendance.builder()
        .staff(staff)
        .shift(shift)
        .checkinTime(LocalDateTime.now())
        .checkinLocation(req.getLocation())
        .checkinImage(imageUrl)
        .build();

    return staffAttendanceRepository.save(attendance);
  }

  public void checkOut(CheckOutRequest req, MultipartFile file) {
    StaffAttendance attendance = staffAttendanceRepository
        .findByShiftIdAndStaffId(req.getShiftId(), req.getStaffId())
        .orElseThrow(() -> new NotFoundException("You haven't checked in yet"));

    if (attendance.getCheckoutTime() != null) {
      throw new IllegalStateException("Already checked out");
    }

    WorkingShift shift = attendance.getShift();
    Outlet outlet = shift.getOutlet();
    String overlayText = String.join("\n",
        "Loại: Check Out",
        "Thời gian: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")),
        "Nhân viên: " + attendance.getStaff().getFullName(),
        "Ca: " + shift.getName(),
        "Địa điểm: " + outlet.getName(),
        "Vị trí: " + req.getLocation());

    String imageUrl = fileStorageService.storeFileAttendance(
        file,
        LocalDate.now(),
        outlet.getCode(),
        shift.getId(),
        "checkout",
        overlayText);

    attendance.setCheckoutTime(LocalDateTime.now());
    attendance.setCheckoutImage(imageUrl);
    attendance.setCheckoutLocation(req.getLocation());

    staffAttendanceRepository.save(attendance);
  }

  public StaffAttendance getCurrentAttendance(Long staffId, Authentication authentication) {
    if (staffId == null) {
      if (authentication == null || !authentication.isAuthenticated()) {
        throw new IllegalStateException("Unauthorized");
      }

      String username = authentication.getName();
      User user = userRepository.findByUsername(username)
          .orElseThrow(() -> new NotFoundException("User not found"));
      Optional<StaffProfile> staff = staffProfileRepository.findByAccountId(user.getId());
      if (staff == null) {
        throw new IllegalStateException("No staff profile associated with this user");
      }
      staffId = staff.get().getId();
    }
    StaffAttendance current = staffAttendanceRepository
        .findTopByStaffIdAndCheckoutTimeIsNullOrderByCheckinTimeDesc(staffId)
        .orElseThrow(() -> new NotFoundException("No active attendance record found"));

    return current;
  }

  public void deleteAttendance(Long id) {
    StaffAttendance attendance = staffAttendanceRepository.findById(id)
        .orElseThrow(() -> new NotFoundException("Attendance not found"));

    staffAttendanceRepository.delete(attendance);
  }
}
