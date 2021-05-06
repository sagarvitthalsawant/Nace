package com.svs.nace.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.svs.nace.controller.NaceController;
import com.svs.nace.entity.EconomicActivity;
import com.svs.nace.repository.NaceRepository;
import com.svs.nace.service.NaceService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;

import static org.hamcrest.CoreMatchers.containsString;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.hamcrest.CoreMatchers.is;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class NaceControllerTest {

    private MockMvc mvc;

    @Mock
    private NaceService economicActivityService;

    @InjectMocks
    private NaceController naceController;

    @Before
    public void setup(){
        MockitoAnnotations.openMocks(this);
        this.mvc = MockMvcBuilders.standaloneSetup(naceController).build();
    }

    @Test
    public void givenValidEconomicActivityOrderNo_thenStatus200()
            throws Exception {

        EconomicActivity economicActivity = new EconomicActivity();
        economicActivity.setOrderNo(399068L);
        when(economicActivityService.findNaceDetailsByOrderId(eq(399068L))).thenReturn(economicActivity);
        mvc.perform(get("/nace/399068"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("orderNo", is(399068)));
    }

    @Test
    public void whenGivenWrongEndpoint_thenStatus404()
            throws Exception {

        EconomicActivity economicActivity = new EconomicActivity();
        economicActivity.setOrderNo(1234567L);
        when(economicActivityService.findNaceDetailsByOrderId(eq(1234567L))).thenReturn(economicActivity);
        mvc.perform(get("/naec/1234567"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void givenValidFileToUpload_thenStatus200()
            throws Exception {

        when(economicActivityService.save(any())).thenReturn(new EconomicActivity());

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
    public void givenValidFileToUploadFromResources_thenStatus200()
            throws Exception {

        when(economicActivityService.save(any())).thenReturn(new EconomicActivity());
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
    public void givenEmployees_whenGetEmployees_thenStatus400()
            throws Exception {

        when(economicActivityService.save(any())).thenReturn(new EconomicActivity());
        Resource resource = new ClassPathResource("NACE.xlsm");
        MockMultipartFile file = new MockMultipartFile("fileName", "NACE_REV2_20210204_135820.xlsm", "application/vnd.ms-excel.sheet.macroenabled.12", resource.getInputStream());

        mvc.perform(
                multipart("/nace/upload")
                        .file(file)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void givenInValidFileToUpload()
            throws Exception {

        when(economicActivityService.save(any())).thenReturn(new EconomicActivity());

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
