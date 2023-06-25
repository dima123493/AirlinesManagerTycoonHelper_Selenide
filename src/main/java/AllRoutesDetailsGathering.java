import filesManagment.ReadFile;
import filesManagment.WriteFile;
import infoFromFiles.FlightLog;
import infoFromFiles.GatheredInfoTableRow;
import infoFromFiles.ReadGatheredResultFromPerfectSeatWebsiteFile;
import infoFromFiles.SeatConfiguration;
import pages.airlineManagerWebsite.*;
import timeOptimizer.FlightId;
import timeOptimizer.FlightInfo;
import timeOptimizer.PlanePool;

import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

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
        List<List<String>> rowsWithInfoForPerfectSeatFinder = new LinkedList<>();

        loginOnWebsite(getProperty("login-email"), getProperty("login-password"));
        open(URL + "/network/showhub/" + getProperty("hub-link-id") + "/linelist");
        routeList.sortByRoutLength();
        routeList.getHrefsFromRouteListPage(routeInfoHrefs);
        routeList.distanceOfTheRoutes(listOfKms);

        collectsAirportNameAndLinkToRoutePrice(routeDetails, routeInfoHrefs, nameOfAirportAndLinkToRoutePrices);

        collectsDemandValueAndPricesFromRoutePricePage(nameOfAirportAndLinkToRoutePrices, routeDetails, rowsWithInfoForPerfectSeatFinder);

        for (int i = 0; i < rowsWithInfoForPerfectSeatFinder.size(); i++) {
            rowsWithInfoForPerfectSeatFinder.get(i).add(listOfKms.get(i));
        }

        WriteFile.writeInfoFromInfoPage(rowsWithInfoForPerfectSeatFinder, "AM_AllRoutesInfo");

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

    public static void collectsAirportNameAndLinkToRoutePrice(RouteDetailsPage routeDetails, List<String> routeInfoHrefs, Map<String, String> nameOfAirportAndLinkToRoutePrices) {
        for (String href : routeInfoHrefs) {
            open(href);
            routeDetails.collectAirprotNamesAndLinksToRoutePrice(nameOfAirportAndLinkToRoutePrices);
        }
    }

    public static void collectsDemandValueAndPricesFromRoutePricePage(Map<String, String> nameOfAirportAndLinkToRoutePrices, RouteDetailsPage routeDetails, List<List<String>> rowsWithInfoForPerfectSeatFinder) {
        for (Map.Entry<String, String> entry : nameOfAirportAndLinkToRoutePrices.entrySet()) {
            String nameOfAirport = entry.getKey();
            open(entry.getValue());
            routeDetails.collectDemandValueAndPrices(nameOfAirport, rowsWithInfoForPerfectSeatFinder);
        }
    }

}

class NewOrMissedRoutesDetailsGathering {
    public static final String URL = getProperty("airlines-manager-base-url");

