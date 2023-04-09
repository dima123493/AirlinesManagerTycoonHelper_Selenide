package pages;

import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;
import static java.lang.Long.parseLong;

public class RoutePricePage {
    String reliability = "//*[@id=\"marketing_linePricing\"]/div[5]/span[2]/p/a";
    String reliabilityStateRU = "Очень ненадежный";
    String reliabilityStateEN = "Very unreliable";
    String amountOfMoneyInMyAccount = "ressource3";
    String performAudit = "//*[@id=\"marketing_linePricing\"]/div[1]/a/span";//hover,click
    String auditPrice = "//*[@id=\"popupContainer\"]/div[2]/p/span";//hover,click
    String performAuditButton = "//div[@id='popupContainer']//span[@id='internalAuditButton']";
    String declineAuditButton = "//*[@id=\"popupContainer\"]/div[3]/span";

    String economyClassPrice = "line_priceEco";
    String businessClassPrice = "line_priceBus";
    String firstClassPrice = "line_priceFirst";
    String cargoPrice = "line_priceCargo";
    String submitPricesButton = "//*[@id=\"marketing_linePricing\"]/form/div/input";

    public void auditAnalysis() {
        $x(performAudit).hover().click();
    }

    public void performAudit() {
        $x(performAuditButton).hover().click();
    }

    public void applyNewPrices(String economyPrice, String businessPrice, String firstPrice, String crgPrice) {
        $(By.id(economyClassPrice)).setValue(economyPrice);
        $(By.id(businessClassPrice)).setValue(businessPrice);
        $(By.id(firstClassPrice)).setValue(firstPrice);
        $(By.id(cargoPrice)).setValue(crgPrice);
        $x(submitPricesButton).click();
    }

    public void auditProcedure() {
        long myAccountMoney;
        long auditPriceValue;
        if ($x(reliability).getText().equals(reliabilityStateRU) ||
                $x(reliability).getText().equals(reliabilityStateEN)) {
            myAccountMoney = parseLong(($(By.id(amountOfMoneyInMyAccount)).getText().replaceAll("(?<=.)\\D+","")));
            auditAnalysis();
            auditPriceValue = parseLong($x(auditPrice).getText().trim().replaceAll("\\D+", ""));
            if (auditPriceValue < myAccountMoney) {
                performAudit();
            } else {
                $x(declineAuditButton).hover().click();
            }
        }
    }

/*    public static void main(String[] args) {
        long test1 = parseLong("-33,696,140,341".replaceAll("(?<=.)\\D+",""));
        long test3 = parseLong("33,696,140,341".replaceAll("(?<=.)\\D+",""));
        long test2 = parseLong("140,341".replaceAll("\\D+", ""));
        System.out.println(test1);
        System.out.println(test2);
        if(test1>test2){
            System.out.println("cool. audit can be performed");
        }else{
            System.out.println("not enough money");
        }
    }*/

}
