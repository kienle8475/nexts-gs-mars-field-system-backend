package com.nexts.gs.mars.nexts_gs_mars_field_service.utils;

import java.util.List;
import java.util.function.Function;

import org.springframework.data.domain.Page;

import com.nexts.gs.mars.nexts_gs_mars_field_service.dto.response.PageResponse;

public class PageResponseUtil {
  public static <T, U> PageResponse<U> toPageResponse(Page<T> page, Function<T, U> mapper) {
    List<U> content = page.getContent().stream().map(mapper).toList();

    return PageResponse.<U>builder()
        .content(content)
        .page(page.getNumber())
        .size(page.getSize())
        .totalElements(page.getTotalElements())
        .totalPages(page.getTotalPages())
        .last(page.isLast())
        .build();
  }

  public static <T> PageResponse<T> toPageResponse(Page<T> page) {
    return PageResponse.<T>builder()
        .content(page.getContent())
        .page(page.getNumber())
        .size(page.getSize())
        .totalElements(page.getTotalElements())
        .totalPages(page.getTotalPages())
        .last(page.isLast())
        .build();
  }
}