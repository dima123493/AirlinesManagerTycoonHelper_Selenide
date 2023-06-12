package filesManagment;

import infoFromFiles.GatheredInfoTableRow;
import infoFromFiles.ReadGatheredResultFromPerfectSeatWebsiteFile;
import infoFromFiles.testRecord;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static propertyLoader.AirlinesProperties.getProperty;

public class ReadFile {
    public static List<GatheredInfoTableRow> readGatheredInfoFile(String excelSheetName) {
        List<GatheredInfoTableRow> dataFromExcel = new ArrayList<>();
        try (OPCPackage pkg = OPCPackage.open(Path.of(getProperty("result-file-location")).toFile());
             XSSFWorkbook workbook = new XSSFWorkbook(pkg)
        ) {
            Sheet sheet = workbook.getSheet(excelSheetName);
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
                var distanceToAirport = sheet.getRow(i).getCell(9).getStringCellValue();
                dataFromExcel.add(new GatheredInfoTableRow(destinationHubValue,
                        economyDemandValue,
                        businessDemandValue,
                        firstDemandValue,
                        cargoDemandValue,
                        economyPriceValue,
                        businessPriceValue,
                        firstPriceValue,
                        cargoPriceValue,
                        distanceToAirport));
            }
        } catch (Exception e) {
            System.out.println("Check the fields values in the table for file on the InfoPage Excel list (it should be 9 in total) or check if file exists");
            throw new RuntimeException(e);
        }
        return dataFromExcel;
    }

    public static List<ReadGatheredResultFromPerfectSeatWebsiteFile> readGatheredResultFromPerfectSeatWebsiteFile(String excelSheetName) {
        List<ReadGatheredResultFromPerfectSeatWebsiteFile> dataFromExcel = new ArrayList<>();
        try (OPCPackage pkg = OPCPackage.open(Path.of(getProperty("result-file-location")).toFile());
             XSSFWorkbook workbook = new XSSFWorkbook(pkg)
        ) {
            Sheet sheet = workbook.getSheet(excelSheetName);
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                var numberOfWavesNeeded = sheet.getRow(i).getCell(0).getStringCellValue();
                var airportName = sheet.getRow(i).getCell(1).getStringCellValue();
                var economySeatsAmountNeeded = sheet.getRow(i).getCell(2).getStringCellValue();
                var businessSeatsAmountNeeded = sheet.getRow(i).getCell(3).getStringCellValue();
                var firstSeatsAmountNeeded = sheet.getRow(i).getCell(4).getStringCellValue();
                var cargoSeatsAmountNeeded = sheet.getRow(i).getCell(5).getStringCellValue();
                var economyPriceForRoute = sheet.getRow(i).getCell(6).getStringCellValue();
                var businessPriceForRoute = sheet.getRow(i).getCell(7).getStringCellValue();
                var firstPriceForRoute = sheet.getRow(i).getCell(8).getStringCellValue();
                var cargoPriceForRoute = sheet.getRow(i).getCell(9).getStringCellValue();
                //var timeTakenForOneWave = sheet.getRow(i).getCell(10).getStringCellValue();
                dataFromExcel.add(new ReadGatheredResultFromPerfectSeatWebsiteFile(numberOfWavesNeeded,
                        airportName,
                        economySeatsAmountNeeded,
                        businessSeatsAmountNeeded,
                        firstSeatsAmountNeeded,
                        cargoSeatsAmountNeeded,
                        economyPriceForRoute,
                        businessPriceForRoute,
                        firstPriceForRoute,
                        cargoPriceForRoute));
            }
        } catch (Exception e) {
            System.out.println("Check the fields values in the table for file on the ResultFromWebsite Excel list (it should be 10 in total and file suppose to exist while it was already used)");
            throw new RuntimeException(e);
        }
        return dataFromExcel;
    }

}