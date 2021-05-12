package com.svs.nace.controller;

import com.svs.nace.entity.EconomicActivity;
import com.svs.nace.helper.ExcelHelper;
import com.svs.nace.service.NaceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/nace")
@Slf4j
public class NaceController {

    @Autowired
    private NaceService economicActivityService;

    @GetMapping("/{id}")
    public EconomicActivity getNaceDetails(@PathVariable("id") Long economicActivityId){
        log.info("recieved getNaceDetails request : {}", economicActivityId);
        return economicActivityService.findNaceDetailsByOrderId(economicActivityId);
    }

    @PostMapping("/upload")
    public ResponseEntity<String> postNaceDetails(@RequestParam("file") MultipartFile file) {
        if (ExcelHelper.hasExcelFormat(file)) {
            try {
                economicActivityService.saveNaceFile(file);
                return ResponseEntity.status(HttpStatus.OK).body("Uploaded the file successfully: " + file.getOriginalFilename());
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body("Could not upload the file: " + file.getOriginalFilename() + "!");
            }
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Please upload an excel file!");
    }
}

