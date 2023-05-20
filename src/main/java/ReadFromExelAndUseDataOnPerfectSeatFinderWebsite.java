import com.codeborne.selenide.Configuration;
import filesManagment.ReadFile;
import filesManagment.WriteFile;
import pages.finderWebsite.PerfectSeatFinderPage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
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
    static final String A300_600R_MANUFACTURER_NAME = "Airbus";
    static final String A300_600R_MODEL_NUMBER = "A300-600R ";
    static final int A300_600R_MAX_DISTANCE = 7540;
    //Long haul
    static final String A380_800_MANUFACTURER_NAME = "Airbus";
    static final String A380_800_MODEL_NUMBER = "A380-800 ";
    static final int A380_800_MAX_DISTANCE = 15556;

    public static void main(String[] args) throws IOException {
        var dataFromExcel = ReadFile.readGatheredInfoFile();
        List<List<String>> dataFromPerfectSeatFinderSite = new ArrayList<>();

        var file2 = Path.of(getProperty("file-path-to-distance-for-each-route"));
        List<String> kilometers = Files.readAllLines(file2);

        for (var row : dataFromExcel) {
            int routeDistance = Integer.parseInt(kilometers.get(dataFromExcel.indexOf(row)));

            Configuration.browserSize = "1920x1080";

            open(URL);
            zoom(0.5);
            PerfectSeatFinderPage perfectSeatFinderPage = new PerfectSeatFinderPage();

            if (routeDistance <= CRJ_1000_MAX_DISTANCE) {
                perfectSeatFinderPage.fillTheFieldsOnTheWebsite(SOURCE_HUB_VALUE, CRJ_1000_MANUFACTURER_NAME, CRJ_1000_MODEL_NUMBER, row);
            } else if(routeDistance > CRJ_1000_MAX_DISTANCE && routeDistance <= A300_600R_MAX_DISTANCE) {
                perfectSeatFinderPage.fillTheFieldsOnTheWebsite(SOURCE_HUB_VALUE, A300_600R_MANUFACTURER_NAME, A300_600R_MODEL_NUMBER, row);
            }else if(routeDistance > A300_600R_MAX_DISTANCE && routeDistance <= A380_800_MAX_DISTANCE) {
                perfectSeatFinderPage.fillTheFieldsOnTheWebsite(SOURCE_HUB_VALUE, A380_800_MANUFACTURER_NAME, A380_800_MODEL_NUMBER, row);
            }else{
                perfectSeatFinderPage.fillTheFieldsOnTheWebsite(SOURCE_HUB_VALUE, "Airbus", "A350-900ULR", row); // plane with the longest distance in the game
            }

            dataFromPerfectSeatFinderSite.add(PerfectSeatFinderPage.collectDataFromPerfectSeatFinderWebSite());
            clearBrowserCookies();
            refresh();
        }
        WriteFile.writeInfoFromPerfectSeatFinderWebsite(dataFromPerfectSeatFinderSite);
        closeWebDriver();
    }

}