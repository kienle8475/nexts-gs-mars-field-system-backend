package com.nexts.gs.mars.nexts_gs_mars_field_service.enums;

public enum LeaveType {
  LUNCH_BREAK("LUNCH_BREAK", "Đi ăn trưa/tối"),
  RESTROOM("RESTROOM", "Đi vệ sinh"),
  BREAK_TIME("BREAK_TIME", "Giải lao"),
  GET_SUPPLIES("GET_SUPPLIES", "Lấy hàng/vật dụng"),
  PRIVATE_TASK("PRIVATE_TASK", "Công việc riêng"),
  QUICK_MEETING("QUICK_MEETING", "Họp nhanh với quản lý"),
  PHONE_CALL("PHONE_CALL", "Nghe điện thoại khẩn"),
  OTHER_REASON("OTHER_REASON", "Lý do khác");

  private final String value;
  private final String label;

  LeaveType(String value, String label) {
    this.value = value;
    this.label = label;
  }

  public String getValue() {
    return value;
  }

  public String getLabel() {
    return label;
  }
}
