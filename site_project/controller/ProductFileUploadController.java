package com.ecommerce.site_project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ecommerce.site_project.service.ProductFileUploadService;

import java.util.*;
@RestController
public class ProductFileUploadController {

    @Autowired
    private ProductFileUploadService productFileUploadService;

    @PostMapping("/upload/product")
    public ResponseEntity<?> uploadProductCsv(@RequestParam("file") MultipartFile file) {
        try {
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("success", false, "message", "Please select a CSV file to upload."));
            }

            productFileUploadService.saveProductsFromCsv(file);
            return ResponseEntity.ok(Map.of("success", true, "message", "Product CSV file successfully uploaded and processed."));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("success", false, "message", "An error occurred while processing the file."));
        }
    }
}
