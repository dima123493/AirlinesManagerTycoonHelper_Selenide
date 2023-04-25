package pages.airlineManagerWebsite;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import java.util.List;
import java.util.Objects;

import static com.codeborne.selenide.Selenide.*;
import static java.lang.Integer.parseInt;

public class PlanesListPage {
    String goLastPage = "//a[normalize-space()='>>']";
    String getPageNumber = "//span[@class='current']";
    String goFirstPage = "//a[normalize-space()='<<']";
    String getNextPage = "//a[normalize-space()='>']";

    public int getTotalPageNumberAndReturnToFirstPage() {
        String lastPageLink = $x(goLastPage).getAttribute("href");
        open(Objects.requireNonNull(lastPageLink));
        int totalPageNumberCount = parseInt($x(getPageNumber).getText());
        String firstPageLink = $x(goFirstPage).getAttribute("href");
        open(Objects.requireNonNull(firstPageLink));

        return totalPageNumberCount;
    }

    public void goToNextPage() {
        String goNextPage = $x(getNextPage).getAttribute("href");
        open(Objects.requireNonNull(goNextPage));
    }

    public void getHrefsFromPlanesList(List<String> links) {
        String listOfHrefsToPlaneDetailesPage = "//*[@id=\"aircraft_dashboard\"]//div[5]//a";
        ElementsCollection linksArray = $$x(listOfHrefsToPlaneDetailesPage);
        for (SelenideElement selenideElement : linksArray) {
            links.add(selenideElement.getAttribute("href"));
        }
    }
}