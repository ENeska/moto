package Core;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Reporter;

import java.net.URL;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;

public class Waiter {
    private static int timeout = 5;
    private WebDriver driver;

    public Waiter(WebDriver driver) {
        this.driver = driver;
    }

    private FluentWait<WebDriver> getWait() {
        return new FluentWait<>(this.driver)
                .withTimeout(Duration.ofSeconds(timeout))
                .pollingEvery(Duration.ofMillis(200))
                .ignoring(NoSuchElementException.class);
    }

    public void waitForWebElementVisibilityBy(By by) {
        getWait().until(ExpectedConditions.visibilityOfElementLocated((by)));
    }

    public void waitForWebElementVisibility(WebElement element) {
        getWait().until(ExpectedConditions.visibilityOf(element));
    }

    public void waitForWebElementVisibility(By by) {
        getWait().until(ExpectedConditions.visibilityOfElementLocated(by));
    }

    public void waitForWebElementInvisibility(WebElement element) {
        getWait().until(ExpectedConditions.invisibilityOf(element));
    }

    public void waitForWebElementTextVisibility(WebElement element, String text) {
        getWait().until(ExpectedConditions.textToBePresentInElement(element, text));
    }

    public void waitForWebElementTextLength(WebElement element, int length) {
        getWait().until(textShouldBeLongerThanPassedLength(element, length));
    }

    public void waitForElementPresenceBy(By by) {
        getWait().until(ExpectedConditions.presenceOfElementLocated((by)));
    }

    public void waitForWebElementToBeClickable(WebElement element) {
        getWait().until(ExpectedConditions.elementToBeClickable(element));
    }

    public void waitForWebElementToBeSelected(WebElement element) {
        getWait().until(ExpectedConditions.elementToBeSelected(element));
    }

    public void waitForWebElementToBeClickable(By by) {
        getWait().until(ExpectedConditions.elementToBeClickable(by));
    }

    public static void wait(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public ExpectedCondition<Boolean> isAnimationEnded() {
        return new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver driver) {
                return isAnimationEnd();
            }

            public String toString() {
                return String.format("Animation never ended");
            }
        };
    }

    protected boolean isAnimationEnd() {
        try {
            enablejQuery(driver);
            return (boolean) ((JavascriptExecutor) driver).executeScript("return $(\":animated\").length == 0");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    public static void enablejQuery(WebDriver driver) throws Exception {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        String readyState = js.executeScript("return document.readyState").toString();
        if (!isjQueryLoaded(driver)) {
            URL jqueryUrl = Resources.getResource("jquery-3.1.1.min.js");
            System.out.println(jqueryUrl.getPath());
            String jqueryText = Resources.toString(jqueryUrl, Charsets.UTF_8);
            js.executeScript(jqueryText);
        }
    }

    public static Boolean isjQueryLoaded(WebDriver driver) throws Exception {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        return (Boolean) js.executeScript("return !!window.jQuery;");
    }

    public static ExpectedCondition<Boolean> textShouldBeLongerThanPassedLength(WebElement element, int length) {
        return new ExpectedCondition<Boolean>() {
            private int currentTextLength = 0;

            public Boolean apply(WebDriver driver) {
                this.currentTextLength = element.getText().length();
                return currentTextLength > length;
            }

            public String toString() {
                return String.format("Text to be longer than \"%s\". Current text: \"%s\"", length, this.currentTextLength);
            }
        };
    }

    public void waitUntilFieldTextIsEqual(int waitTimeSec, String expectedValue, WebElement element) {
        await()
                .atMost(waitTimeSec, TimeUnit.SECONDS)
                .until(() -> element.getAttribute("value").equals(expectedValue));
    }

    public void waitForFirefox(int timeInSec, String browserName)
    {
        if (browserName.equals("firefox"))
        {
            wait(timeInSec*1000);
        }
    }
}
