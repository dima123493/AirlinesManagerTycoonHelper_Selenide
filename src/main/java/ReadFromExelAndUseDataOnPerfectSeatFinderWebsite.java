import com.codeborne.selenide.Configuration;
import filesManagment.ReadFile;
import filesManagment.WriteFile;
import pages.finderWebsite.PerfectSeatFinderPage;

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

    public static void main(String[] args) {
        var dataFromExcel = ReadFile.readGatheredInfoFile("AirlinesManagerNewRoutesInfo");//AirlinesManagerNewRoutesInfo  AirlinesManagerAllRoutesInfo
        List<List<String>> dataFromPerfectSeatFinderSite = new LinkedList<>();

        Configuration.browserSize = "1920x1080";
        open(URL);
        zoom(0.5);

        PerfectSeatFinderPage perfectSeatFinderPage = new PerfectSeatFinderPage();
        perfectSeatFinderPage.changeHubInputType(SOURCE_HUB_VALUE);
        int counter = 0;
        for (var row : dataFromExcel) {
            int routeDistanceFromExcel = Integer.parseInt(row.distanceToAirport());

            if (routeDistanceFromExcel <= CRJ_1000_MAX_DISTANCE) {
                perfectSeatFinderPage.fillTheFieldsOnTheWebsite(CRJ_1000_MANUFACTURER_NAME, CRJ_1000_MODEL_NUMBER, row);
            } else if (routeDistanceFromExcel <= A300_600R_MAX_DISTANCE) {
                perfectSeatFinderPage.fillTheFieldsOnTheWebsite(A300_600R_MANUFACTURER_NAME, A300_600R_MODEL_NUMBER, row);
            } else if (routeDistanceFromExcel <= A380_800_MAX_DISTANCE) {
                perfectSeatFinderPage.fillTheFieldsOnTheWebsite(A380_800_MANUFACTURER_NAME, A380_800_MODEL_NUMBER, row);
            } else {
                perfectSeatFinderPage.fillTheFieldsOnTheWebsite("Airbus", "A350-900ULR", row); // plane with the longest distance in the game
            }
            dataFromPerfectSeatFinderSite.add(PerfectSeatFinderPage.collectDataFromPerfectSeatFinderWebSite());
            perfectSeatFinderPage.removeRouteFromSearch();

            counter++;
            if (counter == 50) {
                refresh();
                zoom(0.5);
                counter = 0;
            }
        }

        /*ArrayList<List<String>> dataFromExcel = new ArrayList<>();
        dataFromExcel.add(new ArrayList<>(List.of("GME", "1065", "104", "24", "29", "185", "246", "425", "387", "242")));
        dataFromExcel.add(new ArrayList<>(List.of("FAT", "834", "167", "52", "36", "2812", "3739", "6467", "6619", "9847")));
        dataFromExcel.add(new ArrayList<>(List.of("GIG", "1598", "159", "39", "48", "3078", "4093", "7079", "7254", "9877")));

        ArrayList<List<String>>dataFromPerfectSeatFinderSite = new ArrayList<>();
        dataFromPerfectSeatFinderSite.add(new ArrayList<>(List.of("2", "GME", "104", "24", "29", "185", "246", "425", "387", "242")));
        dataFromPerfectSeatFinderSite.add(new ArrayList<>(List.of("3", "FAT", "104", "24", "29", "185", "246", "425", "387", "242")));
        dataFromPerfectSeatFinderSite.add(new ArrayList<>(List.of("4", "GIG", "104", "24", "29", "185", "246", "425", "387", "242")));

        AllRoutesDetailsGathering.loginOnWebsite(AirlinesProperties.getProperty("login-email"), AirlinesProperties.getProperty("login-password"));
        open(AirlinesProperties.getProperty("route-planing-page"));
        RoutePlaningPage routeTime = new RoutePlaningPage();
        List<String> time = new LinkedList<>();
        routeTime.getAirCrafts();
        String airCraftIdNeeded = "";
        String airCraftIdSelected = "";
        for (var row : dataFromExcel) {
            //int routeDistance = Integer.parseInt(kilometers.get(dataFromExcel.indexOf(row)));//this can be removed if new version works
            int routeDistanceFromExcel = Integer.parseInt(row.get(9));//new version .distanceToAirport()
            if (routeDistanceFromExcel <= CRJ_1000_MAX_DISTANCE) {
                airCraftIdNeeded = routeTime.getAircraftId(CRJ_1000_MODEL_NUMBER);
                System.out.println("1 This id was requested:" + airCraftIdNeeded + "<-");
                System.out.println(row.get(0));
                airCraftIdSelected = routeTime.getTimeForTheRouteWave(time, row.get(0), airCraftIdNeeded, airCraftIdSelected);
            } else if (routeDistanceFromExcel > CRJ_1000_MAX_DISTANCE && routeDistanceFromExcel <= A300_600R_MAX_DISTANCE) {
                airCraftIdNeeded = routeTime.getAircraftId(A300_600R_MODEL_NUMBER);
                System.out.println("2 This id was requested:" + airCraftIdNeeded + "<-");
                System.out.println(row.get(0));
                airCraftIdSelected = routeTime.getTimeForTheRouteWave(time, row.get(0), airCraftIdNeeded, airCraftIdSelected);
            } else if (routeDistanceFromExcel > A300_600R_MAX_DISTANCE && routeDistanceFromExcel <= A380_800_MAX_DISTANCE) {//&& routeDistanceFromExcel <= A300_600R_MAX_DISTANCE || routeDistanceFromExcel <= A380_800_MAX_DISTANCE
                airCraftIdNeeded = routeTime.getAircraftId(A380_800_MODEL_NUMBER);
                System.out.println("3 This id was requested:" + airCraftIdNeeded + "<-");
                System.out.println(row.get(0));
                System.out.println(airCraftIdSelected);
                airCraftIdSelected = routeTime.getTimeForTheRouteWave(time, row.get(0), airCraftIdNeeded, airCraftIdSelected);
            }
        }

        for (int i = 0; i < dataFromPerfectSeatFinderSite.size(); i++) {
            dataFromPerfectSeatFinderSite.get(i).add(time.get(i));
        }*/

        WriteFile.writeInfoFromPerfectSeatFinderWebsite(dataFromPerfectSeatFinderSite, "PerfectSeatFinderNewRoutesInfo");//PerfectSeatFinderNewRoutesInfo PerfectSeatFinderAllRoutesInfo
        closeWebDriver();

    }

}