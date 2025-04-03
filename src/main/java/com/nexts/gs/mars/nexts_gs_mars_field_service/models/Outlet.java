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
import jakarta.persistence.Column;
import java.time.LocalDateTime;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;

@Entity
@Table(name = "outlets")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Outlet {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "code", nullable = false, unique = true, length = 50)
  private String code;

  @Column(name = "name", nullable = false, length = 100)
  private String name;

  @ManyToOne
  @JoinColumn(name = "province_id")
  private Province province;

  @Column(name = "address", columnDefinition = "TEXT")
  private String address;

  // Liên kết với các profile bán hàng
  @ManyToOne
  @JoinColumn(name = "sale_rep_id")
  private SaleProfile saleRep;

  @ManyToOne
  @JoinColumn(name = "sale_sup_id")
  private SaleProfile saleSupervisor;

  @ManyToOne
  @JoinColumn(name = "kam_id")
  private SaleProfile keyAccountManager;

  @Column(name = "latitude")
  private Double latitude;

  @Column(name = "longitude")
  private Double longitude;

  @Column(name = "checkin_radius_meters")
  private Integer checkinRadiusMeters;

  @Column(name = "created_at")
  private LocalDateTime createdAt;

  @Column(name = "updated_at")
  private LocalDateTime updatedAt;
}
