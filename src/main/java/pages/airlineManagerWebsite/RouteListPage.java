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

    public int countLinks() {
        ElementsCollection linksArray = $$x("//*[@id=\"lineList\"]/div/div/a");
        return linksArray.size();
    }

    public void getHrefsFromRouteList(List<String> links) {
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

    public void printCollectedLinksAndCheckAmountOfItems(List<String> hrefs, int totalLinksNumber) {
        for (String links : hrefs) {
            System.out.println(links);
        }
        System.out.println(hrefs.size() + " === should be equal === " + totalLinksNumber);
    }
}