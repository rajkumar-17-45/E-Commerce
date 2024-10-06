package com.ecommerce.site_project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ecommerce.site_project.service.VendorFileUploadService;

import java.util.Map;

@RestController
public class VendorFileUploadController {

    @Autowired
    private VendorFileUploadService vendorFileUploadService;

    @PostMapping("/upload/vendor")
    public ResponseEntity<?> uploadVendorCsv(@RequestParam("file") MultipartFile file) {
        try {
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("success", false, "message", "Please select a CSV file to upload."));
            }
            // Validate file extension
            String fileName = file.getOriginalFilename();
            if (fileName == null || !fileName.toLowerCase().endsWith(".csv")) {
                return ResponseEntity.badRequest().body(Map.of("success", false, "message", "Invalid file type. Please upload a CSV file."));
            }
            vendorFileUploadService.saveVendorsFromCsv(file);
            return ResponseEntity.ok(Map.of("success", true, "message", "Vendor CSV file successfully uploaded and processed."));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("success", false, "message", "An error occurred while processing the file."));
        }
    }
}
