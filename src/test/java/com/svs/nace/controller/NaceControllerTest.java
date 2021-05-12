package com.svs.nace.controller;

import com.svs.nace.entity.EconomicActivity;
import com.svs.nace.service.NaceService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import java.nio.charset.StandardCharsets;

import static org.hamcrest.CoreMatchers.containsString;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.hamcrest.CoreMatchers.is;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class NaceControllerTest {

    private MockMvc mvc;

    @Mock
    private NaceService naceService;

    @InjectMocks
    private NaceController naceController;

    @BeforeEach
    void setup(){
        MockitoAnnotations.openMocks(this);
        this.mvc = MockMvcBuilders.standaloneSetup(naceController).build();
    }

    @Test
    void givenValidEconomicActivityOrderNo_thenStatus200()
            throws Exception {

        EconomicActivity economicActivity = new EconomicActivity();
        economicActivity.setOrderNo(399068L);
        when(naceService.findNaceDetailsByOrderId(eq(399068L))).thenReturn(economicActivity);
        mvc.perform(get("/nace/399068"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("orderNo", is(399068)));
    }

    @Test
    void whenGivenWrongEndpoint_thenStatus404()
            throws Exception {

        EconomicActivity economicActivity = new EconomicActivity();
        economicActivity.setOrderNo(1234567L);
        when(naceService.findNaceDetailsByOrderId(eq(1234567L))).thenReturn(economicActivity);
        mvc.perform(get("/naec/1234567"))
                .andExpect(status().isNotFound());
    }

    @Test
    void givenValidFileToUpload_thenStatus200()
            throws Exception {

        when(naceService.save(any())).thenReturn(new EconomicActivity());

        MockMultipartFile file =
                new MockMultipartFile(
                        "file",
                        "NACE_REV2_20210204_135820.xlsm",
                        "application/vnd.ms-excel.sheet.macroenabled.12",
                        "<<pdf data>>".getBytes(StandardCharsets.UTF_8));

        mvc.perform(
                multipart("/nace/upload")
                        .file(file)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
        .andExpect(content().string(containsString("Uploaded the file successfully: NACE_REV2_20210204_135820.xlsm")));
    }

    @Test
    void givenInValidMSFileToUpload()
            throws Exception {

        MockMultipartFile file = new MockMultipartFile("file", "someTestFile.xls", "application/vnd.ms-excel", "<<pdf data>>".getBytes(StandardCharsets.UTF_8));

        mvc.perform(
                multipart("/nace/upload")
                        .file(file)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(content().string(containsString("Please upload an excel file!")));
    }

    @Test
    void givenValidFileToUploadFromResources_thenStatus200()
            throws Exception {

        when(naceService.save(any())).thenReturn(new EconomicActivity());
        Resource resource = new ClassPathResource("NACE.xlsm");
        MockMultipartFile file = new MockMultipartFile("file", "NACE_REV2_20210204_135820.xlsm", "application/vnd.ms-excel.sheet.macroenabled.12", resource.getInputStream());

        mvc.perform(
                multipart("/nace/upload")
                        .file(file)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Uploaded the file successfully: NACE_REV2_20210204_135820.xlsm")));
    }

    @Test
    void givenEmployees_whenGetEmployees_thenStatus400()
            throws Exception {

        when(naceService.save(any())).thenReturn(new EconomicActivity());
        Resource resource = new ClassPathResource("NACE.xlsm");
        MockMultipartFile file = new MockMultipartFile("fileName", "NACE_REV2_20210204_135820.xlsm", "application/vnd.ms-excel.sheet.macroenabled.12", resource.getInputStream());

        mvc.perform(
                multipart("/nace/upload")
                        .file(file)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void givenEmptyFileToUpload() throws Exception {
        Resource resource = new ClassPathResource("NACE1.xlsm");
        MockMultipartFile file = new MockMultipartFile("file", "NACE_REV2_20210204_135820.xlsm", "application/vnd.ms-excel.sheet.macroenabled.12", resource.getInputStream());

        NaceController nace = new NaceController();
        ResponseEntity<String> stringResponseEntity = nace.postNaceDetails(file);
        Assertions.assertEquals(HttpStatus.EXPECTATION_FAILED, stringResponseEntity.getStatusCode());
        Assertions.assertEquals("Could not upload the file: NACE_REV2_20210204_135820.xlsm!", stringResponseEntity.getBody());
    }

    @Test
    void givenInValidFileToUpload()
            throws Exception {

        when(naceService.save(any())).thenReturn(new EconomicActivity());

        MockMultipartFile file =
                new MockMultipartFile(
                        "file",
                        "NACE_RgvjhgjEV2_20210204_135820.xlsm",
                        "application/vnddxfgxch.ms-excel.sheet.macroenabled.12",
                        "<<pdf data>>".getBytes(StandardCharsets.UTF_8));

        mvc.perform(
                multipart("/nace/upload")
                        .file(file)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Please upload an excel file!")));
    }
}
