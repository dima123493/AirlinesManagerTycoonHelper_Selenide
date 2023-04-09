package pages.finderWebsite;

import com.codeborne.selenide.ElementsCollection;
import infoFromFiles.TableRow;
import org.openqa.selenium.By;

import java.util.List;

import static com.codeborne.selenide.Selenide.*;

public class PerfectSeatFinderPage {
    String iframe4 = "aswift_4";
    String closeAdd = "//div[@class='grippy-host']";
    String aircraftManufacturerFeld = "cf_aircraftmake";
    String aircraftManufacturer = "//*[@id=\"cf_aircraftmake\"]/option[6]";//TODO Bombardier
    String aircraftModelField = "cf_aircraftmodel";
    String aircraftModel = "//*[@id=\"cf_aircraftmodel\"]/option[1]";//TODO CRJ-1000 (850 km/h)
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
    String waveDropdown = "//select[@name='nwy_seatconfigurator_wave_1_selector']//option";

    static String numberOfWavesNeeded;
    public void fillTheFieldsOnTheWebsite(String sourceHubValue, TableRow row){
        $(By.id(aircraftManufacturerFeld)).click();
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
        $(By.id(cargoPrice)).setValue(row.economyPriceValue());
        $(By.id(addToCircuitButton)).click();
        $(By.id(avoidNegativeConfigurationsCheckBox)).click();
        $(By.id(calculateButton)).click();
        $x(selectDropdown).click();
        $x(selectDropdown).selectOption(lastElementValueInTheDropDown($$x(waveDropdown)));
    }

    public static String lastElementValueInTheDropDown(ElementsCollection dropdownList) {
        numberOfWavesNeeded = dropdownList.get(dropdownList.size() - 1).getAttribute("value");
        return numberOfWavesNeeded;
    }

    public static List<String> collectDataFromPerfecrSeatFinderWebSite() {
        String wavesNum = numberOfWavesNeeded;

        String ecoSeats = "//*[@id=\"nwy_seatconfigurator_wave_" + wavesNum + "_stats\"]/table[1]/tbody/tr[3]/td[2]";
        String busSeats = "//*[@id=\"nwy_seatconfigurator_wave_" + wavesNum + "_stats\"]/table[1]/tbody/tr[3]/td[3]";
        String firstSeats = "//*[@id=\"nwy_seatconfigurator_wave_" + wavesNum + "_stats\"]/table[1]//tr[3]/td[4]";
        String crgSeats = "//*[@id=\"nwy_seatconfigurator_wave_" + wavesNum + "_stats\"]/table[1]//tr[3]/td[5]";

        String aitaCode = "//*[@id=\"nwy_seatconfigurator_wave_" + wavesNum + "_stats\"]/table[2]//tr[3]/td[1]";
        String ecoPrice = "//*[@id=\"nwy_seatconfigurator_wave_" + wavesNum + "_stats\"]/table[2]//tr[3]/td[2]";
        String busPrice = "//*[@id=\"nwy_seatconfigurator_wave_" + wavesNum + "_stats\"]/table[2]//tr[3]/td[3]";
        String firstPrice = "//*[@id=\"nwy_seatconfigurator_wave_" + wavesNum + "_stats\"]/table[2]//tr[3]/td[4]";
        String crgPrice = "//*[@id=\"nwy_seatconfigurator_wave_" + wavesNum + "_stats\"]/table[2]//tr[3]/td[5]";

        String ecoSeatResult = $x(ecoSeats).getText().trim().replaceAll("\\D+", "");
        String busSeatResult = $x(busSeats).getText().trim().replaceAll("\\D+", "");
        String fistSeatResult = $x(firstSeats).getText().trim().replaceAll("\\D+", "");
        String cargoSeatResult = $x(crgSeats).getText().trim().replaceAll("\\D+", "");
        String aitaCodeResult = $x(aitaCode).getText().trim();
        String ecoPriceResult = $x(ecoPrice).getText().trim().replaceAll("\\D+", "");
        String busPriceResult = $x(busPrice).getText().trim().replaceAll("\\D+", "");
        String firstPriceResult = $x(firstPrice).getText().trim().replaceAll("\\D+", "");
        String cargoPriceResult = $x(crgPrice).getText().trim().replaceAll("\\D+", "");

        return List.of(wavesNum, aitaCodeResult, ecoSeatResult, busSeatResult, fistSeatResult, cargoSeatResult, ecoPriceResult, busPriceResult, firstPriceResult, cargoPriceResult);
    }

}
