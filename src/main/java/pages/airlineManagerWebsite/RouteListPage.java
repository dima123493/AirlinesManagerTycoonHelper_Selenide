package pages.airlineManagerWebsite;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;

import java.util.List;
import java.util.Map;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$x;

public class RouteListPage {
    String sortByRoutLengthASD = "//input[@value='distanceMinus']";
    String distance = "//*[@id=\"lineList\"]//ul/li[1]/b";
    String lineDropDownOptions = "//*[@id=\"map_filters\"]//option";

    public void sortByRoutLength() {
        $(By.xpath(sortByRoutLengthASD)).click();
    }

    public void getHrefsFromRouteListPage(List<String> links) {
        ElementsCollection linksArray = $$x("//*[@id=\"lineList\"]/div/div/a");
        for (SelenideElement selenideElement : linksArray) {
            links.add(selenideElement.getAttribute("href"));
        }
    }

    public void distanceOfTheRoutes(List<String> listOfKms) {
        ElementsCollection linksArray = $$x(distance);
        for (SelenideElement selenideElement : linksArray) {
            listOfKms.add(String.valueOf(selenideElement).replaceAll("\\D+", ""));
        }
    }

    public void collectAirportNamesAndTheirLinks( Map<String,String> airportNames){
        List<SelenideElement> airportLocators =  $$x(lineDropDownOptions);
        for(SelenideElement element :airportLocators){
            String text = element.getText();
            String aitaCode = text.substring(text.length() - 3).trim();
            airportNames.put(aitaCode,element.getAttribute("value"));
        }
    }

}