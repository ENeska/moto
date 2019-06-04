package pages;

import Core.Properties;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.testng.Reporter;

public class MainPage extends BasePage {

    @FindBy(css = ".select2-results__options li:nth-child(1)")
    private WebElement polishLanguageOption;

    public MainPage(WebDriver driver) {
        super(driver);
    }
    public MainPage launch() {
        driver.get(Properties.appUrl);
        Reporter.log("Wprowadzam adres strony: " + Properties.appUrl);
        return this;
    }

}
