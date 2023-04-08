package pages;

import static com.codeborne.selenide.Selenide.$x;

public class RoutePricePage {
    String performAudit = "//*[@id=\"marketing_linePricing\"]/div[1]/a/span";//hover,click
    String performAuditButton = "//div[@id='popupContainer']//span[@id='internalAuditButton']";

    String economyClassPrice = "line_priceEco";
    String businessClassPrice = "line_priceBus";
    String firstClassPrice = "line_priceFirst";
    String cargoPrice = "line_priceCargo";
    String submitPricesButton = "//*[@id=\"marketing_linePricing\"]/form/div/input";

    public void performAudit(){
        $x(performAudit).hover().click();
        $x(performAuditButton).hover().click();
    }

    public void applyNewPrices(String economyPrice,String businessPrice,String firstPrice,String crgPrice ){
        $x(economyClassPrice).setValue(economyPrice);
        $x(businessClassPrice).setValue(businessPrice);
        $x(firstClassPrice).setValue(firstPrice);
        $x(cargoPrice).setValue(crgPrice);
        $x(submitPricesButton).click();
    }
}
