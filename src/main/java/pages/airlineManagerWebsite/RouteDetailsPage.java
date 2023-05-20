package pages.airlineManagerWebsite;

import com.codeborne.selenide.Condition;

import java.time.Duration;
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

            //You can uncomment this part if you want to perform an audit of specific route
            // can be reloaded on audit. should be checked

            RoutePricePage action = new RoutePricePage();
            action.auditProcedure();

            String ecoDemand = $x(economyDemand).shouldBe(Condition.visible).getText().trim().replaceAll("\\D+", "");
            String businDemand = $x(businessDemand).shouldBe(Condition.visible).getText().trim().replaceAll("\\D+", "");
            String frtDemand = $x(firstDemand).shouldBe(Condition.visible).getText().trim().replaceAll("\\D+", "");
            String cargDemand = $x(cargoDemand).shouldBe(Condition.visible).getText().trim().replaceAll("\\D+", "");

            String ecoPrice = $x(economyPrice).shouldBe(Condition.visible).getText().trim().replaceAll("\\D+", "");
            String businPrice = $x(businessPrice).shouldBe(Condition.visible).getText().trim().replaceAll("\\D+", "");
            String frtPrice = $x(firstPrice).shouldBe(Condition.visible).getText().trim().replaceAll("\\D+", "");
            String cargPrice = $x(cargoPrice).shouldBe(Condition.visible).getText().trim().replaceAll("\\D+", "");

            rows.add(List.of(name, ecoDemand, businDemand, frtDemand, cargDemand, ecoPrice, businPrice, frtPrice, cargPrice));
        }
    }

    public void collectAirprotNamesAndLinksToRoutePrice(Map<String, String> nameOfAirportAndLink) {
        String portName = $x(airportName).shouldBe(Condition.visible).getText().trim().toUpperCase().substring(0, 3);
        String link = $x(linkToPricesPage).shouldBe(Condition.visible, Duration.ofSeconds(30)).getAttribute("href");
        nameOfAirportAndLink.put(portName, link);
    }
}