package pages.airlineManagerWebsite;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;

import java.util.List;

import static com.codeborne.selenide.Selenide.$$;

public class SchedulePage {
    String sortedTableRows = "sortable";
    String aircraftModel = "blueText";
    String aircraftRouteName = "aircraftUserName";
    String aircraftName = "aircraftName";
    String aircraftId = "a";
    String wavesPerPlane = "center";

    public void collectInfoAboutPlanesAndWavesOnTheRoute(List<List<String>> collectedData) {
        ElementsCollection aircraft = $$(By.className(sortedTableRows));

        for (SelenideElement row : aircraft) {
            String model = row.$(By.className(aircraftModel)).getText();
            String roteName = row.$(By.className(aircraftRouteName)).getText();
            String aircraftID = row.$(By.className(aircraftName)).find(By.tagName(aircraftId)).getAttribute("href").replaceAll("\\D+", "");
            String howManyWavesIsCarried = String.valueOf(row.$$(By.className(wavesPerPlane)).size());
            collectedData.add(List.of(model, roteName, aircraftID, howManyWavesIsCarried));
        }
    }
}
