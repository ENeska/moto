package pages;

import Core.Waiter;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.internal.Coordinates;
import org.openqa.selenium.interactions.internal.Locatable;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import org.testng.Reporter;

import java.util.ArrayList;
import java.util.List;

public class BasePage {
    private Actions action;
    protected WebDriver driver;
    protected Waiter waiter;
    public static String browserContext;

    public BasePage(WebDriver driver) {
        this.driver = driver;
        waiter = new Waiter(driver);
        PageFactory.initElements(driver, this);
        this.action = new Actions(driver);
        browserContext = getBrowserNameFromUserAgent();
    }

    protected void hoverAndClickOnWebElement(WebElement element) {
        waiter.waitForWebElementVisibility(element);
        new Actions(driver).moveToElement(element).build().perform();
    }

    protected void hoverOnWebElement(WebElement element) {
        waiter.waitForWebElementVisibility(element);
        action.moveToElement(element).build().perform();
    }

    protected Actions getAction() {
        return action;
    }

    protected List<String> getStringTextListFromWebElementListWithMoveToElement(List<WebElement> list) {
        List<String> stringList = new ArrayList<>();
        for (WebElement element : list) {
            moveViewToElementAccordingCoordinates(element);
            stringList.add(element.getText().replace("add ", "").replace(" help_outline", ""));
            Assert.assertTrue(element.getText().length() > 0 && !element.getText().equals(" "), "Nie potrafię odczytać textu z elementu.");
        }
        return stringList;
    }

    public static void moveViewToElementAccordingCoordinates(WebElement element) {
        Coordinates cor = ((Locatable) element).getCoordinates();
        cor.inViewPort();
    }

    public void scrollElementIntoView(WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
    }

    protected String getBrowserNameFromUserAgent() {
        String userAgent = (String) ((JavascriptExecutor) driver).executeScript("return navigator.userAgent");
        userAgent = userAgent.toLowerCase();
        String browser = "";
        if (userAgent.contains("firefox")) {
            browser = "firefox";
        } else if (userAgent.contains("chrome")) {
            browser = "chrome";
        }
        return browser;
    }

    public void clearInputSendingBackSpace(WebElement input) {
        int textLength = input.getText().length();
        for (int i = 0; i <= textLength + 1; i++) {
            input.sendKeys(Keys.BACK_SPACE);
        }
    }

    protected void click(WebElement element) {
        Reporter.log("Klikam element: " + element.toString());
        element.click();
    }

    protected String getText(WebElement element)
    {
        action.moveToElement(element).build().perform();
        return element.getText();
    }

    protected void sendKeys(WebElement element, String text) {
        Reporter.log(" Wpisuję " + text);
        waiter.waitForWebElementVisibility(element);
        element.click();
        element.clear();
        element.sendKeys(text);
    }

    protected void pressKey(WebElement element, Keys key) {
        Reporter.log("Naciskam klawisz " + key.name());
        waiter.wait(300);
        element.sendKeys(key);
        waiter.wait(300);
    }

    protected void pressKey(WebElement element, Keys key, int milliseconds) {
        Reporter.log("Naciskam klawisz " + key.name());
        element.sendKeys(key);
        waiter.wait(milliseconds);
    }

    protected void pressTwiceKey(WebElement element, Keys key) {
        Reporter.log("Naciskam dwa razy klawisz " + key.name());
        element.sendKeys(key);
        waiter.wait(300);
        element.sendKeys(key);
        waiter.wait(300);
    }
}
