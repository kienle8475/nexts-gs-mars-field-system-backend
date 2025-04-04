package com.nexts.gs.mars.nexts_gs_mars_field_service.models;

import java.util.List;
import java.util.Map;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "oos_report")
@Getter
@Setter
@NoArgsConstructor
public class OosReport extends BaseReport {

  @Builder
  public OosReport(StaffAttendance attendance, List<Map<String, Object>> data) {
    super(null, attendance, data, null, null);
  }
}