package selenium4_SelfPracticing;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.v119.network.Network;
import org.openqa.selenium.devtools.v119.network.model.ConnectionType;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Optional;

public class NetworkSpeed
{
    ChromeDriver driver ;
    DevTools devTools;
    WebDriverWait wait;
    @BeforeMethod
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        driver= new ChromeDriver();
        //get the DevTools & create a session
        devTools = driver.getDevTools();
        devTools.createSession();
    }

    @Test
    public void slowDownNetwork()
    {
        devTools.send(Network.enable(Optional.empty(),Optional.empty(),Optional.empty() ));
        devTools.send(Network.emulateNetworkConditions(false,
                3000,
                20000,
                10000,
                Optional.of(ConnectionType.CELLULAR2G)));
        driver.get("https://rahulshettyacademy.com/angularAppdemo");
        driver.findElement(By.cssSelector("button[routerlink*='library']")).click();
    }

    @Test
    public void blockNetwork()
    {
        devTools.send(Network.enable(Optional.empty(),Optional.empty(),Optional.empty() ));
        devTools.send(Network.emulateNetworkConditions(true,
                3000,
                20000,
                10000,
                Optional.of(ConnectionType.CELLULAR2G)));
        devTools.addListener(Network.loadingFailed(),loadingFailed ->
        {
            System.out.println(loadingFailed.getErrorText());
        });
        driver.get("https://rahulshettyacademy.com/angularAppdemo");
        driver.findElement(By.cssSelector("button[routerlink*='library']")).click();
    }

    @AfterMethod
    public void tearDown() {
        driver.quit();
    }
}
