import filesManagment.ReadFile;
import filesManagment.WriteFile;
import infoFromFiles.GatheredInfoTableRow;
import infoFromFiles.ReadGatheredResultFromPerfectSeatWebsiteFile;
import infoFromFiles.SeatConfiguration;
import pages.airlineManagerWebsite.*;
import timeOptimizer.FlightId;
import timeOptimizer.FlightInfo;
import timeOptimizer.PlanePool;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
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
        List<String> linksToPricePage = new LinkedList<>(nameOfAirportAndLinkToRoutePrices.values());
        List<List<String>> rowsWithInfoForPerfectSeatFinder = new LinkedList<>();

        loginOnWebsite(getProperty("login-email"), getProperty("login-password"));
        //Configuration.browserSize = "1920x1080";
        open(URL + "/network/showhub/" + getProperty("hub-link-id") + "/linelist");
        //zoom(0.5);
        routeList.sortByRoutLength();
        routeList.getHrefsFromRouteListPage(routeInfoHrefs);
        routeList.distanceOfTheRoutes(listOfKms);

        //this opens links of route details and collects Airport Name and link to route price
        for (String href : routeInfoHrefs) {
            open(href);
            routeDetails.collectAirprotNamesAndLinksToRoutePrice(nameOfAirportAndLinkToRoutePrices);
        }

        System.out.println(linksToPricePage);
        //this collects Demand Value And Prices from route price page
        for (Map.Entry<String, String> entry : nameOfAirportAndLinkToRoutePrices.entrySet()) {
            String nameOfAirport = entry.getKey();
            open(entry.getValue());
            routeDetails.collectDemandValueAndPrices(nameOfAirport, rowsWithInfoForPerfectSeatFinder);
        }
        System.out.println(nameOfAirportAndLinkToRoutePrices.values());

        //WriteFile.writeValuesIntoTextFile(listOfKms, getProperty("file-name-for-gathered-distance"));
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

class NewOrMissedRoutesDetailsGathering {
    public static final String URL = getProperty("airlines-manager-base-url");

