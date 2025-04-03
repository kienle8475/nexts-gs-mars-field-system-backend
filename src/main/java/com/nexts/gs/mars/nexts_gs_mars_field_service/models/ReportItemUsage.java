package com.nexts.gs.mars.nexts_gs_mars_field_service.models;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;

import com.nexts.gs.mars.nexts_gs_mars_field_service.enums.ReportType;

@Entity
@Table(name = "report_item_usages")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReportItemUsage {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "item_id")
  private ReportItem item;

  @Column(name = "report_type")
  @Enumerated(EnumType.STRING)
  private ReportType reportType;
}
