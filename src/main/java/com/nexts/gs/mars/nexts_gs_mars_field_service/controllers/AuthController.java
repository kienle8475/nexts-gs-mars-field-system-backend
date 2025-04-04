package com.nexts.gs.mars.nexts_gs_mars_field_service.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import com.nexts.gs.mars.nexts_gs_mars_field_service.utils.JwtUtil;
import com.nexts.gs.mars.nexts_gs_mars_field_service.dto.request.AuthRequest;
import com.nexts.gs.mars.nexts_gs_mars_field_service.dto.request.RegisterRequest;
import com.nexts.gs.mars.nexts_gs_mars_field_service.dto.response.AuthResponse;
import com.nexts.gs.mars.nexts_gs_mars_field_service.models.User;
import com.nexts.gs.mars.nexts_gs_mars_field_service.repositories.UserRepository;
import com.nexts.gs.mars.nexts_gs_mars_field_service.security.UserSecurityService;
import com.nexts.gs.mars.nexts_gs_mars_field_service.repositories.StaffProfileRepository;
import com.nexts.gs.mars.nexts_gs_mars_field_service.repositories.SaleProfileRepository;
import com.nexts.gs.mars.nexts_gs_mars_field_service.enums.Role;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/auth")
@RequiredArgsConstructor
public class AuthController {

  private final AuthenticationManager authenticationManager;
  private final JwtUtil jwtUtil;
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final StaffProfileRepository staffProfileRepository;
  private final SaleProfileRepository saleProfileRepository;
  private final UserSecurityService userSecurityService;

  @PostMapping("/login")
  public ResponseEntity<?> login(@RequestBody AuthRequest request) {
    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

    User user = userRepository.findByUsername(request.getUsername())
        .orElseThrow(() -> new UsernameNotFoundException("User not found"));

    String token = jwtUtil.generateToken(user);

    Object profile = null;

    switch (user.getRole()) {
      case PS -> {
        profile = staffProfileRepository.findByAccountId(user.getId())
            .orElseThrow(() -> new RuntimeException("Staff profile not found"));
      }
      case SALE -> {
        profile = saleProfileRepository.findByAccountId(user.getId())
            .orElseThrow(() -> new RuntimeException("Sales profile not found"));
      }
      default -> {
        User admin = User.builder()
            .id(0L)
            .username("admin")
            .passwordHash("")
            .role(Role.ADMIN)
            .createdAt(LocalDateTime.now())
            .build();
        profile = admin;
      }
    }

    return ResponseEntity.ok(AuthResponse.builder()
        .token(token)
        .profile(profile)
        .build());
  }

  @PostMapping("/register")
  public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
    if (userRepository.findByUsername(request.getUsername()).isPresent()) {
      return ResponseEntity.badRequest().body("Username already exists");
    }

    User newUser = User.builder()
        .username(request.getUsername())
        .passwordHash(passwordEncoder.encode(request.getPassword()))
        .role(Role.valueOf(request.getRole().toUpperCase()))
        .createdAt(LocalDateTime.now())
        .build();

    userRepository.save(newUser);
    return ResponseEntity.ok("User registered successfully");
  }

  @GetMapping("/verify")
  public ResponseEntity<?> validateToken(@RequestHeader("Authorization") String authHeader) {
    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
      return ResponseEntity.badRequest().body("Missing or invalid Authorization header");
    }

    String token = authHeader.substring(7);
    String username;

    try {
      username = jwtUtil.extractUsername(token);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
    }

    UserDetails userDetails = userSecurityService.loadUserByUsername(username);

    if (!jwtUtil.validateToken(token, userDetails)) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token expired or invalid");
    }

    // Optional: trả thêm thông tin người dùng
    Map<String, Object> response = Map.of(
        "username", username,
        "authorities", userDetails.getAuthorities());

    return ResponseEntity.ok(response);
  }
}