    public static void main(String[] args) {
        /*var routeNameToAnalyze = Path.of(getProperty("file-path-for-new-routes-names-to-analyze"));
        List<String> routeName = Files.readAllLines(routeNameToAnalyze);*/
        var dataFromExcel = ReadFile.readGatheredInfoFile("AirlinesManagerAllRoutesInfo");
        Map<String, String> allRoutes = new LinkedHashMap<>();
        List<List<String>> rowsWithInfoForPerfectSeatFinder = new LinkedList<>();
        List<String> listOfKms = new LinkedList<>();
        RouteListPage routeListPage = new RouteListPage();
        RouteDetailsPage routeDetailsPage = new RouteDetailsPage();

        AllRoutesDetailsGathering.loginOnWebsite(getProperty("login-email"), getProperty("login-password"));

        open(URL + "/network/showhub/" + getProperty("hub-link-id") + "/linelist");

        routeListPage.collectAirportNamesAndTheirLinks(allRoutes);
        var allDestinationsFromExcel = dataFromExcel.stream()
                .map(GatheredInfoTableRow::destinationHubValue)
                .collect(Collectors.toSet());
        var allRoutesFiltered = allRoutes.entrySet().stream()
                .filter(e -> !allDestinationsFromExcel.contains(e.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        for (Map.Entry<String, String> data : allRoutesFiltered.entrySet()) {
            open(URL + data.getValue());
            routeDetailsPage.getDistance(listOfKms);
            open(routeDetailsPage.collectLinkToRoutePrice());
            routeDetailsPage.collectDemandValueAndPrices(data.getKey(), rowsWithInfoForPerfectSeatFinder);
        }

        for (int i = 0; i < rowsWithInfoForPerfectSeatFinder.size(); i++) {
            rowsWithInfoForPerfectSeatFinder.get(i).add(listOfKms.get(i));
        }

        WriteFile.writeInfoFromInfoPage(rowsWithInfoForPerfectSeatFinder, "AirlinesManagerNewRoutesInfo");
    }
}

class ManageRoutePrices {

    public static void main(String[] args) throws IOException {
        Path linksToPricePageFile = Path.of(getProperty("file-path-for-gathered-links-to-price-page"));
        //Path distanceForEachRoute = Path.of(getProperty("file-path-for-gathered-distance"));
        List<String> pricePages = Files.readAllLines(linksToPricePageFile);
        //List<String> kilometers = Files.readAllLines(distanceForEachRoute);
        List<ReadGatheredResultFromPerfectSeatWebsiteFile> gatheredInfoFile = ReadFile.readGatheredResultFromPerfectSeatWebsiteFile("PerfectSeatFinderAllRoutes");
        if (!(pricePages.size() == gatheredInfoFile.size())) {//== kilometers.size() && kilometers.size() ==
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

/*class PlanePool {
    List<Plane> pool = new ArrayList<>();

    void performOneFlight(Duration flightDuration) {
        var planeOptional = pool.stream()
                .filter(plane -> plane.getTime().compareTo(flightDuration) >= 0)
                .findFirst();
        if (planeOptional.isPresent()) {
            planeOptional.get().performOneFlight(flightDuration);
        } else {
            pool.add(new Plane());
            performOneFlight(flightDuration);
        }
    }
}*/

class RouteTimeTableOptimizer {
    static final Duration duration = Duration.ofHours(24);

    public static void main(String[] args) {
        var dataFromExcel = ReadFile.readGatheredResultFromPerfectSeatWebsiteFile("AirlinesManagerNewRoutesInfo");//or AirlinesManagerAllRoutesInfo

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

        PlanePool planePool = new PlanePool();

        for (var configurationMapEntry : configuration.entrySet()) {
//            var minimalTime = configurationMapEntry.getValue().values().stream()
//                    .map(FlightInfo::getDuration)
//                    .min(Comparator.naturalOrder());
            for (var flight : configurationMapEntry.getValue().entrySet()) {

                FlightInfo info = flight.getValue();// numberOfWavesNeeded and timeTakenForOneWave
                while (info.getNumberOfWaves() > 0) {//and seats check
                    planePool.performOneFlight(info.getDuration(), flight.getKey());
                    info.decrementNumberOfWaves();
                }
            }
        }
    }

/*        List<Integer> wavesPerDayCanBeDone = new LinkedList<>();
        List<String> timeRemain = new LinkedList<>();

        for (var row : dataFromExcel) {
            String[] tokens = row.timeTakenForOneWave().split(":");
            int hours = Integer.parseInt(tokens[0]);
            int minutes = Integer.parseInt(tokens[1]);

            int waveAmountPerDay = wavesPerDay(hours, minutes);
            if (parseInt(row.numberOfWavesNeeded()) < waveAmountPerDay) {
                Duration remainingTime = remainingTime(hours, minutes, parseInt(row.numberOfWavesNeeded()));
                wavesPerDayCanBeDone.add(parseInt(row.numberOfWavesNeeded()));
                timeRemain.add(remainingTime.toHoursPart() + ":" + remainingTime.toMinutesPart());
            } else {
                wavesPerDayCanBeDone.add(waveAmountPerDay);
                Duration remainingTime = remainingTime(hours, minutes, waveAmountPerDay);
                timeRemain.add(remainingTime.toHoursPart() + ":" + remainingTime.toMinutesPart());
            }
        }

        for (var row : dataFromExcel) {
            String[] tokens = row.timeTakenForOneWave().split(":");
            int hours = Integer.parseInt(tokens[0]);
            int minutes = Integer.parseInt(tokens[1]);

            String[] remain = timeRemain.get().split(":");
            int remainHours = Integer.parseInt(remain[0]);
            int remainMinutes = Integer.parseInt(remain[1]);
            if () {

            }
        }*/

    public static Duration convertToDuration(String time) {
        String[] tokens = time.split(":");
        int hours = Integer.parseInt(tokens[0]);
        int minutes = Integer.parseInt(tokens[1]);
        Duration hour = Duration.ofHours(hours);
        Duration minute = Duration.ofMinutes(minutes);
        return hour.plus(minute);
    }

    public static int wavesPerDay(Duration time) {
        return (int) duration.dividedBy(time);
    }

    public static int wavesPerDay(int hours, int minutes) {
        Duration hour = Duration.ofHours(hours);
        Duration minute = Duration.ofMinutes(minutes);
        return (int) duration.dividedBy(hour.plus(minute));
    }

    public static Duration remainingTime(int hours, int minutes, int waveAmountPerDay) {
        Duration hour = Duration.ofHours(hours);
        Duration minute = Duration.ofMinutes(minutes);
        return duration.minus(hour.plus(minute).multipliedBy(waveAmountPerDay));
    }
}