package pages;

import org.openqa.selenium.By;

import java.util.List;
import java.util.Map;

import static com.codeborne.selenide.Selenide.*;

public class RouteDetailsPage {
    public static final String airportName = "//*[@id=\"box2\"]/li[4]/b";

    public static final String economyDemand = "//*[@id=\"marketing_linePricing\"]/div[3]/div[1]/div[3]/b";
    public static final String businessDemand = "//*[@id=\"marketing_linePricing\"]/div[3]/div[2]/div[3]/b";
    public static final String firstDemand = "//*[@id=\"marketing_linePricing\"]/div[3]/div[3]/div[3]/b";
    public static final String cargoDemand = "//*[@id=\"marketing_linePricing\"]/div[3]/div[4]/div[3]/b";

    public static final String economyPrice = "//*[@id=\"marketing_linePricing\"]/div[3]/div[1]/div[2]/b";
    public static final String businessPrice = "//*[@id=\"marketing_linePricing\"]/div[3]/div[2]/div[2]/b";
    public static final String firstPrice = "//*[@id=\"marketing_linePricing\"]/div[3]/div[3]/div[2]/b";
    public static final String cargoPrice = "//*[@id=\"marketing_linePricing\"]/div[3]/div[4]/div[2]/b";
    public static final String linkToPricesPage = "//*[@id=\"showLine\"]/div[5]/div/a";
    public void collectDemandValueAndPrices(Map<String, String> nameOfAirportAndLink, List<List<String>> rows) {
        for (Map.Entry<String, String> entry : nameOfAirportAndLink.entrySet()) {
            String name = entry.getKey();
            open(entry.getValue());

            //here page can be reloaded (should be checked)
            RoutePricePage action = new RoutePricePage();
            action.auditProcedure();
            //here page can be reloaded (should be checked)

            String ecoDemand = $x(economyDemand).getText().trim().replaceAll("\\D+", "");
            String businDemand = $x(businessDemand).getText().trim().replaceAll("\\D+", "");
            String frtDemand = $x(firstDemand).getText().trim().replaceAll("\\D+", "");
            String cargDemand = $x(cargoDemand).getText().trim().replaceAll("\\D+", "");

            String ecoPrice = $x(economyPrice).getText().trim().replaceAll("\\D+", "");
            String businPrice = $x(businessPrice).getText().trim().replaceAll("\\D+", "");
            String frtPrice = $x(firstPrice).getText().trim().replaceAll("\\D+", "");
            String cargPrice = $x(cargoPrice).getText().trim().replaceAll("\\D+", "");

/*            String newEcoDemand = ecoDemand.replaceAll("\\D+", "");
            String newBusinDemand = businDemand.replaceAll("\\D+", "");
            String newFrtDemand = frtDemand.replaceAll("\\D+", "");
            String newCargDemand = cargDemand.replaceAll("\\D+", "");
            String newEcoPrice = ecoPrice.replaceAll("\\D+", "");
            String newbusinPrice = businPrice.replaceAll("\\D+", "");
            String newFrtPrice = frtPrice.replaceAll("\\D+", "");
            String newCargPrice = cargPrice.replaceAll("\\D+", "");*/

            rows.add(List.of(name, ecoDemand, businDemand, frtDemand, cargDemand, ecoPrice, businPrice, frtPrice, cargPrice));
        }
    }

    public void collectAirPotNamesAndLinks(Map<String, String> nameOfAirportAndLink) {
        String portName = $x(airportName).getText().trim().toUpperCase().substring(0, 3);
        String link = $x(linkToPricesPage).getAttribute("href");
        nameOfAirportAndLink.put(portName, link);
    }
}