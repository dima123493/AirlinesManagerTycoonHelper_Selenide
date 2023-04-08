import pages.LoginPage;
import pages.RouteDetailsPage;
import pages.RouteListPage;
import pages.RoutePricePage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import static com.codeborne.selenide.Selenide.*;
import static pages.RouteDetailsPage.airportName;
import static pages.RouteDetailsPage.linkToPricesPage;

record AeroportToPriceRef (
    String aeroportName,
    String hrefToPrice
){}

public class AnalyticsGathering {
    public static final String URL = "https://tycoon.airlines-manager.com/";

    public static void main(String[] args) {
        int totalLinksNumber;
        loginOnWebsite("fokist@outlook.com", "9#T253yT");

        RouteListPage routeListPage = new RouteListPage();
        open(URL + "network/showhub/5702176/linelist");
        totalLinksNumber = routeListPage.countLinks();
        routeListPage.sortByRoutLength();

        List<String> routeInfoHrefs = new ArrayList<>();
        routeListPage.getHrefsFromRouteList(routeInfoHrefs);
        routeListPage.printCollectedLinksAndCheckAmountOfItems(routeInfoHrefs, totalLinksNumber);

        RecordResult info = new RecordResult();

        List<String> listOfKms = new ArrayList<>();
        routeListPage.distanceOfTheRoutes(listOfKms);

        info.writeValuesForEachRoute(listOfKms,"DistanceInKm");

/*        String airportName = "//*[@id=\"box2\"]/li[4]/b";

        String economyDemand = "//*[@id=\"marketing_linePricing\"]/div[3]/div[1]/div[3]/b";
        String businessDemand = "//*[@id=\"marketing_linePricing\"]/div[3]/div[2]/div[3]/b";
        String firstDemand = "//*[@id=\"marketing_linePricing\"]/div[3]/div[3]/div[3]/b";
        String cargoDemand = "//*[@id=\"marketing_linePricing\"]/div[3]/div[4]/div[3]/b";

        String economyPrice = "//*[@id=\"marketing_linePricing\"]/div[3]/div[1]/div[2]/b";
        String businessPrice = "//*[@id=\"marketing_linePricing\"]/div[3]/div[2]/div[2]/b";
        String firstPrice = "//*[@id=\"marketing_linePricing\"]/div[3]/div[3]/div[2]/b";
        String cargoPrice = "//*[@id=\"marketing_linePricing\"]/div[3]/div[4]/div[2]/b";*/

        Map<String, String> nameOfAirportAndLink = new LinkedHashMap<>();

        List<List<String>> rows = new ArrayList<>();

        RouteDetailsPage routePage = new RouteDetailsPage();
       // List<AeroportToPriceRef> keyvals = new ArrayList<>();
        for (String href : routeInfoHrefs) {
            open(href);
            //SelenideElement link = $x("//*[@id=\"showLine\"]/div[5]/div/a");
            String key = $x(airportName).getText().trim().toUpperCase().substring(0, 3);
            String value = linkToPricesPage.getAttribute("href");
           // keyvals.add(new AeroportToPriceRef(key, value));
            nameOfAirportAndLink.put(key, value);
        }
        //System.out.println("==========");
       // System.out.println(keyvals);
      //  System.out.println("==========");

        List<String> linksToPricePage = new ArrayList<>();
        linksToPricePage.addAll(nameOfAirportAndLink.values());
        info.writeValuesForEachRoute(linksToPricePage, "LinksToPricePageForEachRoute");
        System.out.println(linksToPricePage);

        routePage.collectDemandValueAndPrices(nameOfAirportAndLink, rows);
/*        for (Map.Entry<String, String> entry : nameOfAirportAndLink.entrySet()) {
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

        }*/

        info.writeInfoFromInfoPage(rows);
        closeWebDriver();
    }

    public static void loginOnWebsite(String email, String password) {
        LoginPage mainPage = new LoginPage();
        try {
            open(URL);
            mainPage.login(email, password);
        } catch (Exception e) {
            System.out.println("Email or password is incorrect!");
        }
    }

}

class ManagePricesPage {
    static final int CRJ_1000_MAX_DISTANCE = 3129;

    public static void main(String[] args) throws IOException {
        var file1 = Path.of("D:\\LinksToPricePageForEachRoute.txt");
        var file2 = Path.of("D:\\DistanceInKm.txt");
        List<String> pricePages = Files.readAllLines(file1);
        List<String> kilometers = Files.readAllLines(file2);
        List<TableRow> fileInfo = ReadRecords.readFile();
        if (!(pricePages.size() == kilometers.size() && kilometers.size() == fileInfo.size())) {
            throw new IllegalStateException("Number of rows do not match");
        }
        for (int i = 0; i < pricePages.size(); i++) {
            var kilometerage = Integer.parseInt(kilometers.get(i));
            var row = fileInfo.get(i);
            if (kilometerage > CRJ_1000_MAX_DISTANCE) {
                continue;
            }
            open(pricePages.get(i));
            String economyPrice = row.economyPriceValue();
            String businessPrice = row.businessPriceValue();
            String firstPrice = row.firstPriceValue();
            String cargoPrice = row.cargoPriceValue();
            managePricesForRoute(economyPrice,businessPrice,firstPrice,cargoPrice);
        }

//        var resultMap = combineTwoListsIntoHashMap(pricePages, kilometers);
//        for (Map.Entry<String, Integer> entry : resultMap.entrySet()) {
//            if (entry.getValue() <= crj1000MaxDistance) {
//                open(entry.getKey());
//                fileInfo.stream()
//                        .filter(row -> row.km().contentEquals())
//
//               if(fileInfo.contains(entry.getValue())){
//                   managePricesForRoute();
//               }
//            }
//        }

    }

    public static void managePricesForRoute(String economyPrice, String businessPrice, String firstPrice, String crgPrice) {
        RoutePricePage managePricesForRoute = new RoutePricePage();
        managePricesForRoute.applyNewPrices(economyPrice, businessPrice, firstPrice, crgPrice);
    }

/*    public static Map<String, Integer> combineTwoListsIntoHashMap(List<String> pricePages, List<String> kilometers) {
        Map<String, Integer> resultMap = new LinkedHashMap<>();
        for (int i = 0; i < pricePages.size(); i++) {
            resultMap.put(pricePages.get(i), Integer.valueOf(kilometers.get(i)));
        }
        return resultMap;
    }*/
}