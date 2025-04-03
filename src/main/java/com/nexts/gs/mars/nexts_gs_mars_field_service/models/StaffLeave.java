package com.nexts.gs.mars.nexts_gs_mars_field_service.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Column;
import java.time.LocalDateTime;

@Entity
@Table(name = "staff_leaves")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StaffLeave {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  // Mỗi kỳ nghỉ gắn với một bản ghi chấm công
  @ManyToOne
  @JoinColumn(name = "attendance_id", nullable = false)
  private StaffAttendance attendance;

  @Column(name = "leave_type", length = 50)
  private String leaveType;

  @Column(name = "start_time")
  private LocalDateTime startTime;

  @Column(name = "end_time")
  private LocalDateTime endTime;

  @Column(name = "note", columnDefinition = "TEXT")
  private String note;
}
