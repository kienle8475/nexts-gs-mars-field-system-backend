package com.nexts.gs.mars.nexts_gs_mars_field_service.exceptions;

import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.RequestDispatcher;
import com.nexts.gs.mars.nexts_gs_mars_field_service.dto.response.ApiResponse;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
  private final MessageSource messageSource;

  public GlobalExceptionHandler(MessageSource messageSource) {
    this.messageSource = messageSource;
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<ApiResponse<Object>> handleBadRequest(
      IllegalArgumentException ex, WebRequest request, Locale locale) {

    log.warn("Bad request: {}", ex.getMessage());

    String message = messageSource.getMessage("error.bad_request", null, ex.getMessage(), locale);
    return buildResponse(HttpStatus.BAD_REQUEST, message);
  }

  @ExceptionHandler(EntityNotFoundException.class)
  public ResponseEntity<ApiResponse<Object>> handleNotFoundException(
      EntityNotFoundException ex, WebRequest request, Locale locale) {

    log.warn("Resource not found: {}", ex.getMessage());

    String message = messageSource.getMessage("error.not_found", new Object[] { ex.getMessage() }, locale);
    return buildResponse(HttpStatus.NOT_FOUND, message);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ApiResponse<Object>> handleException(
      Exception ex, HttpServletRequest request, Locale locale) {
    // Lấy status gốc từ request attributes
    Object statusObj = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
    int statusCode = statusObj != null ? Integer.parseInt(statusObj.toString()) : 500;

    // Nếu là lỗi 404 (static resource hoặc path sai)
    if (statusCode == HttpStatus.NOT_FOUND.value()) {
      String path = (String) request.getAttribute(RequestDispatcher.ERROR_REQUEST_URI);
      String message = messageSource.getMessage("error.not_found", new Object[] { path }, locale);
      return buildResponse(HttpStatus.NOT_FOUND, message);
    }

    // Lỗi khác
    String message = messageSource.getMessage("error.internal", new Object[] { ex.getMessage() }, locale);
    return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, message);

  }

  @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
  public ResponseEntity<ApiResponse<Object>> handleMethodNotSupported(HttpRequestMethodNotSupportedException ex,
      Locale locale) {

    log.warn("Method not allowed: {}", ex.getMessage());
    String message = messageSource.getMessage("error.method_not_allowed", new Object[] { ex.getMessage() }, locale);
    return buildResponse(HttpStatus.METHOD_NOT_ALLOWED, message);
  }

  @ExceptionHandler(NoHandlerFoundException.class)
  public ResponseEntity<ApiResponse<Object>> handleNoHandlerFound(NoHandlerFoundException ex,
      Locale locale) {

    log.warn("No handler found: {}", ex.getRequestURL());
    String message = messageSource.getMessage("error.not_found", new Object[] { ex.getRequestURL() }, locale);
    return buildResponse(HttpStatus.NOT_FOUND, message);
  }

  private ResponseEntity<ApiResponse<Object>> buildResponse(HttpStatus status, String message) {
    ApiResponse<Object> response = ApiResponse.builder()
        .message(message)
        .status(status.value())
        .data(null)
        .build();

    return ResponseEntity.status(status).body(response);
  }

}
