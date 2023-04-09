import infoFromFiles.TableRow;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class ReadFile {
    public static List<TableRow> readFile() {
        List<TableRow> dataFromExcel = new ArrayList<>();

        try (OPCPackage pkg = OPCPackage.open(Path.of("D:\\AirlinesManager\\Miracle.xlsx").toFile());
             XSSFWorkbook workbook = new XSSFWorkbook(pkg)
        ) {
            Sheet sheet = workbook.getSheet("InfoPage");
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                var destinationHubValue = sheet.getRow(i).getCell(0).getStringCellValue();
                var economyDemandValue = sheet.getRow(i).getCell(1).getStringCellValue();
                var businessDemandValue = sheet.getRow(i).getCell(2).getStringCellValue();
                var firstDemandValue = sheet.getRow(i).getCell(3).getStringCellValue();
                var cargoDemandValue = sheet.getRow(i).getCell(4).getStringCellValue();
                var economyPriceValue = sheet.getRow(i).getCell(5).getStringCellValue();
                var businessPriceValue = sheet.getRow(i).getCell(6).getStringCellValue();
                var firstPriceValue = sheet.getRow(i).getCell(7).getStringCellValue();
                var cargoPriceValue = sheet.getRow(i).getCell(8).getStringCellValue();
                // var km = sheet.getRow(i).getCell(9).getStringCellValue();
                dataFromExcel.add(new TableRow(destinationHubValue,
                        economyDemandValue,
                        businessDemandValue,
                        firstDemandValue,
                        cargoDemandValue,
                        economyPriceValue,
                        businessPriceValue,
                        firstPriceValue,
                        cargoPriceValue));
                //,km
            }
        } catch (Exception e) {
            System.out.println("Check the fields values in the table for file on InfoPage Excel list (it should be 9 in total)");
            throw new RuntimeException(e);
        }
        return dataFromExcel;
    }


}