    public static void main(String[] args) {
        var dataFromExcel = ReadFile.readGatheredInfoFile("AM_AllRoutesInfo");
        Map<String, String> allRoutes = new LinkedHashMap<>();
        List<List<String>> rowsWithInfoForPerfectSeatFinder = new LinkedList<>();
        List<String> listOfKms = new LinkedList<>();
        RouteListPage routeListPage = new RouteListPage();
        RouteDetailsPage routeDetailsPage = new RouteDetailsPage();

        AllRoutesDetailsGathering.loginOnWebsite(getProperty("login-email"), getProperty("login-password"));

        open(URL + "/network/showhub/" + getProperty("hub-link-id") + "/linelist");

        routeListPage.collectAirportNamesAndTheirLinks(allRoutes);
        //analyzes rows from Excel and gathered airport names (and links) from website, excludes existing airports from files
        var allDestinationsFromExcel = dataFromExcel.stream()
                .map(GatheredInfoTableRow::destinationHubValue)
                .collect(Collectors.toSet());
        var allRoutesFiltered = allRoutes.entrySet().stream()
                .filter(e -> !allDestinationsFromExcel.contains(e.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        //gathers information about routes which were not in the file yet
        for (Map.Entry<String, String> data : allRoutesFiltered.entrySet()) {
            open(URL + data.getValue());
            routeDetailsPage.getDistance(listOfKms);
            open(routeDetailsPage.collectLinkToRoutePrice());
            routeDetailsPage.collectDemandValueAndPrices(data.getKey(), rowsWithInfoForPerfectSeatFinder);
        }

        for (int i = 0; i < rowsWithInfoForPerfectSeatFinder.size(); i++) {
            rowsWithInfoForPerfectSeatFinder.get(i).add(listOfKms.get(i));
        }

        WriteFile.writeInfoFromInfoPage(rowsWithInfoForPerfectSeatFinder, "AM_NewMissedRoutesInfo");
    }
}

class ManageAllRoutePrices {
    public static final String URL = getProperty("airlines-manager-base-url");

    public static void main(String[] args) {
        List<ReadGatheredResultFromPerfectSeatWebsiteFile> gatheredInfoFile = ReadFile.readGatheredResultFromPerfectSeatWebsiteFile("PSF_AllRoutesInfo");

        AllRoutesDetailsGathering.loginOnWebsite(getProperty("login-email"), getProperty("login-password"));
        open(URL + "/network/showhub/" + getProperty("hub-link-id") + "/linelist");

        RouteListPage routeListPage = new RouteListPage();

        Map<String, String> allRoutes = new LinkedHashMap<>();

        routeListPage.collectAirportNamesAndTheirLinks(allRoutes);

        getValueFromFileAndApplyPriceForRoute(gatheredInfoFile, allRoutes);
    }

    public static void getValueFromFileAndApplyPriceForRoute(List<ReadGatheredResultFromPerfectSeatWebsiteFile> gatheredInfoFile, Map<String, String> allRoutes) {
        RouteDetailsPage routeDetailsPage = new RouteDetailsPage();
        RoutePricePage managePricesForRoute = new RoutePricePage();

        for (int i = 0; i <= gatheredInfoFile.size(); i++) {
            var row = gatheredInfoFile.get(i);
            if (allRoutes.containsKey(row.airportName())) {
                open(URL + allRoutes.get(row.airportName()));
                open(routeDetailsPage.collectLinkToRoutePrice());
                String economyPrice = row.economyPriceForRoute();
                String businessPrice = row.businessPriceForRoute();
                String firstPrice = row.firstPriceForRoute();
                String cargoPrice = row.cargoPriceForRoute();
                managePricesForRoute.applyNewPrices(economyPrice, businessPrice, firstPrice, cargoPrice);
            }
        }
    }

}

class ManageNewRoutePrices {

    public static void main(String[] args) {
        List<ReadGatheredResultFromPerfectSeatWebsiteFile> gatheredInfoFile = ReadFile.readGatheredResultFromPerfectSeatWebsiteFile("PSF_NewMissedRoutesInfo");

        AllRoutesDetailsGathering.loginOnWebsite(getProperty("login-email"), getProperty("login-password"));

        RouteListPage routeListPage = new RouteListPage();

        Map<String, String> allRoutes = new LinkedHashMap<>();

        routeListPage.collectAirportNamesAndTheirLinks(allRoutes);

        ManageAllRoutePrices.getValueFromFileAndApplyPriceForRoute(gatheredInfoFile, allRoutes);
    }
}

class RouteTimeTableOptimizer {

    public static void main(String[] args) {
        var dataFromExcel = ReadFile.readGatheredResultFromPerfectSeatWebsiteFile("PSF_NewMissedRoutesInfo");// "PSF_NewMissedRoutesInfo" "PSF_AllRoutesInfo"

        Map<SeatConfiguration, Map<FlightId, FlightInfo>> configuration = new HashMap<>();
        for (var row : dataFromExcel) {
            var seatConfig = new SeatConfiguration(
                    parseInt(row.economySeatsAmountNeeded()),
                    parseInt(row.businessSeatsAmountNeeded()),
                    parseInt(row.firstSeatsAmountNeeded()),
                    parseInt(row.cargoSeatsAmountNeeded()));
            FlightId flightId = new FlightId(row.airportName());
            FlightInfo flightInfo = new FlightInfo(
                    parseInt(row.numberOfWavesNeeded()),
                    convertToDuration(row.timeTakenForOneWave())
            );
            configuration.merge(seatConfig, new HashMap<>(Map.of(flightId, flightInfo)),
                    (old, el) -> {
                        old.putAll(el);
                        return old;
                    });
        }


        List<FlightLog> log = new ArrayList<>();
        List<List<String>> timeTableOptimizedToRecord = new ArrayList<>();

        // Map<SeatConfiguration, PlanePool> planePools = new HashMap<>();
        for (var configurationMapEntry : configuration.entrySet()) {
//            var minimalTime = configurationMapEntry.getValue().values().stream()
//                    .map(FlightInfo::getDuration)
//                    .min(Comparator.naturalOrder());
            PlanePool planePool = new PlanePool();
            // planePools.put(configurationMapEntry.getKey(), planePool);
            for (var flight : configurationMapEntry.getValue().entrySet()) {

                FlightInfo info = flight.getValue();
                while (info.getNumberOfWaves() > 0) {
                    planePool.performOneFlight(info.getDuration(), flight.getKey(), log);
                    info.decrementNumberOfWaves();
                }
            }
        }

        /*for(Map.Entry<SeatConfiguration, PlanePool> info : planePools.entrySet()){
            System.out.println(info.getKey());
            System.out.println(info.getValue().toString());
        }*/

        for (FlightLog info : log) {
            String id = String.valueOf(info.planeId());
            String name = info.airportName();
            String timeTakn = info.timeTaken().toHoursPart() + ":" + info.timeTaken().toMinutesPart();
            String timeLft = info.timeLeft().toHoursPart() + ":" + info.timeLeft().toMinutesPart();
            timeTableOptimizedToRecord.add(List.of(id, name, timeTakn, timeLft));
        }

        WriteFile.writeTimetableOptimizationCalculations(timeTableOptimizedToRecord, "Schedule");

    }

    public static Duration convertToDuration(String time) {
        String[] tokens = time.split(":");
        int hours = Integer.parseInt(tokens[0]);
        int minutes = Integer.parseInt(tokens[1]);
        Duration hour = Duration.ofHours(hours);
        Duration minute = Duration.ofMinutes(minutes);
        return hour.plus(minute);
    }

}

class PlanesManagement {
    public static void main(String[] args) {
        AllRoutesDetailsGathering.loginOnWebsite(getProperty("login-email"), getProperty("login-password"));

        SchedulePage planesOnTheRoute = new SchedulePage();
        PlaneDetailsPage planeDetails = new PlaneDetailsPage();
        PlaneConfigurationPage planeConfiguration = new PlaneConfigurationPage();

        List<List<String>> collectedOnRoutePlanesData = new ArrayList<>();
        List<ReadGatheredResultFromPerfectSeatWebsiteFile> gatheredInfoFile = ReadFile.readGatheredResultFromPerfectSeatWebsiteFile("PSF_AllRoutesInfo");

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

        WriteFile.writeInfoFromPerfectSeatFinderWebsite(modifiedDataList, "PSF_DataMinusActual");

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

}//TODO lost its sense due to RouteTimeTableOptimizer