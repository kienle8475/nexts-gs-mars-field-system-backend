package com.nexts.gs.mars.nexts_gs_mars_field_service.models;

import java.util.Map;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "sampling_report")
@Getter
@Setter
@NoArgsConstructor
public class SamplingReport extends BaseReport {

  @Builder
  public SamplingReport(StaffAttendance attendance, List<Map<String, Object>> data) {
    super(null, attendance, data, null, null);
  }
}
