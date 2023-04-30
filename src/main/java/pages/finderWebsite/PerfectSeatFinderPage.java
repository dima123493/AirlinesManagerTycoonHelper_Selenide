package pages.finderWebsite;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import infoFromFiles.GatheredInfoTableRow;
import org.openqa.selenium.By;

import java.util.List;

import static com.codeborne.selenide.Selenide.*;

public class PerfectSeatFinderPage {
    String aircraftManufacturerField = "cf_aircraftmake";
    String aircraftManufacturer;
    String aircraftModelField = "cf_aircraftmodel";
    String aircraftModel;
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

    public String aircraftManufacturerSelector(String manufacturerName) {
        return aircraftManufacturer = "//*[@id=\"cf_aircraftmake\"]/option[text()=\"" + manufacturerName + "\"]";
    }

    public String aircraftModelSelector(String modelNumber) {
        return aircraftModel = "//*[@id=\"cf_aircraftmodel\"]/option[contains(text(),\"" + modelNumber + "\")]";
    }

    public void fillTheFieldsOnTheWebsite(String sourceHubValue, String manufacturerName, String modelNumber, GatheredInfoTableRow row) {
        $(By.id(aircraftManufacturerField)).click();
        $x(aircraftManufacturerSelector(manufacturerName)).click();
        $(By.id(aircraftModelField)).click();
        $x(aircraftModelSelector(modelNumber)).click();
        $x(changeTypeToInputForSourceHub).click();
        $(By.id(sourceHub)).click();
        $(By.id(sourceHub)).setValue(sourceHubValue);
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
        $x(selectDropdown).scrollTo().click();
        $x(selectDropdown).selectOption(lastElementValueInTheDropDown($$x(waveDropdown)));
    }

    public static String lastElementValueInTheDropDown(ElementsCollection dropdownList) {
        numberOfWavesNeeded = dropdownList.get(dropdownList.size() - 1).getAttribute("value");
        return numberOfWavesNeeded;
    }

    public static List<String> collectDataFromPerfectSeatFinderWebSite() {
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

        String ecoSeatResult = $x(ecoSeats).shouldBe(Condition.visible).getText().trim().replaceAll("\\D+", "");
        String busSeatResult = $x(busSeats).shouldBe(Condition.visible).getText().trim().replaceAll("\\D+", "");
        String fistSeatResult = $x(firstSeats).shouldBe(Condition.visible).getText().trim().replaceAll("\\D+", "");
        String cargoSeatResult = $x(crgSeats).shouldBe(Condition.visible).getText().trim().replaceAll("\\D+", "");
        String aitaCodeResult = $x(aitaCode).shouldBe(Condition.visible).getText().trim();
        String ecoPriceResult = $x(ecoPrice).shouldBe(Condition.visible).getText().trim().replaceAll("\\D+", "");
        String busPriceResult = $x(busPrice).shouldBe(Condition.visible).getText().trim().replaceAll("\\D+", "");
        String firstPriceResult = $x(firstPrice).shouldBe(Condition.visible).getText().trim().replaceAll("\\D+", "");
        String cargoPriceResult = $x(crgPrice).shouldBe(Condition.visible).getText().trim().replaceAll("\\D+", "");

        return List.of(wavesNum, aitaCodeResult, ecoSeatResult, busSeatResult, fistSeatResult, cargoSeatResult, ecoPriceResult, busPriceResult, firstPriceResult, cargoPriceResult);
    }

}