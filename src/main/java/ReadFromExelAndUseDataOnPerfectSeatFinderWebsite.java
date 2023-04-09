import com.codeborne.selenide.Configuration;
import pages.finderWebsite.PerfectSeatFinderPage;

import java.util.ArrayList;
import java.util.List;

import static com.codeborne.selenide.Selenide.*;

record TableRow(String destinationHubValue,
                String economyDemandValue,
                String businessDemandValue,
                String firstDemandValue,
                String cargoDemandValue,
                String economyPriceValue,
                String businessPriceValue,
                String firstPriceValue,
                String cargoPriceValue) { } //,String km

public class ReadFromExelAndUseDataOnPerfectSeatFinderWebsite {
    static final String URL = "https://destinations.noway.info/en/seatconfigurator/index.html";
     static final String sourceHubValue = "KBP";
     //String numberOfWavesNeeded;

    public static void main(String[] args) {
        ReadFile reader = new ReadFile();
        var dataFromExcel = reader.readFile();
        List<List<String>> dataFromPerfecrSeatFinderSite = new ArrayList<>();
        for (var row : dataFromExcel) {
            //insertValuesFromExelOnThePerfecrSeatFinderWebsite(row);

            Configuration.browserSize = "1920x1080";
            open(URL);

            PerfectSeatFinderPage perfectSeatFinderPage = new PerfectSeatFinderPage();

            perfectSeatFinderPage.fillTheFieldsOnTheWebsite(sourceHubValue,row.destinationHubValue(),
                    row.economyDemandValue(),row.businessDemandValue(),row.firstDemandValue(),
                    row.cargoDemandValue(),row.economyPriceValue(),row.businessPriceValue(),
                    row.firstPriceValue(),row.cargoPriceValue());


            dataFromPerfecrSeatFinderSite.add(perfectSeatFinderPage.collectDataFromPerfecrSeatFinderWebSite());
            clearBrowserCookies();
        }
        RecordFile.writeInfoFromPerfecrSeatFinderWebsite(dataFromPerfecrSeatFinderSite);
        closeWebDriver();
    }

