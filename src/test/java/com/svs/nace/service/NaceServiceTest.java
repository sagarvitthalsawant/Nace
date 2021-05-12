package com.svs.nace.service;

import com.svs.nace.entity.EconomicActivity;
import com.svs.nace.helper.ExcelHelper;
import com.svs.nace.repository.NaceRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.mock.web.MockMultipartFile;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class NaceServiceTest {

    @InjectMocks
    private NaceService naceService;

    @Mock
    private NaceRepository naceRepository;

    private ExcelHelper excelHelper;

    @BeforeEach
    void setup() {
        excelHelper =mock(ExcelHelper.class);
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void whenValidOrderId_thenEconomicActivityShouldBeFound() {
        EconomicActivity economicActivity = new EconomicActivity();
        economicActivity.setOrderNo(1234567L);
        when(naceService.findNaceDetailsByOrderId(any())).thenReturn(economicActivity);
        EconomicActivity economicActivityObj = naceService.findNaceDetailsByOrderId(1234567L);
        Assertions.assertNotNull(economicActivityObj);
    }

    @Test
    void whenInValidOrderId_thenEconomicActivityShouldNotBeFound() {
        when(naceService.findNaceDetailsByOrderId(eq(123L))).thenReturn(null);
        EconomicActivity findEconomicActivity = naceService.findNaceDetailsByOrderId(123L);
        Assertions.assertNull(findEconomicActivity);
    }

    @Test
    void whenValidMultipartFile_thenSaveFile() throws Exception {
        Resource resource = new ClassPathResource("NACE.xlsm");
        MockMultipartFile file = new MockMultipartFile("file", "NACE_REV2_20210204_135820.xlsm", "application/vnd.ms-excel.sheet.macroenabled.12", resource.getInputStream());
        naceService.saveNaceFile(file);
    }

}
