package pages.airlineManagerWebsite;

import com.codeborne.selenide.Condition;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Objects;

import static com.codeborne.selenide.Selenide.$x;
import static com.codeborne.selenide.Selenide.open;

public class PlaneDetailsPage {
    String aircraftName = "//*[@id=\"aircraft_show\"]/div[1]/div[1]/span[1]";
    String aircraftCategory = "//*[@id=\"aircraft_show\"]/div[1]/div[1]/img[1]";
    String aircraftDeterioration = "//*[@id=\"resumeWear\"]/b/span";
    String aircraftAge = "//*[@id=\"resumeMark\"]/b";
    String checkA = "//*[@id=\"checkA\"]/a";
    String checkD = "//*[@id=\"checkD\"]/a";

    String configurationLink = "//*[@id=\"subMenu\"]/a[3]";

    public String getConfigurationLink() {
        return $x(configurationLink).shouldBe(Condition.visible).getAttribute("href");
    }

    public String getAircraftName() {
        String collectedAircraftName = $x(aircraftName).getText();
        return StringUtils.substringAfter(collectedAircraftName, "/").trim();
    }

    public void getInfoAboutPlane(List<List<String>> infoAboutPlane) {
        String formattedAircraftName = getAircraftName();

        String collectedAircraftCategory = Objects.requireNonNull($x(aircraftCategory).getAttribute("alt")).trim().replaceAll("\\D+", "");

        String collectedAircraftDeterioration = $x(aircraftDeterioration).getText();
        String formattedAircraftDeterioration = StringUtils.substringBefore(collectedAircraftDeterioration, "%").trim();

        String linkToPlaneConfigurationPage = getConfigurationLink();
        //String linkToPlaneConfigurationPage = $x(configurationLink).getAttribute("href");

        infoAboutPlane.add(List.of(formattedAircraftName, collectedAircraftCategory, formattedAircraftDeterioration, Objects.requireNonNull(linkToPlaneConfigurationPage)));
    }

    public void performACheck() {
        open(Objects.requireNonNull($x(checkA).getAttribute("href")));
    }

    public void performDCheck() {
        open(Objects.requireNonNull($x(checkD).getAttribute("href")));
    }

    public void makeCheckDecision() {
        float damageBeginsFrom = 20;
        double recommendedAgeToStartCheck = 3.5;

        String collectedAircraftDeterioration = $x(aircraftDeterioration).getText();
        float deteriorationPercentage = Float.parseFloat(StringUtils.substringBefore(collectedAircraftDeterioration, "%").trim());

        String planeAgeValue = $x(aircraftAge).getText();
        float planeAge = Float.parseFloat(StringUtils.substringBefore(planeAgeValue, "/").trim());

        if (deteriorationPercentage > damageBeginsFrom) {
            if (planeAge <= recommendedAgeToStartCheck) {
                performACheck();
            } else {
                performDCheck();
            }
        }
    }

}