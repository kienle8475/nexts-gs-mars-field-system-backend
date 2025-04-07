package com.nexts.gs.mars.nexts_gs_mars_field_service.services;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.nexts.gs.mars.nexts_gs_mars_field_service.dto.request.RegisterStaffRequest;
import com.nexts.gs.mars.nexts_gs_mars_field_service.dto.request.StaffProfileUpdateRequest;
import com.nexts.gs.mars.nexts_gs_mars_field_service.dto.response.StaffSimpleOptionResponse;
import com.nexts.gs.mars.nexts_gs_mars_field_service.enums.Role;
import com.nexts.gs.mars.nexts_gs_mars_field_service.models.StaffProfile;
import com.nexts.gs.mars.nexts_gs_mars_field_service.models.User;
import com.nexts.gs.mars.nexts_gs_mars_field_service.repositories.StaffProfileRepository;
import com.nexts.gs.mars.nexts_gs_mars_field_service.repositories.UserRepository;
import jakarta.transaction.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class StaffProfileService {
  private final StaffProfileRepository staffProfileRepository;
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final FileStorageService fileStorageService;

  public List<StaffSimpleOptionResponse> getSimple() {
    return staffProfileRepository.findSimple();
  }

  public Page<StaffProfile> getAllStaff(Pageable pageable) {
    return staffProfileRepository.findAll(pageable);
  }

  @Transactional
  public StaffProfile registerStaff(RegisterStaffRequest request, MultipartFile[] files) {
    if (userRepository.existsByUsername(request.getUsername())) {
      throw new IllegalArgumentException("Username already exists");
    }
    User user = User.builder()
        .username(request.getUsername())
        .passwordHash(passwordEncoder.encode(request.getPassword()))
        .createdAt(LocalDateTime.now())
        .role(Role.PS)
        .build();
    userRepository.save(user);

    String profileImageUrl = null;
    String profileImageReportUrl = null;
    if (files != null && files.length > 0) {
      profileImageUrl = fileStorageService.storeAvatarImage(files[1]);
      profileImageReportUrl = fileStorageService.storeProfileImageReport(files[0]);
    }

    StaffProfile staffProfile = StaffProfile.builder()
        .account(user)
        .staffCode(request.getStaffCode())
        .fullName(request.getFullName())
        .profileImage(profileImageUrl)
        .profileImageReport(profileImageReportUrl)
        .trainingDate(request.getTrainingDate())
        .startDate(LocalDate.now())
        .passProbationDate(request.getPassProbationDate())
        .updatedAt(LocalDateTime.now())
        .build();
    return staffProfileRepository.save(staffProfile);
  }

  @Transactional
  public StaffProfile updateStaffProfile(Long id, StaffProfileUpdateRequest request, MultipartFile[] files) {
    StaffProfile profile = staffProfileRepository.findById(id)
        .orElseThrow(() -> new NoSuchElementException("StaffProfile not found"));
    String profileImageUrl = profile.getProfileImage();
    String profileImageReportUrl = profile.getProfileImageReport();
    if (files != null && files.length > 0) {
      profileImageUrl = fileStorageService.storeAvatarImage(files[0]);
      profileImageReportUrl = fileStorageService.storeProfileImageReport(files[1]);
    }

    if (request.getPassword() != null || request.getPassword().length() > 0) {
      profile.getAccount().setPasswordHash(passwordEncoder.encode(request.getPassword()));
    }
    profile.setStaffCode(request.getStaffCode());
    profile.setFullName(request.getFullName());
    profile.setProfileImage(profileImageUrl);
    profile.setProfileImageReport(profileImageReportUrl);
    profile.setTrainingDate(request.getTrainingDate());
    profile.setStartDate(request.getStartDate());
    profile.setPassProbationDate(request.getPassProbationDate());
    profile.setUpdatedAt(LocalDateTime.now());

    return staffProfileRepository.save(profile);
  }
}
