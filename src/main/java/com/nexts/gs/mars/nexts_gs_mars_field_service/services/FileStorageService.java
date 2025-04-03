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

  public String storeFile(MultipartFile file) {
    String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
    Path targetLocation = Paths.get(fileStorageProperties.getUploadDir()).toAbsolutePath().normalize();
    try {
      Files.createDirectories(targetLocation);
      Path filePath = targetLocation.resolve(fileName);
      Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
      return "/uploads/images/" + fileName;
    } catch (IOException e) {
      throw new RuntimeException("Could not store file " + fileName + ". Please try again!", e);
    }
  }

  public String storeFile(MultipartFile file, LocalDate date, String outletCode, Long shiftId) {
    try {
      String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
      Path uploadDir = Paths.get(fileStorageProperties.getUploadDir(),
          date.toString(),
          outletCode,
          "shift_" + shiftId).toAbsolutePath().normalize();

      Files.createDirectories(uploadDir);
      Path targetPath = uploadDir.resolve(fileName);

      // Đọc ảnh gốc
      BufferedImage originalImage = ImageIO.read(file.getInputStream());

      // Resize ảnh
      BufferedImage resizedImage = resizeWithPadding(originalImage, 1200, 1600);

      // Ghi chữ lên ảnh
      Graphics2D g = resizedImage.createGraphics();
      g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

      String overlayText = "NEXTS";
      String[] lines = overlayText.split("\\n");
      int padding = 10;
      int lineHeight = 28;
      int blockHeight = (lineHeight * lines.length) + (padding * 2);
      int blockWidth = resizedImage.getWidth();

      int yStart = resizedImage.getHeight() - blockHeight;

      // Nền mờ
      g.setColor(new Color(0, 0, 0, 150));
      g.fillRect(0, yStart, blockWidth, blockHeight);

      // Ghi từng dòng text
      g.setColor(Color.WHITE);
      g.setFont(new Font("Arial", Font.PLAIN, 22));

      int yText = yStart + padding + lineHeight - 5;
      for (String line : lines) {
        g.drawString(line, padding, yText);
        yText += lineHeight;
      }
      g.dispose();

      // Ghi ảnh ra file
      ImageIO.write(resizedImage, "jpg", targetPath.toFile());

      // Trả URL tương đối để lưu DB
      return "/uploads/images/" + date + "/" + outletCode + "/shift_" + shiftId + "/" + fileName;

    } catch (IOException e) {
      throw new RuntimeException("Failed to store file", e);
    }
  }

  private BufferedImage resizeWithPadding(BufferedImage originalImage, int targetWidth, int targetHeight) {
    int originalWidth = originalImage.getWidth();
    int originalHeight = originalImage.getHeight();

    double scale = Math.min((double) targetWidth / originalWidth, (double) targetHeight / originalHeight);
    int scaledWidth = (int) (originalWidth * scale);
    int scaledHeight = (int) (originalHeight * scale);

    BufferedImage outputImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
    Graphics2D g2d = outputImage.createGraphics();

    g2d.setColor(Color.BLACK);
    g2d.fillRect(0, 0, targetWidth, targetHeight);

    int x = (targetWidth - scaledWidth) / 2;
    int y = (targetHeight - scaledHeight) / 2;
    g2d.drawImage(originalImage, x, y, scaledWidth, scaledHeight, null);
    g2d.dispose();

    return outputImage;
  }
}
