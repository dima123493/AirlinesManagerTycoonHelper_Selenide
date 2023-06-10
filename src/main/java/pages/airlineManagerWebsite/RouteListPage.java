package pages.airlineManagerWebsite;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;

import java.util.List;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$x;

public class RouteListPage {
    String sortByRoutLength = "//input[@value='distanceMinus']";

    String distance = "//*[@id=\"lineList\"]//ul/li[1]/b";



    public void sortByRoutLength() {
        $(By.xpath(sortByRoutLength)).click();
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
        System.out.println("Route length in km was gathered");
    }

}