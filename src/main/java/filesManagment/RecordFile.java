package filesManagment;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class RecordFile {

/*    public static void main(String[] args) {
        Map<String, String> nameOfAirportAndLink = new LinkedHashMap<>();
        nameOfAirportAndLink.put("1", "https://tycoon.airlines-manager.com/marketing/pricing/62313174");
        nameOfAirportAndLink.put("2", "https://tycoon.airlines-manager.com/marketing/pricing/57168817");
        nameOfAirportAndLink.put("3", "p3");

        for (String value : nameOfAirportAndLink.values()) {
            writeLinksToPricePageForEachRoute(value);
        }
    }*/

    public void writeValuesForEachRoute(List<String> listOfKms, String fileName) {
        try {
            Files.write(Path.of("D:\\" + fileName + ".txt"), listOfKms);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
/*        File file = new File("D:\\" + fileName + ".txt");
        try {
            if (file.createNewFile()) {
                System.out.println("File created: " + file.getName());
            }
        } catch (IOException e) {
            System.out.println("File can not be created!");
            e.printStackTrace();
        }

        FileWriter myWriter = null;
        try {
           //List<String> lines = Files.readAllLines(file.toPath());
            //boolean exist = true;
            //while (!lines.contains(value) && exist) {
                myWriter = new FileWriter(file,true);// ,true continues to write in the document
                myWriter.write(System.lineSeparator());// adds new line
                myWriter.write(listOfKms.toString());
                System.out.println("Record was added to the file.");
                myWriter.close();
          //      exist = false;
          //  }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }*/

    }

    /*public static void writeValuesForEachRoute(int value, String fileName) {
        File file = new File("D:\\" + fileName + ".txt");
        try {
            if (file.createNewFile()) {
                System.out.println("File created: " + file.getName());
            }
        } catch (IOException e) {
            System.out.println("File can not be created!");
            e.printStackTrace();
        }

        FileWriter myWriter = null;
        try {
            //List<String> lines = Files.readAllLines(file.toPath());
            //boolean exist = true;
           // while (!lines.contains(value) && exist) {
                myWriter = new FileWriter(file,true);// ,true continues to write in the document
                myWriter.write(System.lineSeparator());// adds new line
                myWriter.write(value);
                System.out.println("Record was added to the file.");
                myWriter.close();
            //    exist = false;
           // }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }*/

    public void writeInfoFromInfoPage(List<List<String>> rows) {
        File xlsxFile = new File("D:\\Miracle.xlsx");
        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(xlsxFile);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        Workbook workbook = null;
        try {
            workbook = WorkbookFactory.create(inputStream);
            // XSSFWorkbook workbook = new XSSFWorkbook();
            Sheet spreadsheet = workbook.getSheet("InfoPage");
            if (spreadsheet == null) {
                spreadsheet = workbook.createSheet("InfoPage");
            } else {
                spreadsheet = workbook.getSheet("InfoPage");
            }

            //XSSFWorkbook workbook = new XSSFWorkbook();
            // XSSFSheet spreadsheet = workbook.createSheet("InfoPage");

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
            try (var out = Files.newOutputStream(Path.of("D:\\Miracle.xlsx"))) {
                workbook.write(out);
            }
        } catch (IOException | EncryptedDocumentException e) {
            throw new RuntimeException(e);
        }
    }

    public static void writeInfoFromPerfecrSeatFinderWebsite(List<List<String>> rows) {
        File xlsxFile = new File("D:\\Miracle.xlsx");
        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(xlsxFile);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        Workbook workbook = null;
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

            for (int i = 0; i < rows.size(); i++) {
                Row row = spreadsheet.createRow(i + 1);
                var rowData = rows.get(i);
                for (int j = 0; j < rowData.size(); j++) {
                    row.createCell(j).setCellValue(rowData.get(j));
                }
            }
            try (var out = Files.newOutputStream(Path.of("D:\\Miracle.xlsx"))) {
                workbook.write(out);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}