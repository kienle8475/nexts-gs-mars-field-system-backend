package com.nexts.gs.mars.nexts_gs_mars_field_service.dto.response;

import java.time.LocalDateTime;

public record StaffLeaveSaleResponse(
    Long id,
    String staffName,
    String leaveType,
    LocalDateTime startTime,
    LocalDateTime endTime,
    String note,
    String outletName) {
}