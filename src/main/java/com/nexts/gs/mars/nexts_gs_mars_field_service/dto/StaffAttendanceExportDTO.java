package com.nexts.gs.mars.nexts_gs_mars_field_service.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record StaffAttendanceExportDTO(
    int index,
    String profileImageUrl,
    LocalDate date,
    String shiftName,
    LocalDateTime shiftStart,
    LocalDateTime shiftEnd,
    String outletName,
    String outletAddress,
    String checkinImage,
    String checkoutImage,
    LocalDateTime checkinTime,
    LocalDateTime checkoutTime,
    String staffCode,
    String fullName,
    LocalDate trainingDate,
    LocalDate startDate,
    LocalDate passProbationDate) {
}