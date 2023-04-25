import filesManagment.ReadFile;
import filesManagment.RecordFile;
import infoFromFiles.ReadGatheredResultFromPerfectSeatWebsiteFile;
import pages.airlineManagerWebsite.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import static com.codeborne.selenide.Selenide.closeWebDriver;
import static com.codeborne.selenide.Selenide.open;
import static propertyLoader.AirlinesProperties.getProperty;

/*record AeroportToPriceRef (
    String aeroportName,
    String hrefToPrice
){}*/

public class RouteDetailsGathering {
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

        info.writeValuesForEachRoute(listOfKms, getProperty("file-name-for-gathered-distance"));

        Map<String, String> nameOfAirportAndLinkToRoutePrices = new LinkedHashMap<>();

        List<List<String>> rows = new ArrayList<>();

        RouteDetailsPage routePage = new RouteDetailsPage();
        // List<AeroportToPriceRef> keyvals = new ArrayList<>();
        for (String href : routeInfoHrefs) {
            open(href);
            routePage.collectAirprotNamesAndLinksToRoutePrice(nameOfAirportAndLinkToRoutePrices);
            //SelenideElement link = $x("//*[@id=\"showLine\"]/div[5]/div/a");

            //String key = $x(airportName).getText().trim().toUpperCase().substring(0, 3);
            // String value = $x(linkToPricesPage).getAttribute("href");
            // keyvals.add(new AeroportToPriceRef(key, value));
            //nameOfAirportAndLinkToRoutePrices.put(key, value);
        }
        //System.out.println("==========");
        // System.out.println(keyvals);
        //  System.out.println("==========");

        System.out.println(nameOfAirportAndLinkToRoutePrices);
        System.out.println(nameOfAirportAndLinkToRoutePrices.size());

        List<String> linksToPricePage = new ArrayList<>(nameOfAirportAndLinkToRoutePrices.values());
        info.writeValuesForEachRoute(linksToPricePage, getProperty("file-name-for-gathered-links-to-price-page"));
        System.out.println(linksToPricePage);

        routePage.collectDemandValueAndPrices(nameOfAirportAndLinkToRoutePrices, rows);
/*        for (Map.Entry<String, String> entry : nameOfAirportAndLinkToRoutePrices.entrySet()) {
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

class ManageRoutePrices {

    public static void main(String[] args) throws IOException {
        var file1 = Path.of(getProperty("file-path-for-gathered-links-to-price-page"));
        var file2 = Path.of("file-path-for-gathered-distance");
        List<String> pricePages = Files.readAllLines(file1);
        List<String> kilometers = Files.readAllLines(file2);
        List<ReadGatheredResultFromPerfectSeatWebsiteFile> gatheredInfoFile = ReadFile.readGatheredResultFromPerfectSeatWebsiteFile();
        if (!(pricePages.size() == kilometers.size() && kilometers.size() == gatheredInfoFile.size())) {
            throw new IllegalStateException("Number of rows in files do not match");
        }
        loginOnWebsite(getProperty("login-email"), getProperty("login-password"));


        for (int i = 0; i < pricePages.size(); i++) {
            var row = gatheredInfoFile.get(i);
                open(pricePages.get(i));
                String economyPrice = row.economyPriceForRoute();
                String businessPrice = row.businessPriceForRoute();
                String firstPrice = row.firstPriceForRoute();
                String cargoPrice = row.cargoPriceForRoute();
                managePricesForRoute(economyPrice, businessPrice, firstPrice, cargoPrice);
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

    public static void loginOnWebsite(String email, String password) {
        LoginPage mainPage = new LoginPage();
        try {
            open(getProperty("airlines-manager-base-url"));
            mainPage.login(email, password);
        } catch (Exception e) {
            System.out.println("Email or password is incorrect!");
        }
    }

/*    public static Map<String, Integer> combineTwoListsIntoHashMap(List<String> pricePages, List<String> kilometers) {
        Map<String, Integer> resultMap = new LinkedHashMap<>();
        for (int i = 0; i < pricePages.size(); i++) {
            resultMap.put(pricePages.get(i), Integer.valueOf(kilometers.get(i)));
        }
        return resultMap;
    }*/
}



class PlanesManagement {
    public static void main(String[] args) {
        loginOnWebsite(getProperty("login-email"), getProperty("login-password"));
        PlanesListPage planeList = new PlanesListPage();
        PlaneDetailsPage planeDetails = new PlaneDetailsPage();
        open("https://tycoon.airlines-manager.com/aircraft");
        int totalNumberOfPlaneListPages = planeList.getTotalPageNumberAndReturnToFirstPage();
        List<String> linksToPlaneDetailsPage = new LinkedList<>();
        //List<String> infoAboutPlane = new LinkedList<>();
        List<List<String>> infoAboutPlane = new ArrayList<>();
        gatherLinksToAllThePlanesDetails(planeList, totalNumberOfPlaneListPages, linksToPlaneDetailsPage);
        gatherInfoAboutPlanesAndPerformCheck(planeDetails, linksToPlaneDetailsPage, infoAboutPlane);
    }

    public static void gatherInfoAboutPlanesAndPerformCheck(PlaneDetailsPage planeDetails, List<String> linksToPlaneDetailsPage, List<List<String>> infoAboutPlane) {
        for (String url : linksToPlaneDetailsPage) {
            open(url);
            planeDetails.getInfoAboutPlane(infoAboutPlane);
            planeDetails.makeCheckDecision();
        }
    }

    public static void gatherLinksToAllThePlanesDetails(PlanesListPage planeList, int totalNumberOfPlaneListPages, List<String> linksToPlaneDetailsPage) {
        for (int i = 1; i <= totalNumberOfPlaneListPages; i++) {
            planeList.getHrefsFromPlanesList(linksToPlaneDetailsPage);
            if (i >= 1 && i < totalNumberOfPlaneListPages) {
                planeList.goToNextPage();
            }
        }
    }

    public static void loginOnWebsite(String email, String password) {
        LoginPage mainPage = new LoginPage();
        try {
            open(getProperty("airlines-manager-base-url"));
            mainPage.login(email, password);
        } catch (Exception e) {
            System.out.println("Email or password is incorrect!");
        }
    }
}