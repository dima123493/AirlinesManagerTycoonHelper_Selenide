package pages.airlineManagerWebsite;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;

import java.util.List;

import static com.codeborne.selenide.Selenide.*;

public class RoutePlaningPage {
    String aircraftModel = "//*[@class=\"bold\"]";
    String searchAirportNameField = "lineNameFilter";
    String nameOfSelectedAirport = "//*[@id=\"lineList\"]/span";

    public void getTimeForTheRouteWave(List<String> time, String model,String destinationHubValue){
        SelenideElement airCraft = $$x(aircraftModel).filter(Condition.have(Condition.text(model))).first();
        airCraft.click();
        $(By.id(searchAirportNameField)).setValue(destinationHubValue);
        String timeFormatted = $x(nameOfSelectedAirport).getText().replaceAll(".*(?=) ","").trim().replace("h",":");
        time.add(timeFormatted);
        System.out.println(timeFormatted);
    }
}
