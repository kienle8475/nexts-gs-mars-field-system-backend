package com.nexts.gs.mars.nexts_gs_mars_field_service.dto.response;

import java.time.LocalDateTime;

public record StaffLeaveAdminResponse(
    Long id,
    String staffName,
    String outletName,
    String shiftName,
    LocalDateTime startTime,
    LocalDateTime endTime,
    String leaveType,
    String note) {
}