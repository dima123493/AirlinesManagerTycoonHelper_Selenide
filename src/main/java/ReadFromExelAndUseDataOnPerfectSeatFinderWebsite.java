import com.codeborne.selenide.Configuration;
import filesManagment.ReadFile;
import filesManagment.WriteFile;
import pages.airlineManagerWebsite.RoutePlaningPage;
import pages.finderWebsite.PerfectSeatFinderPage;
import propertyLoader.AirlinesProperties;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import static com.codeborne.selenide.Selenide.*;
import static propertyLoader.PerfectSeatFinderProperties.getProperty;

public class ReadFromExelAndUseDataOnPerfectSeatFinderWebsite {
    static final String URL = getProperty("perfect-seat-finder-base-url");
    static final String SOURCE_HUB_VALUE = getProperty("hub-from-which-rote-starts");
    //Short haul
    static final String CRJ_1000_MANUFACTURER_NAME = "Bombardier";
    static final String CRJ_1000_MODEL_NUMBER = "CRJ-1000 ";
    static final int CRJ_1000_MAX_DISTANCE = 3129;

    //Middle haul
    //Long haul
    static final String A300_600R_MANUFACTURER_NAME = "Airbus";
    static final String A300_600R_MODEL_NUMBER = "A300-600R ";
    static final int A300_600R_MAX_DISTANCE = 7540;
    static final String A380_800_MANUFACTURER_NAME = "Airbus";
    static final String A380_800_MODEL_NUMBER = "A380-800 ";
    static final int A380_800_MAX_DISTANCE = 15556;

    public static void main(String[] args) throws IOException {
        var dataFromExcel = ReadFile.readGatheredInfoFile("AirlinesManagerAllRoutesInfo1");//or AirlinesManagerNewRoutesInfo
        List<List<String>> dataFromPerfectSeatFinderSite = new LinkedList<>();

        //var file2 = Path.of(getProperty("file-path-to-distance-for-each-route"));//this can be removed if new version works
        //List<String> kilometers = Files.readAllLines(file2);//this can be removed if new version works

        Configuration.browserSize = "1920x1080";
        open(URL);
        zoom(0.5);

        PerfectSeatFinderPage perfectSeatFinderPage = new PerfectSeatFinderPage();
        perfectSeatFinderPage.changeHubInputType(SOURCE_HUB_VALUE);

        for (var row : dataFromExcel) {
            //int routeDistance = Integer.parseInt(kilometers.get(dataFromExcel.indexOf(row)));//this can be removed if new version works
            int routeDistanceFromExcel = Integer.parseInt(row.distanceToAirport());//new version

            if (routeDistanceFromExcel <= CRJ_1000_MAX_DISTANCE) {
                perfectSeatFinderPage.fillTheFieldsOnTheWebsite(CRJ_1000_MANUFACTURER_NAME, CRJ_1000_MODEL_NUMBER, row);
            } else if (routeDistanceFromExcel > CRJ_1000_MAX_DISTANCE && routeDistanceFromExcel <= A300_600R_MAX_DISTANCE) {
                perfectSeatFinderPage.fillTheFieldsOnTheWebsite(A300_600R_MANUFACTURER_NAME, A300_600R_MODEL_NUMBER, row);
            } else if (routeDistanceFromExcel > A300_600R_MAX_DISTANCE && routeDistanceFromExcel <= A380_800_MAX_DISTANCE) {
                perfectSeatFinderPage.fillTheFieldsOnTheWebsite(A380_800_MANUFACTURER_NAME, A380_800_MODEL_NUMBER, row);
            } else {
                perfectSeatFinderPage.fillTheFieldsOnTheWebsite("Airbus", "A350-900ULR", row); // plane with the longest distance in the game
            }
            dataFromPerfectSeatFinderSite.add(PerfectSeatFinderPage.collectDataFromPerfectSeatFinderWebSite());
            perfectSeatFinderPage.removeRouteFromSearch();
        }

        AllRoutesDetailsGathering.loginOnWebsite(AirlinesProperties.getProperty("login-email"), AirlinesProperties.getProperty("login-password"));
        open(AirlinesProperties.getProperty("route-planing-page"));
        RoutePlaningPage routeTime = new RoutePlaningPage();
        List<String> time = new LinkedList<>();
        routeTime.getAirCrafts();
        String airCraftIdNeeded = "";
        String airCraftIdSelected = "";
        for (var row : dataFromExcel) {
            //int routeDistance = Integer.parseInt(kilometers.get(dataFromExcel.indexOf(row)));//this can be removed if new version works
            int routeDistanceFromExcel = Integer.parseInt(row.distanceToAirport());//new version .distanceToAirport()
            if (!row.destinationHubValue().contains("GIB") && routeDistanceFromExcel <= CRJ_1000_MAX_DISTANCE) {
                airCraftIdNeeded =  routeTime.getAircraftId(CRJ_1000_MODEL_NUMBER);
                airCraftIdSelected = routeTime.getTimeForTheRouteWave(time, row.destinationHubValue(),airCraftIdNeeded,airCraftIdSelected);
            } else if (routeDistanceFromExcel > CRJ_1000_MAX_DISTANCE && routeDistanceFromExcel <= A300_600R_MAX_DISTANCE || row.destinationHubValue().equals("GIB")) {
                airCraftIdNeeded =  routeTime.getAircraftId(A300_600R_MODEL_NUMBER);
                airCraftIdSelected = routeTime.getTimeForTheRouteWave(time, row.destinationHubValue(),airCraftIdNeeded,airCraftIdSelected);
            } else if (routeDistanceFromExcel > A300_600R_MAX_DISTANCE && routeDistanceFromExcel <= A380_800_MAX_DISTANCE) {
                airCraftIdNeeded =  routeTime.getAircraftId(A380_800_MODEL_NUMBER);
                airCraftIdSelected = routeTime.getTimeForTheRouteWave(time, row.destinationHubValue(),airCraftIdNeeded,airCraftIdSelected);
            }
        }

        for (int i = 0; i < dataFromPerfectSeatFinderSite.size(); i++) {
            dataFromPerfectSeatFinderSite.get(i).add(time.get(i));
        }

        WriteFile.writeInfoFromPerfectSeatFinderWebsite(dataFromPerfectSeatFinderSite, "PerfectSeatFinderAllRoutesInfo");//PerfectSeatFinderNewRoutesInfo
        closeWebDriver();

    }

}