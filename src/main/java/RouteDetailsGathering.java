import filesManagment.ReadFile;
import filesManagment.RecordFile;
import infoFromFiles.ReadGatheredResultFromPerfectSeatWebsiteFile;
import pages.airlineManagerWebsite.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import static com.codeborne.selenide.Selenide.*;
import static java.lang.Integer.parseInt;
import static propertyLoader.AirlinesProperties.getProperty;

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
        for (String href : routeInfoHrefs) {
            open(href);
            routePage.collectAirprotNamesAndLinksToRoutePrice(nameOfAirportAndLinkToRoutePrices);
        }

        System.out.println(nameOfAirportAndLinkToRoutePrices);
        System.out.println(nameOfAirportAndLinkToRoutePrices.size());

        List<String> linksToPricePage = new ArrayList<>(nameOfAirportAndLinkToRoutePrices.values());
        info.writeValuesForEachRoute(linksToPricePage, getProperty("file-name-for-gathered-links-to-price-page"));
        System.out.println(linksToPricePage);

        routePage.collectDemandValueAndPrices(nameOfAirportAndLinkToRoutePrices, rows);
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

}


class PlanesManagement {
    public static void main(String[] args) {
        loginOnWebsite(getProperty("login-email"), getProperty("login-password"));

        SchedulePage planesOmTheRoute = new SchedulePage();
        PlaneDetailsPage planeDetails = new PlaneDetailsPage();
        PlaneConfigurationPage planeConfiguration = new PlaneConfigurationPage();
        boolean reconfiguration;
        open("https://tycoon.airlines-manager.com/network/generalplanning/1");
        List<List<String>> collectedOnRoutePlanesData = new ArrayList<>();
        planesOmTheRoute.collectInfoAboutPlanesAndWavesOnTheRoute(collectedOnRoutePlanesData);

        //this part reconfigures planes which are already on the route using data from Excel
        List<ReadGatheredResultFromPerfectSeatWebsiteFile> gatheredInfoFile = ReadFile.readGatheredResultFromPerfectSeatWebsiteFile();
        for( List<String> record : collectedOnRoutePlanesData){
            open("https://tycoon.airlines-manager.com/aircraft/show/" + record.get(2));// record.get(2) - gets aircraft id

            //find by name and extract data from that row
            for(ReadGatheredResultFromPerfectSeatWebsiteFile rowValues : gatheredInfoFile) {
                    if (planeDetails.getAircraftName().contains(rowValues.airportName())) {
                        open(planeDetails.getConfigurationLink());
                        String economySeats = rowValues.economySeatsAmountNeeded();
                        String businessSeats = rowValues.businessSeatsAmountNeeded();
                        String firstSeats = rowValues.firstSeatsAmountNeeded();
                        String cargoSeats = rowValues.cargoSeatsAmountNeeded();
                        reconfiguration = planeConfiguration.configureAircraftSeats(economySeats, businessSeats, firstSeats, cargoSeats);
                        //TODO change amount of waves needed in the exel file on the amount of waves the plane does
                        if (reconfiguration == false) {//TODO should be TRUE
                           int result = parseInt(rowValues.numberOfWavesNeeded()) - parseInt(record.get(3));
                            gatheredInfoFile.set(parseInt(rowValues.numberOfWavesNeeded()),
                                    new ReadGatheredResultFromPerfectSeatWebsiteFile(
                                            String.valueOf(result),// if number is negative you have to delete that amount of routes - 1
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
                        back();
                    }
                }
            }

        //VALID CODE
/*        PlanesListPage planeList = new PlanesListPage();
        open("https://tycoon.airlines-manager.com/aircraft");
        int totalNumberOfPlaneListPages = planeList.getTotalPageNumberAndReturnToFirstPage();
        List<String> linksToPlaneDetailsPage = new LinkedList<>();
        List<List<String>> infoAboutPlane = new ArrayList<>();//can be performed in PlaneDetailsPage getInfoAboutPlane() method
        gatherLinksToAllThePlanesDetails(planeList, totalNumberOfPlaneListPages, linksToPlaneDetailsPage);
        performCheck(planeDetails, linksToPlaneDetailsPage);*/
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