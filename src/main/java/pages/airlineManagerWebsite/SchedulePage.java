package pages.airlineManagerWebsite;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;

import java.util.List;
import java.util.Objects;

import static com.codeborne.selenide.Selenide.$$;

public class SchedulePage {
    String sortedTableRows = "sortable";
    String aircraftModel = "blueText";
    String aircraftRouteName = "aircraftUserName";
    String aircraftId = "//td/a";
    String wavesPerPlane = "//span[@class='center']";

    public void collectInfoAboutPlanesAndWavesOnTheRoute(List<List<String>> collectedData) {
        ElementsCollection aircraft = $$(By.className(sortedTableRows));

        for (SelenideElement row : aircraft) {
            String model = row.$(By.className(aircraftModel)).getText();
            String roteName = row.$(By.className(aircraftRouteName)).getText();
            String aircraftID = Objects.requireNonNull(row.$x(aircraftId).getAttribute("href")).replaceAll("\\D+", "");
            String howManyWavesIsCarried = String.valueOf(row.$$x(wavesPerPlane).size());
            collectedData.add(List.of(model, roteName, aircraftID, howManyWavesIsCarried));
        }
    }
}
