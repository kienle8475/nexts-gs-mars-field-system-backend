package com.nexts.gs.mars.nexts_gs_mars_field_service.services;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.apache.poi.sl.usermodel.PictureData;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFPictureData;
import org.apache.poi.xslf.usermodel.XSLFPictureShape;
import org.apache.poi.xslf.usermodel.XSLFShape;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.apache.poi.xslf.usermodel.XSLFSlideLayout;
import org.apache.poi.xslf.usermodel.XSLFSlideMaster;
import org.apache.poi.xslf.usermodel.XSLFTextShape;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.nexts.gs.mars.nexts_gs_mars_field_service.models.StaffAttendance;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ExportPPTXAsyncService {
  private final ExportJobService jobService;
  private final SimpMessagingTemplate messagingTemplate;

  @Async
  public void exportAttendancePPTX(Long jobId, List<StaffAttendance> attendances, InputStream templateInputStream) {
    try {
      ByteArrayInputStream pptStream = export(attendances, templateInputStream);

      String filename = "attendance_report_" + jobId + ".pptx";
      Path filepath = Paths.get("exports").resolve(filename);
      Files.createDirectories(filepath.getParent());

      Files.copy(pptStream, filepath, StandardCopyOption.REPLACE_EXISTING);

      jobService.updateJobDone(jobId, filename, filepath.toString());
      messagingTemplate.convertAndSend("/topic/export-status/" + jobId, "DONE");

    } catch (Exception e) {
      jobService.markFailed(jobId);
      messagingTemplate.convertAndSend("/topic/export-status/" + jobId, "FAILED");
    }
  }

  public ByteArrayInputStream export(List<StaffAttendance> attendances, InputStream templateInputStream) {
    try (XMLSlideShow ppt = new XMLSlideShow(templateInputStream);
        ByteArrayOutputStream out = new ByteArrayOutputStream()) {

      XSLFSlideMaster master = ppt.getSlideMasters().getFirst();
      XSLFSlideLayout layout = master.getLayout("title_and_content");

      for (StaffAttendance a : attendances) {
        XSLFSlide slide = ppt.createSlide(layout);

        List<XSLFShape> shapes = new ArrayList<>(slide.getShapes());
        for (XSLFShape shape : shapes) {
          if (shape == null || shape.getShapeName() == null)
            continue;

          String name = shape.getShapeName();
          switch (name) {
            case "date" -> setText(shape, a.getCheckinTime().toLocalDate().toString());
            case "shift" -> setText(shape, a.getShift().getName());
            case "outlet" -> setText(shape, a.getShift().getOutlet().getName());
            case "checkinTime" -> setText(shape, a.getCheckinTime() != null ? formatTime(a.getCheckinTime()) : "-");
            case "checkoutTime" -> setText(shape, a.getCheckoutTime() != null ? formatTime(a.getCheckoutTime()) : "-");
            case "profileImage" -> insertImage(slide, shape, a.getStaff().getProfileImage(), ppt);
            case "checkinImage" -> insertImage(slide, shape, a.getCheckinImage(), ppt);
            case "checkoutImage" -> insertImage(slide, shape, a.getCheckoutImage(), ppt);
          }
        }
      }
      ppt.write(out);
      return new ByteArrayInputStream(out.toByteArray());
    } catch (Exception e) {
      throw new RuntimeException("Failed to export PPTX", e);
    }
  }

  private void setText(XSLFShape shape, String text) {
    if (shape instanceof XSLFTextShape textShape) {
      textShape.clearText();
      textShape.setText(text != null ? text : "");
    }
  }

  private void insertImage(XSLFSlide slide, XSLFShape shape, String imagePath, XMLSlideShow ppt) {
    try {
      if (imagePath == null || imagePath.isBlank())
        return;

      imagePath = imagePath.replace("\\", "/");
      if (imagePath.startsWith("/")) {
        imagePath = imagePath.substring(1);
      }

      Path fullPath = Paths.get(System.getProperty("user.dir"), imagePath);

      if (!Files.exists(fullPath)) {
        System.err.println("Image not found: " + fullPath);
        return;
      }

      byte[] pictureData = Files.readAllBytes(fullPath);
      PictureData.PictureType type = PictureData.PictureType.JPEG;
      XSLFPictureData pd = ppt.addPicture(pictureData, type);

      XSLFPictureShape pic = slide.createPicture(pd);
      pic.setAnchor(shape.getAnchor());
      slide.removeShape(shape);

    } catch (Exception e) {
      System.err.println("Failed to insert image: " + imagePath);
      if (e != null)
        e.printStackTrace();
    }
  }

  private String formatTime(java.time.LocalDateTime time) {
    return time != null ? time.toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm")) : "";
  }
}