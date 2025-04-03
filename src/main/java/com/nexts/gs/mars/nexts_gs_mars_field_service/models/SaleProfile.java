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
import jakarta.persistence.OneToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import com.nexts.gs.mars.nexts_gs_mars_field_service.enums.SaleRole;

@Entity
@Table(name = "sales_profiles")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SaleProfile {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  // Liên kết đến tài khoản người dùng
  @OneToOne
  @JoinColumn(name = "account_id", nullable = false)
  private User account;

  @Column(name = "full_name", length = 100)
  private String fullName;

  @Column(name = "role", nullable = false, length = 20)
  @Enumerated(EnumType.STRING)
  private SaleRole role;

}
