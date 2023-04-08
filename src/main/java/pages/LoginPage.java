package pages;

import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;

public class LoginPage {
    String dismissCookie = "//a[@aria-label='dismiss cookie message']";
    String userNameField = "username";
    String passwordField = "password";
    String loginButton = "loginSubmit";

    public void login(String email, String password) {
        $(By.xpath(dismissCookie)).click();
        $(By.id(userNameField)).setValue(email);
        $(By.id(passwordField)).setValue(password);
        $(By.id(loginButton)).click();
    }

}