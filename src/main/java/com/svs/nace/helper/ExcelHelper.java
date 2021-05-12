package com.svs.nace.helper;

import com.svs.nace.entity.EconomicActivity;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.NumberToTextConverter;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

@Slf4j
public class ExcelHelper {
    public static String TYPE = "application/vnd.ms-excel.sheet.macroenabled.12";
    static String SHEET = "NACE_REV2_20210204_135820";

    public static boolean hasExcelFormat(MultipartFile file) {
        return TYPE.equalsIgnoreCase(file.getContentType());
    }

    public static List<EconomicActivity> excelToEconomicActivity(InputStream is) throws IOException {
            Workbook workbook = new XSSFWorkbook(is);

            Sheet sheet = workbook.getSheet(SHEET);
            Iterator<Row> rows = sheet.iterator();

            List<EconomicActivity> economicActivities = new ArrayList<>();

            int rowNumber = 0;
            while (rows.hasNext()) {
                Row currentRow = rows.next();

                // skip header
                if (rowNumber == 0) {
                    rowNumber++;
                    continue;
                }

                Iterator<Cell> cellsInRow = currentRow.iterator();

                EconomicActivity economicActivity = new EconomicActivity();

                int cellIdx = 0;
                while (cellsInRow.hasNext()) {
                    Cell currentCell = cellsInRow.next();

                     switch (cellIdx) {
                     case 0:
                         economicActivity.setOrderNo((long) currentCell.getNumericCellValue());
                         break;

                     case 1:
                         economicActivity.setLevel((long) currentCell.getNumericCellValue());
                     break;

                     case 2:
                         economicActivity.setCode(getCellValueBasedOnCellType(currentCell));
                     break;

                     case 3:
                         economicActivity.setParent(getCellValueBasedOnCellType(currentCell));
                     break;

                     case 4:
                         economicActivity.setDescription(getCellValueBasedOnCellType(currentCell));
                     break;

                     case 5:
                         economicActivity.setThisItemIncludes(getCellValueBasedOnCellType(currentCell));
                     break;

                     case 6:
                         economicActivity.setThisItemAlsoIncludes(getCellValueBasedOnCellType(currentCell));
                    break;

                     case 7:
                         economicActivity.setRuling(getCellValueBasedOnCellType(currentCell));
                     break;

                     case 8:
                         economicActivity.setThisItemexcludes(getCellValueBasedOnCellType(currentCell));
                     break;

                     case 9:
                         economicActivity.setRefrenceToISIC(getCellValueBasedOnCellType(currentCell));
                     break;

                     default:
                     break;
                     }

                    cellIdx++;
                }

                economicActivities.add(economicActivity);
            }

            workbook.close();

            return economicActivities;

    }

    public static <T> T getCellValueBasedOnCellType(Cell currentCell) {
        if(currentCell.getCellType() == CellType.NUMERIC) {
            return (T) NumberToTextConverter.toText(currentCell.getNumericCellValue());
        } else if (currentCell.getCellType() == CellType.FORMULA) {
            return (T) currentCell.getCellFormula();
        } else { return (T) currentCell.getStringCellValue(); }
    }
}