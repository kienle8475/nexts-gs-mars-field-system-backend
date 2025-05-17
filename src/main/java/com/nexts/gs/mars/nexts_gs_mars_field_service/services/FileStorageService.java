package com.nexts.gs.mars.nexts_gs_mars_field_service.services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import javax.imageio.ImageIO;

import java.time.LocalDate;
import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Color;
import java.awt.Font;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.nexts.gs.mars.nexts_gs_mars_field_service.configurations.FileStorageProperties;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FileStorageService {
  private final FileStorageProperties fileStorageProperties;

  public String storeFile(MultipartFile file, String location, boolean isResize, int width,
      int height) {
    String fileName = UUID.randomUUID().toString() + ".jpg";
    Path targetLocation = Paths.get(fileStorageProperties.getUploadDir(), location).toAbsolutePath().normalize();
    try {
      Files.createDirectories(targetLocation);
      Path filePath = targetLocation.resolve(fileName);

      if (isResize) {
        BufferedImage originalImage = ImageIO.read(file.getInputStream());
        BufferedImage resizedImage = resizeAndCropToFill(originalImage, width, height);
        ImageIO.write(resizedImage, "jpg", filePath.toFile());
      } else {
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
      }
      return "/uploads/images/" + location + "/" + fileName;
    } catch (IOException e) {
      throw new RuntimeException("Could not store file " + fileName + ". Please try again!", e);
    }
  }

  public String storeAvatarImage(MultipartFile file) {
    return storeFile(file, "avatars", false, 0, 0);
  }

  public String storeProfileImageReport(MultipartFile file) {
    return storeFile(file, "profiles", true, 1200, 1600);
  }

  public String storeFileAttendance(MultipartFile file, LocalDate date, String outletCode, Long shiftId,
      String actionType, String overlayText) {
    try {
      String fileName = actionType + "_" + UUID.randomUUID() + ".jpg";
      Path uploadDir = Paths.get(fileStorageProperties.getUploadDir(),
          date.toString(),
          outletCode,
          String.format("%05d", shiftId)).toAbsolutePath().normalize();

      Files.createDirectories(uploadDir);
      Path targetPath = uploadDir.resolve(fileName);

      // Đọc ảnh gốc
      BufferedImage originalImage = ImageIO.read(file.getInputStream());

      // Resize ảnh
      BufferedImage resizedImage = resizeAndCropToFill(originalImage, 1200, 1600);

      // Ghi chữ lên ảnh
      Graphics2D g = resizedImage.createGraphics();
      g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

      String[] lines = overlayText.split("\\n");
      int padding = 20;
      int lineHeight = 32;
      int blockHeight = (lineHeight * lines.length) + (padding * 2);
      int blockWidth = resizedImage.getWidth();

      int yStart = resizedImage.getHeight() - blockHeight;

      // Nền mờ
      g.setColor(new Color(0, 0, 0, 150));
      g.fillRect(0, yStart, blockWidth, blockHeight);

      // Ghi từng dòng text
      g.setColor(Color.WHITE);
      g.setFont(new Font("Default", Font.PLAIN, 32));

      int yText = yStart + padding + lineHeight - 5;
      for (String line : lines) {
        g.drawString(line, padding, yText);
        yText += lineHeight;
      }
      g.dispose();

      // Ghi ảnh ra file
      ImageIO.write(resizedImage, "jpg", targetPath.toFile());

      // Trả URL tương đối để lưu DB
      return "/uploads/images/" + date + "/" + outletCode + "/" + String.format("%05d", shiftId) + "/" + fileName;

    } catch (IOException e) {
      throw new RuntimeException("Failed to store file", e);
    }
  }

  public BufferedImage resizeAndCropToFill(BufferedImage originalImage, int targetWidth, int targetHeight) {
    if (originalImage == null) {
      throw new IllegalArgumentException("Original image is null.");
    }

    int originalWidth = originalImage.getWidth();
    int originalHeight = originalImage.getHeight();

    if (originalWidth <= 0 || originalHeight <= 0) {
      throw new IllegalArgumentException("Original image has invalid dimensions.");
    }

    if (targetWidth <= 0 || targetHeight <= 0) {
      throw new IllegalArgumentException("Target dimensions must be positive.");
    }

    double scale = Math.max((double) targetWidth / originalWidth, (double) targetHeight / originalHeight);
    int scaledWidth = (int) Math.ceil(originalWidth * scale);
    int scaledHeight = (int) Math.ceil(originalHeight * scale);

    int imageType = originalImage.getColorModel().hasAlpha() ? BufferedImage.TYPE_INT_ARGB : BufferedImage.TYPE_INT_RGB;
    BufferedImage resizedImage = new BufferedImage(scaledWidth, scaledHeight, imageType);

    Graphics2D g2d = resizedImage.createGraphics();
    g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
    g2d.drawImage(originalImage, 0, 0, scaledWidth, scaledHeight, null);
    g2d.dispose();

    int x = Math.max((scaledWidth - targetWidth) / 2, 0);
    int y = Math.max((scaledHeight - targetHeight) / 2, 0);

    if (x + targetWidth > resizedImage.getWidth() || y + targetHeight > resizedImage.getHeight()) {
      throw new IllegalArgumentException("Crop area is out of image bounds.");
    }
    return resizedImage.getSubimage(x, y, targetWidth, targetHeight);
  }

}
