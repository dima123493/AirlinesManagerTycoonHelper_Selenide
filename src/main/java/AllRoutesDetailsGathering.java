import filesManagment.ReadFile;
import filesManagment.WriteFile;
import infoFromFiles.ReadGatheredResultFromPerfectSeatWebsiteFile;
import pages.airlineManagerWebsite.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import static com.codeborne.selenide.Selenide.*;
import static java.lang.Integer.parseInt;
import static propertyLoader.AirlinesProperties.getProperty;

public class AllRoutesDetailsGathering {
    public static final String URL = getProperty("airlines-manager-base-url");

    public static void main(String[] args) {
        RouteListPage routeList = new RouteListPage();
        RouteDetailsPage routeDetails = new RouteDetailsPage();

        List<String> listOfKms = new LinkedList<>();
        List<String> routeInfoHrefs = new LinkedList<>();
        Map<String, String> nameOfAirportAndLinkToRoutePrices = new LinkedHashMap<>();
        List<String> linksToPricePage = new LinkedList<>(nameOfAirportAndLinkToRoutePrices.values());
        List<List<String>> rowsWithInfoForPerfectSeatFinder = new LinkedList<>();

        loginOnWebsite(getProperty("login-email"), getProperty("login-password"));
        open(URL + "/network/showhub/" + getProperty("hub-link-id") + "/linelist");

        routeList.sortByRoutLength();
        routeList.getHrefsFromRouteListPage(routeInfoHrefs);
        routeList.distanceOfTheRoutes(listOfKms);

        //this opens links of route details and collects Airport Name and link to route price
        for (String href : routeInfoHrefs) {
            open(href);
            routeDetails.collectAirprotNamesAndLinksToRoutePrice(nameOfAirportAndLinkToRoutePrices);
        }

        //this collects Demand Value And Prices from route price page
        for (Map.Entry<String, String> entry : nameOfAirportAndLinkToRoutePrices.entrySet()) {
            String nameOfAirport = entry.getKey();
            open(entry.getValue());
            routeDetails.collectDemandValueAndPrices(nameOfAirport, rowsWithInfoForPerfectSeatFinder);
        }

        System.out.println(linksToPricePage);
        System.out.println(nameOfAirportAndLinkToRoutePrices.values());

        WriteFile.writeValuesIntoTextFile(listOfKms, getProperty("file-name-for-gathered-distance"));//this can be removed if KMs is written into Excel
        for (int i = 0; i < rowsWithInfoForPerfectSeatFinder.size(); i++) {
            rowsWithInfoForPerfectSeatFinder.get(i).add(listOfKms.get(i));
        }

        WriteFile.writeValuesIntoTextFile(linksToPricePage, getProperty("file-name-for-gathered-links-to-price-page"));
        WriteFile.writeInfoFromInfoPage(rowsWithInfoForPerfectSeatFinder, "AirlinesManagerAllRoutesInfo");

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
        Path linksToPricePageFile = Path.of(getProperty("file-path-for-gathered-links-to-price-page"));
        Path distanceForEachRoute = Path.of(getProperty("file-path-for-gathered-distance"));//this can be removed if new version works
        List<String> pricePages = Files.readAllLines(linksToPricePageFile);
        List<String> kilometers = Files.readAllLines(distanceForEachRoute);//this can be removed if new version works
        List<ReadGatheredResultFromPerfectSeatWebsiteFile> gatheredInfoFile = ReadFile.readGatheredResultFromPerfectSeatWebsiteFile("PerfectSeatFinderAllRoutes");
        if (!(pricePages.size() == kilometers.size() && kilometers.size() == gatheredInfoFile.size())) {//modify this if new version works
            throw new IllegalStateException("Number of rows in files do not match! Important info is missing!");
        }

        AllRoutesDetailsGathering.loginOnWebsite(getProperty("login-email"), getProperty("login-password"));

        RoutePricePage managePricesForRoute = new RoutePricePage();

        for (int i = 0; i < pricePages.size(); i++) {
            var row = gatheredInfoFile.get(i);
            open(pricePages.get(i));
            String economyPrice = row.economyPriceForRoute();
            String businessPrice = row.businessPriceForRoute();
            String firstPrice = row.firstPriceForRoute();
            String cargoPrice = row.cargoPriceForRoute();
            managePricesForRoute.applyNewPrices(economyPrice, businessPrice, firstPrice, cargoPrice);
        }

    }

}

