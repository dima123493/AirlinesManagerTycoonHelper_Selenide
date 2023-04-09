import infoFromFiles.TableRow;
import pages.airlineManagerWebsite.LoginPage;
import pages.airlineManagerWebsite.RouteDetailsPage;
import pages.airlineManagerWebsite.RouteListPage;
import pages.airlineManagerWebsite.RoutePricePage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import static com.codeborne.selenide.Selenide.*;
import static java.lang.System.getProperty;

/*record AeroportToPriceRef (
    String aeroportName,
    String hrefToPrice
){}*/

public class AnalyticsGathering {
    public static final String URL = getProperty("airlines-manager-base-url");

    public static void main(String[] args) {
        int totalLinksNumber;
        loginOnWebsite(getProperty("login-email"), getProperty("login-password"));

        RouteListPage routeListPage = new RouteListPage();
        open(URL + "network/showhub/" + getProperty("hub-link-id") + "/linelist");
        totalLinksNumber = routeListPage.countLinks();
        routeListPage.sortByRoutLength();

        List<String> routeInfoHrefs = new ArrayList<>();
        routeListPage.getHrefsFromRouteList(routeInfoHrefs);
        routeListPage.printCollectedLinksAndCheckAmountOfItems(routeInfoHrefs, totalLinksNumber);

        RecordFile info = new RecordFile();

        List<String> listOfKms = new ArrayList<>();
        routeListPage.distanceOfTheRoutes(listOfKms);

        info.writeValuesForEachRoute(listOfKms,getProperty("file-name-for-gathered-distance"));

        Map<String, String> nameOfAirportAndLink = new LinkedHashMap<>();

        List<List<String>> rows = new ArrayList<>();

        RouteDetailsPage routePage = new RouteDetailsPage();
       // List<AeroportToPriceRef> keyvals = new ArrayList<>();
        for (String href : routeInfoHrefs) {
            open(href);
            routePage.collectAirPotNamesAndLinks(nameOfAirportAndLink);
            //SelenideElement link = $x("//*[@id=\"showLine\"]/div[5]/div/a");

            //String key = $x(airportName).getText().trim().toUpperCase().substring(0, 3);
           // String value = $x(linkToPricesPage).getAttribute("href");
           // keyvals.add(new AeroportToPriceRef(key, value));
            //nameOfAirportAndLink.put(key, value);
        }
        //System.out.println("==========");
       // System.out.println(keyvals);
      //  System.out.println("==========");

        List<String> linksToPricePage = new ArrayList<>(nameOfAirportAndLink.values());
        info.writeValuesForEachRoute(linksToPricePage, getProperty("file-name-for-gathered-links-to-price-page"));
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

class ManagePrices {
    public static final String URL = getProperty("airlines-manager-base-url");
    static final int CRJ_1000_MAX_DISTANCE = 3129;//TODO change this!!!

    public static void main(String[] args) throws IOException {
        var file1 = Path.of("D:\\AirlinesManager\\LinksToPricePageForEachRoute.txt");
        var file2 = Path.of("D:\\AirlinesManager\\DistanceInKm.txt");
        List<String> pricePages = Files.readAllLines(file1);
        List<String> kilometers = Files.readAllLines(file2);
        List<TableRow> fileInfo = ReadFile.readFile();
        if (!(pricePages.size() == kilometers.size() && kilometers.size() == fileInfo.size())) {
            throw new IllegalStateException("Number of rows in files do not match");
        }

        LoginPage mainPage = new LoginPage();
        try {
            open(URL);
            mainPage.login(getProperty("login-email"), getProperty("login-password"));
        } catch (Exception e) {
            System.out.println("Email or password is incorrect!");
        }


        for (int i = 0; i < pricePages.size(); i++) {
            int kilometerage = Integer.parseInt(kilometers.get(i));
            var row = fileInfo.get(i);
            if (kilometerage < CRJ_1000_MAX_DISTANCE) {// TODO if ReadFromExcel...Class checks multiple planes - this "if" is useless
                open(pricePages.get(i));
                String economyPrice = row.economyPriceValue();
                String businessPrice = row.businessPriceValue();
                String firstPrice = row.firstPriceValue();
                String cargoPrice = row.cargoPriceValue();
                managePricesForRoute(economyPrice,businessPrice,firstPrice,cargoPrice);
            }

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