   /* public static List<TableRow> readFile() {
        List<TableRow> dataFromExcel = new ArrayList<>();

        try (OPCPackage pkg = OPCPackage.open(Path.of("D:\\Miracle.xlsx").toFile());
             XSSFWorkbook workbook = new XSSFWorkbook(pkg);
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
                var km = sheet.getRow(i).getCell(9).getStringCellValue();
                dataFromExcel.add(new TableRow(destinationHubValue,
                        economyDemandValue,
                        businessDemandValue,
                        firstDemandValue,
                        cargoDemandValue,
                        economyPriceValue,
                        businessPriceValue,
                        firstPriceValue,
                        cargoPriceValue,
                        km));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return dataFromExcel;
    }*/


/*
    public static void insertValuesFromExelOnThePerfecrSeatFinderWebsite(TableRow row) {
*/
/*        String iframe4 = "aswift_4";
        String closeAdd = "//div[@class='grippy-host']";
        String aircraftManufacturerFeld = "cf_aircraftmake";
        String aircraftManufacturer = "//*[@id=\"cf_aircraftmake\"]/option[6]";//Bombardier
        String aircraftModelField = "cf_aircraftmodel";
        String aircraftModel = "//*[@id=\"cf_aircraftmodel\"]/option[1]";//CRJ-1000 (850 km/h)
        String changeTypeToInputForSourceHub = "//*[@id=\"nwy_airport_fullsearch\"]/table/tbody/tr[1]/td/a";
        final String sourceHub = "cf_hub_src";
        String changeTypeToInputForDestinationHub = "//*[@id=\"nwy_airport_dst_fullsearch\"]/table/tbody/tr[1]/td/a";
        String destinationHub = "cf_hub_dst";
        String economyDemand = "demand_eco";
        String businessDemand = "demand_bus";
        String firstDemand = "demand_first";
        String cargoDemand = "demand_cargo";
        String economyPrice = "auditprice_eco";
        String businessPrice = "auditprice_bus";
        String firstPrice = "auditprice_first";
        String cargoPrice = "auditprice_cargo";
        String addToCircuitButton = "add2circuit_button";
        String avoidNegativeConfigurationsCheckBox = "nonegativeconfig";
        String calculateButton = "calculate_button";
        String selectDropdown = "//*[@id=\"nwy_seatconfigurator_wave_1_stats\"]/table[1]/tbody/tr[3]/td[1]/select";
        String waveDropdown = "//select[@name='nwy_seatconfigurator_wave_1_selector']//option";*//*



        */
/*Configuration.browserSize = "1920x1080";
        open(URL);*//*


        //switchTo().frame($(By.id(iframe4)));

*/
/*        $(By.id(aircraftManufacturerFeld)).click();
        $x(aircraftManufacturer).click();
        $(By.id(aircraftModelField)).click();
        $x(aircraftModel).click();
        $x(changeTypeToInputForSourceHub).click();
        $(By.id(sourceHub)).click();
        $(By.id(sourceHub)).setValue(sourceHubValue);
        //actions().moveToElement($x(closeAdd).shouldBe(Condition.appear)).click().perform();
        $x(changeTypeToInputForDestinationHub).click();
        $(By.id(destinationHub)).click();
        $(By.id(destinationHub)).setValue(row.destinationHubValue());
        $(By.id(economyDemand)).click();
        $(By.id(economyDemand)).setValue(row.economyDemandValue());
        $(By.id(businessDemand)).click();
        $(By.id(businessDemand)).setValue(row.businessDemandValue());
        $(By.id(firstDemand)).click();
        $(By.id(firstDemand)).setValue(row.firstDemandValue());
        $(By.id(cargoDemand)).click();
        $(By.id(cargoDemand)).setValue(row.cargoDemandValue());
        $(By.id(economyPrice)).click();
        $(By.id(economyPrice)).setValue(row.economyPriceValue());
        $(By.id(businessPrice)).click();
        $(By.id(businessPrice)).setValue(row.businessPriceValue());
        $(By.id(firstPrice)).click();
        $(By.id(firstPrice)).setValue(row.firstPriceValue());
        $(By.id(cargoPrice)).click();
        $(By.id(cargoPrice)).setValue(row.cargoPriceValue());
        $(By.id(addToCircuitButton)).click();
        $(By.id(avoidNegativeConfigurationsCheckBox)).click();
        $(By.id(calculateButton)).click();
        $x(selectDropdown).click();
        $x(selectDropdown).selectOption(lastElementValueInTheDropDown($$x(waveDropdown)));*//*

    }
*/

/*    public static String lastElementValueInTheDropDown(ElementsCollection dropdownList) {
        numberOfWavesNeeded = dropdownList.get(dropdownList.size() - 1).getAttribute("value");
        return numberOfWavesNeeded;
    }*/

/*    public static List<String> collectDataFromPerfecrSeatFinderWebSite() {
        String wavesNum = numberOfWavesNeeded;

        String ecoSeats = "//*[@id=\"nwy_seatconfigurator_wave_" + wavesNum + "_stats\"]/table[1]/tbody/tr[3]/td[2]";
        String busSeats = "//*[@id=\"nwy_seatconfigurator_wave_" + wavesNum + "_stats\"]/table[1]/tbody/tr[3]/td[3]";
        String firstSeats = "//*[@id=\"nwy_seatconfigurator_wave_" + wavesNum + "_stats\"]/table[1]//tr[3]/td[4]";
        String crgSeats = "//*[@id=\"nwy_seatconfigurator_wave_" + wavesNum + "_stats\"]/table[1]//tr[3]/td[5]";

        String aitaCode = "//*[@id=\"nwy_seatconfigurator_wave_" + wavesNum + "_stats\"]/table[2]//tr[3]/td[1]";
        String ecoPrie = "//*[@id=\"nwy_seatconfigurator_wave_" + wavesNum + "_stats\"]/table[2]//tr[3]/td[2]";
        String busPrie = "//*[@id=\"nwy_seatconfigurator_wave_" + wavesNum + "_stats\"]/table[2]//tr[3]/td[3]";
        String firstPrie = "//*[@id=\"nwy_seatconfigurator_wave_" + wavesNum + "_stats\"]/table[2]//tr[3]/td[4]";
        String crgPrie = "//*[@id=\"nwy_seatconfigurator_wave_" + wavesNum + "_stats\"]/table[2]//tr[3]/td[5]";

        String ecoSeatResult = $x(ecoSeats).getText().trim().replaceAll("\\D+", "");
        String busSeatResult = $x(busSeats).getText().trim().replaceAll("\\D+", "");
        String fitstSeatResult = $x(firstSeats).getText().trim().replaceAll("\\D+", "");
        String cargSeatResult = $x(crgSeats).getText().trim().replaceAll("\\D+", "");
        String aitaCodeResult = $x(aitaCode).getText().trim();
        String ecoPriceResult = $x(ecoPrie).getText().trim().replaceAll("\\D+", "");
        String busPriceResult = $x(busPrie).getText().trim().replaceAll("\\D+", "");
        String firstPriceResult = $x(firstPrie).getText().trim().replaceAll("\\D+", "");
        String cargPriceResult = $x(crgPrie).getText().trim().replaceAll("\\D+", "");

        return List.of(wavesNum, aitaCodeResult, ecoSeatResult, busSeatResult, fitstSeatResult, cargSeatResult, ecoPriceResult, busPriceResult, firstPriceResult, cargPriceResult);
    }*/

}