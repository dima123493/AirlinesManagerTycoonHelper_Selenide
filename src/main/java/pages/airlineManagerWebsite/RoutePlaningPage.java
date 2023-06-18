package pages.airlineManagerWebsite;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;

import java.util.*;

import static com.codeborne.selenide.Selenide.*;

public class RoutePlaningPage {
    private static final String CAROUSEL_VIEW = "carouselView";
    private static final String collectionOfAirCrafts = "//*[@id=\"carouselView\"]/div/div";
    private static final String aircraftModel = "//span[@class=\"bold\"]";
    private static final String searchAirportNameField = "lineNameFilter";
    private static final String nameOfSelectedAirport = "//*[@id=\"lineList\"]/span";
    private static final String allDistances = "//input[@value='all']";
    private static final String shDistance = "//input[@value='cc']";
    private static final String mhDistance = "//input[@value='mc']";
    private static final String lhDistance = "//input[@value='lc']";

    private static final SelenideElement scrollLineIndicator = $x("//*[@id=\"aircraftSlider\"]/a");
    private static final SelenideElement scrollToTheRight = $(By.id("aircraftSliderRight"));
    private static final SelenideElement scrollToTheLeft = $(By.id("aircraftSliderLeft"));

    public Map<String, String> airCrafts = new LinkedHashMap<>();

    public void getAirCrafts() {
        $(By.id(CAROUSEL_VIEW)).shouldBe(Condition.visible);
        ElementsCollection airCraftCollection = $$x(collectionOfAirCrafts);
        List<String> locators = new ArrayList<>();
        locators.add(shDistance);
        locators.add(mhDistance);
        locators.add(lhDistance);
        for (String amount : locators) {
            $x(amount).scrollTo().click();
            for (SelenideElement aircraft : airCraftCollection) {
                String aircraftId = aircraft.getAttribute("id");//.replaceAll("\\D+", "")
                String aircrftModel = aircraft.findElement(By.xpath(aircraftModel)).getText().replaceAll("(?<= ).*", "").trim();
                airCrafts.put(aircraftId, aircrftModel);
            }
        }
        $x(allDistances).scrollTo().click();
    }

    public String getAircraftId(String model) {
        String aircraftId = "";
        for (Map.Entry<String, String> value : airCrafts.entrySet()) {
            if (value.getValue().contains(model.trim())) {
                aircraftId = value.getKey();
                break;
            }
        }
        return aircraftId;
    }

    public String getTimeForTheRouteWave(List<String> time, String destinationHubValue, String airCraftIdNeeded, String airCraftIdSelected) {
        //System.out.println(airCrafts);
        /*Map.Entry<String, String> airCraft = airCrafts.entrySet().stream().findFirst().filter(el -> !el.getValue().contains(model)).get();
        String aircraftId = airCraft.getKey();*/
        /*String aircraftId = "";
        for(Map.Entry<String, String> value : airCrafts.entrySet()){
            if(value.getValue().contains(model.trim())){
                aircraftId = value.getKey();
                break;
            }
        }*/

        boolean selected = false;
        String selectedAircraftId = "";


        while (!selected) {
            if (!airCraftIdSelected.equals(airCraftIdNeeded)) {
                scrollToTheRight.click();
                if ($(By.id(airCraftIdNeeded)).isDisplayed()) {
                    $(By.id(airCraftIdNeeded)).click();
                    selectedAircraftId = airCraftIdNeeded;
                    selected = true;
                    rollBackCursor();
                }
            } else {
                selectedAircraftId = airCraftIdNeeded;
                selected = true;
            }
            if (Objects.equals(scrollLineIndicator.getAttribute("style"), "left: 100%;")) {
                selected = true;
                System.out.println("Plane not found or check the name search");
            }
        }


        $(By.id(searchAirportNameField)).scrollTo().setValue(destinationHubValue);
        String timeFormatted = $x(nameOfSelectedAirport).shouldBe(Condition.appear).getText().replaceAll(".*(?=) ", "").trim().replace("h", ":");
        time.add(timeFormatted);

        return selectedAircraftId;
    }

    public void rollBackCursor() {
        boolean returned = false;
        while (!returned) {
            scrollToTheLeft.click();
            if (Objects.equals(scrollLineIndicator.getAttribute("style"), "left: 0%;")) {
                returned = true;
            }
        }
    }

/*    public void pickDistanceRange(String aircraftRange) {
        if (aircraftRange.equals("SH")) {
            $x(shDistance).scrollTo().click();
        } else if (aircraftRange.equals("MH")) {
            $x(mhDistance).scrollTo().click();
        } else {
            $x(lhDistance).scrollTo().click();
        }
    }*/
}
