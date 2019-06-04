package Functional;

import Core.Waiter;
import Core.WebDriverFactory;
import Core.WebDriverListener;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.*;
import org.testng.xml.XmlTest;
import pages.MainPage;

import java.lang.reflect.Method;

public class BaseTest {
    private WebDriver driver;
    private EventFiringWebDriver e_driver;
    private WebDriverFactory driverFactory;
    protected MainPage mainPage;

    @BeforeTest
    @Parameters(("browserName"))
    public void setUpBeforeTest(@Optional("chrome") String browser, ITestContext ctx, XmlTest xmlTest) {
        driverFactory = new WebDriverFactory(ctx);
        driver = driverFactory.getBaseWebDriver(browser);

        Waiter waiter = new Waiter(driver);
        WebDriverListener eventListener = new WebDriverListener(waiter);

        e_driver = new EventFiringWebDriver(driver);
        e_driver.register(eventListener);

        mainPage = new MainPage(e_driver);

    }

    @AfterTest
    public void cleanUpAfterTest() {
        if (driver.toString().contains("Firefox")) {
            driver.quit();
        } else {
            driver.close();
            driver.quit();
        }
    }

    private void testResult(ITestResult testResult, Method method) {
        if (testResult.getStatus() == ITestResult.FAILURE) {
            System.out.println("\n\nTest nie przeszedl!");
            System.out.println("Przyczyna problemu:" + testResult.getThrowable());
            System.out.println("Dane testowe z xml:" + method.getName());

            Object[] params = testResult.getParameters();
            for (int i = 0; i < params.length; i++) {
                try {
                    String a = (String) params[i];
                    System.out.println("Parametr: " + i + ": " + a);
                } catch (ClassCastException ex) {
                    System.out.println("Nie potrafie wypisac parametrow");
                }
            }
            if (testResult.getStatus() == ITestResult.SUCCESS) {
                System.out.println("\n\nTest przeszedl!");
            }
        }
    }

    private void refreshPage(XmlTest xmlTest) {
            System.out.println("Odswiezam strone");
            e_driver.navigate().refresh();
    }

}