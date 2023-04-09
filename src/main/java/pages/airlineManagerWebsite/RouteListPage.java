package pages.airlineManagerWebsite;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;

import java.util.List;

import static com.codeborne.selenide.Selenide.*;

public class RouteListPage {
    String sortByRoutLength = "//input[@value='distanceMinus']";

    String distance = "//*[@id=\"lineList\"]//ul/li[1]/b";

/*
    String getLastPage = "//a[normalize-space()='>>']";
    String getPageNumber = "//span[@class='current']";
    String getFirstPage = "//a[normalize-space()='<<']";

        public int getTotalPageNumberAndReturnToFirstPage() {
        String lastPageLink = $x(getLastPage).getAttribute("href");
        open(lastPageLink);
        int totalPageNumberCount = parseInt($x(getPageNumber).getText());
        String firstPageLink = $x(getFirstPage).getAttribute("href");
        open(firstPageLink);

        return totalPageNumberCount;
    }*/

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
