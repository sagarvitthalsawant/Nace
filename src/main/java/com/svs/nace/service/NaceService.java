package com.svs.nace.service;

import com.svs.nace.helper.ExcelHelper;
import com.svs.nace.entity.EconomicActivity;
import com.svs.nace.repository.NaceRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@Slf4j
public class NaceService {

    @Autowired
    private NaceRepository naceRepository;

    public EconomicActivity save(EconomicActivity economicActivity){
        return naceRepository.save(economicActivity);
    }

    public void saveNaceFile(MultipartFile file){
        try{
            List<EconomicActivity> economicActivities = ExcelHelper.excelToEconomicActivity(file.getInputStream());
            economicActivities.forEach(economicActivity -> save(economicActivity));
        } catch (IOException e) {
            log.warn(e.getMessage(), e);
        }
    }

    public EconomicActivity findNaceDetailsByOrderId(Long economicActivityId) {
        EconomicActivity economicActivity = naceRepository.findByOrderNo(economicActivityId);
        return economicActivity;
    }
}
