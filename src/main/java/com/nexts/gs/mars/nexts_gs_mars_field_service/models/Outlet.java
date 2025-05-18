package com.nexts.gs.mars.nexts_gs_mars_field_service.models;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.OneToMany;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "outlets")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class Outlet {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "code", nullable = false, unique = true, length = 50)
  private String code;

  @Column(name = "name", nullable = false, length = 100)
  private String name;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "admin_unit_id")
  private AdministrativeUnit administrativeUnit;

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

  @OneToMany(mappedBy = "outlet", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
  private List<OutletKpi> kpis = new ArrayList<>();

  @OneToOne(mappedBy = "outlet", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
  private OutletWorkingTime workingTime;
}
