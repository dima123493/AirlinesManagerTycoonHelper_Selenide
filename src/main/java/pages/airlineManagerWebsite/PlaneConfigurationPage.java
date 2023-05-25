package pages.airlineManagerWebsite;

import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;
import static java.lang.Long.parseLong;

public class PlaneConfigurationPage {
    String economySeats = "ecoManualInput";
    String businessSeats = "busManualInput";
    String firstSeats = "firstManualInput";
    String cargoSeats = "cargoManualInput";
    String amountOfMoneyInMyAccount = "ressource3";
    String totalPrice = "totalPrice";
    String confirmButton = "//*[@id=\"showEquipment\"]/div[10]/input[1]";

    public boolean isPageLoaded() {
        return $(By.id(economySeats)).isDisplayed();
    }

    public boolean configureAircraftSeats(String ecoSeats, String busSeats, String fstSeats, String crgSeats) {
        $(By.id(economySeats)).clear();
        $(By.id(businessSeats)).clear();
        $(By.id(firstSeats)).clear();
        $(By.id(cargoSeats)).clear();

        $(By.id(economySeats)).setValue(ecoSeats);
        $(By.id(businessSeats)).setValue(busSeats);
        $(By.id(firstSeats)).setValue(fstSeats);
        $(By.id(cargoSeats)).setValue(crgSeats);
        if (enoughMoneyForReconfiguration()) {
            $(By.xpath(confirmButton)).hover().click();
            return true;
        } else {
            return false;
        }
    }

    public boolean enoughMoneyForReconfiguration() {
        long myAccountMoney;
        long reconfigurationPrice;
        myAccountMoney = parseLong(($(By.id(amountOfMoneyInMyAccount)).getText().replaceAll("(?<=.)\\D+", "")));
        reconfigurationPrice = parseLong($(By.id(totalPrice)).getText().trim().replaceAll("\\D+", ""));
        return reconfigurationPrice < myAccountMoney || reconfigurationPrice == 0;
    }

}