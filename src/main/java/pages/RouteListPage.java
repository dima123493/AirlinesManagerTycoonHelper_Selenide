package pages;

import com.codeborne.selenide.ElementsCollection;
import org.openqa.selenium.By;

import java.util.List;

import static com.codeborne.selenide.Selenide.*;

public class RouteListPage {
    String getLastPage = "//a[normalize-space()='>>']";
    String getPageNumber = "//span[@class='current']";
    String getFirstPage = "//a[normalize-space()='<<']";
    String sortByRoutLength = "//input[@value='distanceMinus']";

    String distance = "//*[@id=\"lineList\"]//ul/li[1]/b";

/*    public int getTotalPageNumberAndReturnToFirstPage() {
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

    public List<String> getHrefsFromRouteList(List<String> links) {
        ElementsCollection linksArray = $$x("//*[@id=\"lineList\"]/div/div/a");
        for (int i = 0; i < linksArray.size(); i++) {
            links.add(linksArray.get(i).getAttribute("href"));
        }
        return links;
    }

    public List<String> distanceOfTheRoutes(List<String> listOfKms) {
        ElementsCollection linksArray = $$x(distance);

        for (int i = 0; i < linksArray.size(); i++) {
            listOfKms.add(String.valueOf(linksArray.get(i)).replaceAll("\\D+", ""));
        }
        System.out.println("Route length in km");
        return listOfKms;
    }

    public void printCollectedLinksAndCheckAmountOfItems(List<String> hrefs, int totalLinksNumber) {
        for (String links : hrefs) {
            System.out.println(links);
        }
        System.out.println(hrefs.size() + " === should be equal === " + totalLinksNumber);
    }
}
