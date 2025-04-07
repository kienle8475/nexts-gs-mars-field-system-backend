package com.nexts.gs.mars.nexts_gs_mars_field_service.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "staff_profiles")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StaffProfile {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @OneToOne
  @JoinColumn(name = "account_id", nullable = false)
  private User account;

  @Column(name = "staff_code", nullable = false, unique = true, length = 20)
  private String staffCode;

  @Column(name = "full_name", length = 100)
  private String fullName;

  @Column(name = "profile_image", columnDefinition = "TEXT")
  private String profileImage;

  @Column(name = "profile_image_report", columnDefinition = "TEXT")
  private String profileImageReport;

  @Column(name = "training_date")
  private LocalDate trainingDate;

  @Column(name = "start_date")
  private LocalDate startDate;

  @Column(name = "pass_probation_date")
  private LocalDate passProbationDate;

  @Column(name = "updated_at")
  private LocalDateTime updatedAt;
}