class PlanesManagement {
    public static void main(String[] args) {
        AllRoutesDetailsGathering.loginOnWebsite(getProperty("login-email"), getProperty("login-password"));

        SchedulePage planesOnTheRoute = new SchedulePage();
        PlaneDetailsPage planeDetails = new PlaneDetailsPage();
        PlaneConfigurationPage planeConfiguration = new PlaneConfigurationPage();

        List<List<String>> collectedOnRoutePlanesData = new ArrayList<>();
        List<ReadGatheredResultFromPerfectSeatWebsiteFile> gatheredInfoFile = ReadFile.readGatheredResultFromPerfectSeatWebsiteFile("PerfectSeatFinderAllRoutes");

        open("https://tycoon.airlines-manager.com/network/generalplanning/1");
        planesOnTheRoute.collectInfoAboutPlanesAndWavesOnTheRoute(collectedOnRoutePlanesData);

        //this part reconfigures planes which are already on the route using data from Excel
        for (List<String> listRecord : collectedOnRoutePlanesData) {
            open("https://tycoon.airlines-manager.com/aircraft/show/" + listRecord.get(2));// record.get(2) - gets aircraft id
            //find by name and extract data from that row
            gatheredInfoFile.stream()
                    .filter(excelRecord -> planeDetails.getAircraftName().contains(excelRecord.airportName()))
                    .findFirst()
                    .ifPresent(rowValues -> {
                        open("https://tycoon.airlines-manager.com/aircraft/show/" + listRecord.get(2) + "/reconfigure");
                        if (!planeConfiguration.isPageLoaded()) {
                            refresh();
                            open("https://tycoon.airlines-manager.com/aircraft/show/" + listRecord.get(2) + "/reconfigure");
                        }
                        String economySeats = rowValues.economySeatsAmountNeeded();
                        String businessSeats = rowValues.businessSeatsAmountNeeded();
                        String firstSeats = rowValues.firstSeatsAmountNeeded();
                        String cargoSeats = rowValues.cargoSeatsAmountNeeded();
                        var isReconfigured = planeConfiguration.configureAircraftSeats(economySeats, businessSeats, firstSeats, cargoSeats);
                        if (isReconfigured == false) {//TODO should be TRUE
                            int result = parseInt(rowValues.numberOfWavesNeeded()) - parseInt(listRecord.get(3));
                            gatheredInfoFile.set(parseInt(rowValues.numberOfWavesNeeded()),
                                    rowValues.withNumberOfWavesNeeded(String.valueOf(result))
                            );
                        }
                    });
        }

        List<List<String>> modifiedDataList = new ArrayList<>();
        for (ReadGatheredResultFromPerfectSeatWebsiteFile rowValues : gatheredInfoFile) {
            modifiedDataList.add(List.of(
                    rowValues.numberOfWavesNeeded(),
                    rowValues.airportName(),
                    rowValues.economySeatsAmountNeeded(),
                    rowValues.businessSeatsAmountNeeded(),
                    rowValues.firstSeatsAmountNeeded(),
                    rowValues.cargoSeatsAmountNeeded(),
                    rowValues.economyPriceForRoute(),
                    rowValues.businessPriceForRoute(),
                    rowValues.firstPriceForRoute(),
                    rowValues.cargoPriceForRoute()));
        }

        WriteFile.writeInfoFromPerfectSeatFinderWebsite(modifiedDataList, "PerfectSeatFinderDataMinusActual");

        /*            for (ReadGatheredResultFromPerfectSeatWebsiteFile rowValues : gatheredInfoFile) {
                if (planeDetails.getAircraftName().contains(rowValues.airportName())) {//flightname
                    open(planeDetails.getConfigurationLink());
                    String economySeats = rowValues.economySeatsAmountNeeded();
                    String businessSeats = rowValues.businessSeatsAmountNeeded();
                    String firstSeats = rowValues.firstSeatsAmountNeeded();
                    String cargoSeats = rowValues.cargoSeatsAmountNeeded();
                    var reconfiguration = planeConfiguration.configureAircraftSeats(economySeats, businessSeats, firstSeats, cargoSeats);//isrecongigured
                    if (false && reconfiguration) {
                        int result = parseInt(rowValues.numberOfWavesNeeded()) - parseInt(listRecord.get(3));
                        gatheredInfoFile.set(parseInt(rowValues.numberOfWavesNeeded()),
                                rowValues.withNumberOfWavesNeeded(String.valueOf(result))
                        );
                    }
                    back();
                    break;
                }
            }*/
    }

    public static void performCheck(PlaneDetailsPage planeDetails, List<String> linksToPlaneDetailsPage) {
        for (String url : linksToPlaneDetailsPage) {
            open(url);
            //planeDetails.getInfoAboutPlane(infoAboutPlane);
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

}

class NewRoutesDetailsGathering {
    public static final String URL = getProperty("airlines-manager-base-url");

    public static void main(String[] args) throws IOException {
        var routeNameToAnalyze = Path.of(getProperty("file-path-for-new-routes-names-to-analyze"));
        List<String> routeName = Files.readAllLines(routeNameToAnalyze);
        List<List<String>> rowsWithInfoForPerfectSeatFinder = new LinkedList<>();
        List<String> listOfKms = new LinkedList<>();
        RouteListPage routeListPage = new RouteListPage();
        RouteDetailsPage routeDetailsPagePage = new RouteDetailsPage();

        AllRoutesDetailsGathering.loginOnWebsite(getProperty("login-email"), getProperty("login-password"));

        for (String airportCode : routeName) {
            open(URL + "/network/showhub/" + getProperty("hub-link-id") + "/linelist");
            String endpoint = routeListPage.getEndpointFromRouteList(airportCode);
            System.out.println(endpoint);
            open(URL + endpoint);
            routeDetailsPagePage.getDistance(listOfKms);
            open(routeDetailsPagePage.collectLinkToRoutePrice());
            routeDetailsPagePage.collectDemandValueAndPrices(airportCode, rowsWithInfoForPerfectSeatFinder);
        }

        for (int i = 0; i < rowsWithInfoForPerfectSeatFinder.size(); i++) {
            rowsWithInfoForPerfectSeatFinder.get(i).add(listOfKms.get(i));
        }

        WriteFile.writeInfoFromInfoPage(rowsWithInfoForPerfectSeatFinder,"AirlinesManagerNewRoutesInfo");
    }
}