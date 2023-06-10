package filesManagment;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static propertyLoader.AirlinesProperties.getProperty;

public class WriteFile {
    public static void writeValuesIntoTextFile(List<String> listOfKms, String fileName) {
        try {
            Files.write(Path.of("D:\\AirlinesManager\\" + fileName + ".txt"), listOfKms);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void writeInfoFromInfoPage(List<List<String>> rows) {
        File xlsxFile = new File(getProperty("result-file-location"));
        FileInputStream inputStream;
        try {
            if (xlsxFile.createNewFile()) {
                XSSFWorkbook workbook = new XSSFWorkbook();
                FileOutputStream out = new FileOutputStream(getProperty("result-file-location"));
                workbook.write(out);
                out.close();
            }
            inputStream = new FileInputStream(xlsxFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Workbook workbook;
        try {
            workbook = WorkbookFactory.create(inputStream);
            Sheet spreadsheet = workbook.getSheet("InfoPage");
            if (spreadsheet == null) {
                spreadsheet = workbook.createSheet("InfoPage");
            } else {
                spreadsheet = workbook.getSheet("InfoPage");
            }

            Row header = spreadsheet.createRow(0);
            header.createCell(0).setCellValue("Destination");
            header.createCell(1).setCellValue("Economy Demand");
            header.createCell(2).setCellValue("Business Demand");
            header.createCell(3).setCellValue("First Demand");
            header.createCell(4).setCellValue("Cargo Demand");
            header.createCell(5).setCellValue("Economy Price");
            header.createCell(6).setCellValue("Business Price");
            header.createCell(7).setCellValue("First Price");
            header.createCell(8).setCellValue("Cargo Price");

            for (int i = 0; i < rows.size(); i++) {
                var row = spreadsheet.createRow(i + 1);
                var rowData = rows.get(i);
                for (int j = 0; j < rowData.size(); j++) {
                    row.createCell(j).setCellValue(rowData.get(j));
                }
            }
            try (var out = Files.newOutputStream(Path.of(getProperty("result-file-location")))) {
                workbook.write(out);
            }
        } catch (IOException | EncryptedDocumentException e) {
            throw new RuntimeException(e);
        }
    }

    public static void writeInfoFromPerfectSeatFinderWebsite(List<List<String>> rows) {
        File xlsxFile = new File(getProperty("result-file-location"));
        FileInputStream inputStream;
        try {
            inputStream = new FileInputStream(xlsxFile);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        Workbook workbook;
        try {
            workbook = WorkbookFactory.create(inputStream);
            Sheet spreadsheet = workbook.getSheet("ResultFromWebsite");
            if (spreadsheet == null) {
                spreadsheet = workbook.createSheet("ResultFromWebsite");
            } else {
                spreadsheet = workbook.getSheet("ResultFromWebsite");
            }

            Row header = spreadsheet.createRow(0);
            header.createCell(0).setCellValue("№ of Waves needed");
            header.createCell(1).setCellValue("AITA");
            header.createCell(2).setCellValue("Economy Seats");
            header.createCell(3).setCellValue("Business Seats");
            header.createCell(4).setCellValue("First Seats");
            header.createCell(5).setCellValue("Cargo Seats");
            header.createCell(6).setCellValue("Economy Price");
            header.createCell(7).setCellValue("Business Price");
            header.createCell(8).setCellValue("First Price");
            header.createCell(9).setCellValue("Cargo Price");
            header.createCell(10).setCellValue("Time for 1 wave");

            for (int i = 0; i < rows.size(); i++) {
                Row row = spreadsheet.createRow(i + 1);
                var rowData = rows.get(i);
                for (int j = 0; j < rowData.size(); j++) {
                    row.createCell(j).setCellValue(rowData.get(j));
                }
            }
            try (var out = Files.newOutputStream(Path.of(getProperty("result-file-location")))) {
                workbook.write(out);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void rewriteInfoFromPerfectSeatFinderWebsiteAfterPlaneConfiguration(List<List<String>> rows, String excelSheetName) {
        File xlsxFile = new File(getProperty("result-file-location"));
        FileInputStream inputStream;
        try {
            inputStream = new FileInputStream(xlsxFile);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        Workbook workbook;
        try {
            workbook = WorkbookFactory.create(inputStream);
            Sheet spreadsheet = workbook.getSheet(excelSheetName);
            if (spreadsheet == null) {
                spreadsheet = workbook.createSheet(excelSheetName);
            } else {
                spreadsheet = workbook.getSheet(excelSheetName);
            }

            Row header = spreadsheet.createRow(0);
            header.createCell(0).setCellValue("№ of Waves needed");
            header.createCell(1).setCellValue("AITA");
            header.createCell(2).setCellValue("Economy Seats");
            header.createCell(3).setCellValue("Business Seats");
            header.createCell(4).setCellValue("First Seats");
            header.createCell(5).setCellValue("Cargo Seats");
            header.createCell(6).setCellValue("Economy Price");
            header.createCell(7).setCellValue("Business Price");
            header.createCell(8).setCellValue("First Price");
            header.createCell(9).setCellValue("Cargo Price");

            for (int i = 0; i < rows.size(); i++) {
                Row row = spreadsheet.createRow(i + 1);
                var rowData = rows.get(i);
                for (int j = 0; j < rowData.size(); j++) {
                    row.createCell(j).setCellValue(rowData.get(j));
                }
            }
            try (var out = Files.newOutputStream(Path.of(getProperty("result-file-location")))) {
                workbook.write(out);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}