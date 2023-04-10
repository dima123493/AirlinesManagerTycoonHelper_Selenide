package pages.airlineManagerWebsite;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import java.util.List;

import static com.codeborne.selenide.Selenide.*;
import static java.lang.Integer.parseInt;

public class PlanesListPage {
    String goLastPage = "//a[normalize-space()='>>']";
    String getPageNumber = "//span[@class='current']";
    String goFirstPage = "//a[normalize-space()='<<']";
    String getNextPage = "//a[normalize-space()='>']";

    public int getTotalPageNumberAndReturnToFirstPage() {
        String lastPageLink = $x(goLastPage).getAttribute("href");
        open(lastPageLink);
        int totalPageNumberCount = parseInt($x(getPageNumber).getText());
        String firstPageLink = $x(goFirstPage).getAttribute("href");
        open(firstPageLink);

        return totalPageNumberCount;
    }

    public void goToNextPage() {
        String goNextPage = $x(getNextPage).getAttribute("href");
        open(goNextPage);
    }

    public void getHrefsFromPlanesList(List<String> links) {
        ElementsCollection linksArray = $$x("//*[@id=\"aircraft_dashboard\"]//div[5]//a");
        for (SelenideElement selenideElement : linksArray) {
            links.add(selenideElement.getAttribute("href"));
        }
    }
}
