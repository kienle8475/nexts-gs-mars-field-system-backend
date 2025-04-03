package com.nexts.gs.mars.nexts_gs_mars_field_service.configurations;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Configuration
@ConfigurationProperties(prefix = "file")
@Data
public class FileStorageProperties {
  private String uploadDir;
}
