package Core;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.ITestContext;

import java.util.concurrent.TimeUnit;

public class WebDriverFactory {
    private ITestContext ctx;

    public WebDriverFactory(ITestContext ctx) {
        this.ctx = ctx;
    }

    public WebDriver getBaseWebDriver(String browser) {
        WebDriver driver;
        browser = browser.toLowerCase();

        if (browser.equals("chrome")) {
            WebDriverManager.chromedriver().setup();
            driver = new ChromeDriver();
        } else {
            WebDriverManager.firefoxdriver().setup();
            driver = new FirefoxDriver();
        }
        setDimensions(driver);
        driver.manage().timeouts().implicitlyWait(3000, TimeUnit.MILLISECONDS);

        return driver;
    }

    private void setDimensions(WebDriver driver) {
        String dimensionsString = ctx.getCurrentXmlTest().getParameter("dimensions");
        try {
            String stringWidth = dimensionsString.split("x")[0];
            String stringHeight = dimensionsString.split("x")[1];
            int width = Integer.parseInt(stringWidth);
            int height = Integer.parseInt(stringHeight);
            driver.manage().window().setSize(new Dimension(width, height));
        } catch (Exception e) {
            System.out.println("Nie duało się zmienić rozdzielczści. Rozdzielczość: " + dimensionsString + " context: " + ctx.getCurrentXmlTest().toString() + " " + e);
            System.out.println("Ustawiam domyslna (pelny ekran)!");
            driver.manage().window().maximize();
        }
    }

    public Dimension getDimensions(WebDriver driver) {
        String dimensionsString = ctx.getCurrentXmlTest().getParameter("dimensions");
        int width;
        int height;
        try {
            String stringWidth = dimensionsString.split("x")[0];
            String stringHeight = dimensionsString.split("x")[1];
            width = Integer.parseInt(stringWidth);
            height = Integer.parseInt(stringHeight);
        } catch (Exception e) {
            driver.manage().window().maximize();
            height = driver.manage().window().getSize().height;
            width = driver.manage().window().getSize().width;
        }

        return new Dimension(width, height);
    }

}
