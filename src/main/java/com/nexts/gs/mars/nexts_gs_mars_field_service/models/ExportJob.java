package com.nexts.gs.mars.nexts_gs_mars_field_service.models;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "export_jobs")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class ExportJob {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String filename;
  private String filepath;
  private String status;

  @Column(name = "created_at")
  private LocalDateTime createdAt;
}
