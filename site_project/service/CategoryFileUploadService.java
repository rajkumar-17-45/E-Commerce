package com.ecommerce.site_project.service;

import com.ecommerce.site_project.entity.Category;
import com.ecommerce_project.repository.CategoryRepository;
import com.opencsv.CSVReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryFileUploadService {

    @Autowired
    private CategoryRepository categoryRepository;

    public void saveCategoriesFromCsv(MultipartFile file) throws Exception {
        List<Category> categories = new ArrayList<>();
        try (CSVReader reader = new CSVReader(new InputStreamReader(file.getInputStream()))) {
            String[] line;

            // Read and skip the header row
            reader.readNext();

            // Process each line of the CSV file
            while ((line = reader.readNext()) != null) {
                if (line.length < 4) {
                    // Skip rows that don't have enough columns
                    continue;
                }

                Category category = new Category();
                category.setTitle(line[0]);  // First column is the title
                category.setAlias(line[1]);  // Second column is the alias
                category.setImageURL(line[2]);  // Third column is the image URL
                category.setEnabled(Boolean.parseBoolean(line[3]));  // Fourth column is enabled status
                
                categories.add(category);
            }

            if (!categories.isEmpty()) {
                // Save all categories to the database
                categoryRepository.saveAll(categories);
            }
        }
    }
}
