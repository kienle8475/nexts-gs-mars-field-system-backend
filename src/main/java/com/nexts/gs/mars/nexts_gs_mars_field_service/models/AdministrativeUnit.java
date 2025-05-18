package com.nexts.gs.mars.nexts_gs_mars_field_service.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "administrative_units", uniqueConstraints = @UniqueConstraint(columnNames = { "name", "type" }))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class AdministrativeUnit {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, length = 100)
  private String name;

  @Column(nullable = false, length = 20)
  private String type; // "province", "district", "ward"

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "parent_id")
  @JsonIgnore
  private AdministrativeUnit parent;

  @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
  @JsonManagedReference
  private List<AdministrativeUnit> children;
}
