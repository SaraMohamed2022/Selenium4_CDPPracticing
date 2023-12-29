package selenium4_SelfPracticing;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.v119.emulation.Emulation;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import java.time.Duration;
import java.util.Optional;

import static org.testng.Assert.assertEquals;

public class EmulationTest {
    ChromeDriver driver ;
    WebDriverWait wait ;
    @BeforeClass
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        driver= new ChromeDriver();
    }

    @Test
    public void testingEmulation() throws InterruptedException {
        //get the DevTools & create a session
        DevTools devTools = driver.getDevTools();
        devTools.createSession();
        By libraryLocator = By.linkText("Library");

        devTools.send(Emulation.setDeviceMetricsOverride(
                600,
                1000,
                50,
                true,
                Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty()
                ));
        driver.get("https://rahulshettyacademy.com/angularAppdemo/");
        driver.findElement(By.cssSelector(".navbar-toggler")).click();
        wait=new WebDriverWait(driver, Duration.ofSeconds(3));
        wait.until(ExpectedConditions.elementToBeClickable(libraryLocator));
        driver.findElement(libraryLocator).click();
        assertEquals(driver.getCurrentUrl(),"https://rahulshettyacademy.com/angularAppdemo/library");
    }

    @AfterClass
    public void tearDown() {
        WebDriverManager.chromedriver().setup();
        driver.quit();
    }
    }
