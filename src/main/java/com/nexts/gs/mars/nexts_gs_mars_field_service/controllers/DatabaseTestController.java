package com.nexts.gs.mars.nexts_gs_mars_field_service.controllers;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("${api.version}/test-db")
@RequiredArgsConstructor
public class DatabaseTestController {
  private final DataSource dataSource;

  @GetMapping
  public String testConnection() {
    try (Connection conn = dataSource.getConnection()) {
      return "✅ Connected to: " + conn.getMetaData().getURL();
    } catch (SQLException e) {
      return "❌ Connection failed: " + e.getMessage();
    }
  }
}
