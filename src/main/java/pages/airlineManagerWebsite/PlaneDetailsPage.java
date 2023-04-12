package pages.airlineManagerWebsite;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.codeborne.selenide.Selenide.*;
import static java.lang.Long.parseLong;

public class PlaneDetailsPage {
    String aircraftName = "//*[@id=\"aircraft_show\"]/div[1]/div[1]/span[1]";//getText before "/" symbol
    String aircraftCategory = "//*[@id=\"aircraft_show\"]/div[1]/div[1]/img[1]";//getAttribute alt ->("cat4") get numbers only;
    String aircraftDeterioration = "//*[@id=\"resumeWear\"]/b/span";//getText, get numbers only;
    String aircraftAge = "//*[@id=\"resumeMark\"]/b";// get Text before "/"
    String checkACost = "//*[@id=\"checkA\"]/div";//getText Parse INT
    String checkDCost = "//*[@id=\"checkD\"]/div";//getText Parse INT
    String amountOfMoneyInMyAccount = "ressource3";
    String checkA = "//*[@id=\"checkA\"]/a";//get href and open
    String checkD = "//*[@id=\"checkD\"]/a";//get href and open

    String configurationLink = "//*[@id=\"subMenu\"]/a[3]";

    public void getInfoAboutPlane(List<List<String>> infoAboutPlane) {
        String collectedAircraftName = $x(aircraftName).getText();
        String formattedAircraftName = StringUtils.substringBefore(collectedAircraftName, "/").trim();

        String collectedAircraftCategory = Objects.requireNonNull($x(aircraftCategory).getAttribute("alt")).trim().replaceAll("\\D+", "");

        String collectedAircraftDeterioration = $x(aircraftDeterioration).getText();
        String formattedAircraftDeterioration = StringUtils.substringBefore(collectedAircraftDeterioration, "%").trim();

        String linkToPlaneConfigurationPage = $x(configurationLink).getAttribute("href");

        infoAboutPlane.add(List.of(formattedAircraftName, collectedAircraftCategory, formattedAircraftDeterioration, linkToPlaneConfigurationPage));
    }

    public void performACheck() {
        //long myAccountMoney = parseLong(($(By.id(amountOfMoneyInMyAccount)).getText().replaceAll("(?<=.)\\D+", "")));
        //long checkACostValue = parseLong(($(By.id(checkACost)).getText().replaceAll("(?<=.)\\D+", "")));

        //if (checkACostValue <= myAccountMoney) {
            open(Objects.requireNonNull($x(checkA).getAttribute("href")));
        //} check of account balance is not required to perform a check
    }

    public void performDCheck() {
        //long myAccountMoney = parseLong(($(By.id(amountOfMoneyInMyAccount)).getText().replaceAll("(?<=.)\\D+", "")));
       // long checkDCostValue = parseLong(($(By.id(checkDCost)).getText().replaceAll("(?<=.)\\D+", "")));

       // if (checkDCostValue <= myAccountMoney) {
            open(Objects.requireNonNull($x(checkD).getAttribute("href")));
       // } check of account balance is not required to perform a check
    }

    public void makeCheckDecision() {
        float damageBeginsFrom = 20;

        String collectedAircraftDeterioration = $x(aircraftDeterioration).getText();
        float deteriorationPercentage = Float.parseFloat(StringUtils.substringBefore(collectedAircraftDeterioration, "%").trim());

        String planeAgeValue = $x(aircraftAge).getText();
        float planeAge = Float.parseFloat(StringUtils.substringBefore(planeAgeValue, "/").trim());

        if (deteriorationPercentage >= damageBeginsFrom) {
            if (planeAge<5) {
                performACheck();
            } else if (planeAge==5) {
                performDCheck();
            }
        }
    }

}
