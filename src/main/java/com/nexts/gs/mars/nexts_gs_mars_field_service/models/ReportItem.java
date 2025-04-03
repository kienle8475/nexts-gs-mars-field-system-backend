package com.nexts.gs.mars.nexts_gs_mars_field_service.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.OneToMany;
import jakarta.persistence.CascadeType;

import java.util.List;
import java.util.ArrayList;

@Entity
@Table(name = "report_items")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReportItem {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;

  @Column(name = "sku_code")
  private String skuCode;

  private String unit;

  private String description;

  @Builder.Default
  @OneToMany(mappedBy = "item", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<ReportItemUsage> usages = new ArrayList<>();
}
