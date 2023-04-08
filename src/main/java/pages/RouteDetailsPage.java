package pages;

import com.codeborne.selenide.SelenideElement;

import java.util.List;
import java.util.Map;

import static com.codeborne.selenide.Selenide.$x;
import static com.codeborne.selenide.Selenide.open;

public class RouteDetailsPage {
    public final static String airportName = "//*[@id=\"box2\"]/li[4]/b";

    public final static String economyDemand = "//*[@id=\"marketing_linePricing\"]/div[3]/div[1]/div[3]/b";
    public final static String businessDemand = "//*[@id=\"marketing_linePricing\"]/div[3]/div[2]/div[3]/b";
    public final static String firstDemand = "//*[@id=\"marketing_linePricing\"]/div[3]/div[3]/div[3]/b";
    public final static String cargoDemand = "//*[@id=\"marketing_linePricing\"]/div[3]/div[4]/div[3]/b";

    public final static String economyPrice = "//*[@id=\"marketing_linePricing\"]/div[3]/div[1]/div[2]/b";
    public final static String businessPrice = "//*[@id=\"marketing_linePricing\"]/div[3]/div[2]/div[2]/b";
    public final static String firstPrice = "//*[@id=\"marketing_linePricing\"]/div[3]/div[3]/div[2]/b";
    public final static String cargoPrice = "//*[@id=\"marketing_linePricing\"]/div[3]/div[4]/div[2]/b";

    public final static SelenideElement linkToPricesPage = $x("//*[@id=\"showLine\"]/div[5]/div/a");


    public void collectDemandValueAndPrices(Map<String, String> nameOfAirportAndLink, List<List<String>> rows) {
        for (Map.Entry<String, String> entry : nameOfAirportAndLink.entrySet()) {
            String name = entry.getKey();
            open(entry.getValue());

            String ecoDemand = $x(economyDemand).getText().trim();
            String businDemand = $x(businessDemand).getText().trim();
            String frtDemand = $x(firstDemand).getText().trim();
            String cargDemand = $x(cargoDemand).getText().trim();

            String ecoPrice = $x(economyPrice).getText().trim();
            String businPrice = $x(businessPrice).getText().trim();
            String frtPrice = $x(firstPrice).getText().trim();
            String cargPrice = $x(cargoPrice).getText().trim();

            String newEcoDemand = ecoDemand.replaceAll("\\D+", "");
            String newBusinDemand = businDemand.replaceAll("\\D+", "");
            String newFrtDemand = frtDemand.replaceAll("\\D+", "");
            String newCargDemand = cargDemand.replaceAll("\\D+", "");
            String newEcoPrice = ecoPrice.replaceAll("\\D+", "");
            String newbusinPrice = businPrice.replaceAll("\\D+", "");
            String newFrtPrice = frtPrice.replaceAll("\\D+", "");
            String newCargPrice = cargPrice.replaceAll("\\D+", "");

            rows.add(List.of(name, newEcoDemand, newBusinDemand, newFrtDemand, newCargDemand, newEcoPrice, newbusinPrice, newFrtPrice, newCargPrice));

        }
    }
}
