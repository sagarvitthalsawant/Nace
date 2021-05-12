package com.svs.nace.helper;

import com.svs.nace.entity.EconomicActivity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.function.Predicate;

public class ExcelHelperTest {

    private ExcelHelper excelHelper;

    @Test
    void hasValidExcelFormat_thenTrue() throws IOException {
        Resource resource = new ClassPathResource("NACE.xlsm");
        MockMultipartFile file = new MockMultipartFile("file", "NACE_REV2_20210204_135820.xlsm", "application/vnd.ms-excel.sheet.macroenabled.12", resource.getInputStream());
        Assertions.assertTrue(excelHelper.hasExcelFormat(file));
    }

    @Test
    void hasInValidExcelFormat_thenTrue(){
        MockMultipartFile file =
                new MockMultipartFile(
                        "file",
                        "NACE_REV2_20210204_135820.xlsm",
                        "application/vnd.pdf.12",
                        "<<pdf data>>".getBytes(StandardCharsets.UTF_8));

        Assertions.assertFalse(excelHelper.hasExcelFormat(file));
    }

    @Test
    void returnsValidObject_excelToEconomicActivity() throws IOException {
        Resource resource = new ClassPathResource("NACE.xlsm");
        MockMultipartFile file = new MockMultipartFile("file", "NACE_REV2_20210204_135820.xlsm", "application/vnd.ms-excel.sheet.macroenabled.12", resource.getInputStream());
        List<EconomicActivity> economicActivities = excelHelper.excelToEconomicActivity(file.getInputStream());

        Assertions.assertNotNull(economicActivities);
        Assertions.assertNotNull(economicActivities.get(0).getOrderNo());
    }
}
