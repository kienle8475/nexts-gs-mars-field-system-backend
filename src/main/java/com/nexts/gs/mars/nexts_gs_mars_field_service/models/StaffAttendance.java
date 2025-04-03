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
import org.hibernate.annotations.Type;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "staff_attendance")
public class StaffAttendance {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "staff_id", nullable = false)
  private StaffProfile staff;

  @ManyToOne
  @JoinColumn(name = "shift_id", nullable = false)
  private WorkingShift shift;

  @Column(name = "checkin_time")
  private LocalDateTime checkinTime;

  @Column(name = "checkout_time")
  private LocalDateTime checkoutTime;

  @Column(name = "checkin_image", columnDefinition = "text")
  private String checkinImage;

  @Column(name = "checkout_image", columnDefinition = "text")
  private String checkoutImage;

  @Type(value = JsonBinaryType.class)
  @Column(name = "checkin_location", columnDefinition = "jsonb")
  private GeoLocation checkinLocation;

  @Type(value = JsonBinaryType.class)
  @Column(name = "checkout_location", columnDefinition = "jsonb")
  private GeoLocation checkoutLocation;
}
