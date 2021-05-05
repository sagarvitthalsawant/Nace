package com.svs.nace.helper;

import com.svs.nace.entity.EconomicActivity;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.NumberToTextConverter;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelHelper {
    public static String TYPE = "application/vnd.ms-excel.sheet.macroenabled.12";
    static String[] HEADERs = { "Order", "Level", "Code", "Parent", "Description", "This item includes", "This item also includes", "Rulings", "This item excludes", "Reference to ISIC Rev. 4" };
    static String SHEET = "NACE_REV2_20210204_135820";

    public static boolean hasExcelFormat(MultipartFile file) {
        return TYPE.equalsIgnoreCase(file.getContentType());
    }

    public static List<EconomicActivity> excelToEconomicActivity(InputStream is) {
        try {
            Workbook workbook = new XSSFWorkbook(is);

            Sheet sheet = workbook.getSheet(SHEET);
            Iterator<Row> rows = sheet.iterator();

            List<EconomicActivity> tutorials = new ArrayList<EconomicActivity>();

            int rowNumber = 0;
            while (rows.hasNext()) {
                Row currentRow = rows.next();

                // skip header
                if (rowNumber == 0) {
                    rowNumber++;
                    continue;
                }

                Iterator<Cell> cellsInRow = currentRow.iterator();

                EconomicActivity tutorial = new EconomicActivity();

                int cellIdx = 0;
                while (cellsInRow.hasNext()) {
                    Cell currentCell = cellsInRow.next();

                     switch (cellIdx) {
                     case 0:
                     tutorial.setOrderNo((long) currentCell.getNumericCellValue());
                     break;

                     case 1:
                     tutorial.setLevel((long) currentCell.getNumericCellValue());
                     break;

                     case 2:
                         if (currentCell.getCellType() == CellType.NUMERIC) {
                             tutorial.setCode(NumberToTextConverter.toText(currentCell.getNumericCellValue()));
                         } else {tutorial.setCode(currentCell.getStringCellValue());}

                         break;

                     case 3:
                         if (currentCell.getCellType() == CellType.NUMERIC) {
                             tutorial.setParent(NumberToTextConverter.toText(currentCell.getNumericCellValue()));
                         } else {tutorial.setParent(currentCell.getStringCellValue());}
                     break;

                     case 4:
                             tutorial.setDescription(currentCell.getStringCellValue());
                     break;

                     case 5:
                             tutorial.setThisItemIncludes(currentCell.getStringCellValue());
                     break;

                     case 6:
                             tutorial.setThisItemAlsoIncludes(currentCell.getStringCellValue());
                     break;

                     case 7:
                         if (currentCell.getCellType() == CellType.NUMERIC) {
                             tutorial.setRuling(NumberToTextConverter.toText(currentCell.getNumericCellValue()));
                         } else {tutorial.setRuling(currentCell.getStringCellValue());}

                     break;

                     case 8:
                         if(! currentCell.getStringCellValue().isEmpty()) {
                             tutorial.setThisItemexcludes(currentCell.getStringCellValue());
                         }
                     break;

                     case 9:
                         if(! currentCell.getStringCellValue().isEmpty()) {
                             tutorial.setRefrenceToISIC(currentCell.getStringCellValue());
                         }
                     break;

                     default:
                     break;
                     }

                    cellIdx++;
                }

                tutorials.add(tutorial);
            }

            workbook.close();

            return tutorials;
        } catch (IOException e) {
            throw new RuntimeException("fail to parse Excel file: " + e.getMessage());
        }
    }
}