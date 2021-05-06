package com.svs.nace.service;

import com.svs.nace.controller.NaceController;
import com.svs.nace.entity.EconomicActivity;
import com.svs.nace.repository.NaceRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@SpringBootTest
public class NaceServiceTest {


    @InjectMocks
    private NaceService naceService;

    @Mock
    private NaceRepository naceRepository;

    @Before
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void whenValidOrderId_thenEconomicActivityShouldBeFound() {
        EconomicActivity economicActivity = new EconomicActivity();
        economicActivity.setOrderNo(1234567L);
        when(naceService.findNaceDetailsByOrderId(any())).thenReturn(economicActivity);
        EconomicActivity economicActivityObj = naceService.findNaceDetailsByOrderId(1234567L);
        Assertions.assertNotNull(economicActivityObj);
    }

    @Test
    public void whenInValidOrderId_thenEconomicActivityShouldNotBeFound() {
        when(naceService.findNaceDetailsByOrderId(eq(123L))).thenReturn(null);
        EconomicActivity findEconomicActivity = naceService.findNaceDetailsByOrderId(123L);
        Assert.assertNull(findEconomicActivity);
    }

    @Test
    public void whenValidMultipartFile_thenSaveFile() throws Exception {
        Resource resource = new ClassPathResource("NACE.xlsm");
        MockMultipartFile file = new MockMultipartFile("file", "NACE_REV2_20210204_135820.xlsm", "application/vnd.ms-excel.sheet.macroenabled.12", resource.getInputStream());
        naceService.saveNaceFile(file);
    }
}
