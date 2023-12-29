package selenium4_SelfPracticing;

import com.google.common.collect.ImmutableList;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.v119.network.Network;
import org.openqa.selenium.devtools.v119.fetch.model.RequestPattern;
import org.openqa.selenium.devtools.v119.network.model.ErrorReason;
import org.openqa.selenium.devtools.v119.network.model.Response;
import org.openqa.selenium.devtools.v119.fetch.Fetch;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.*;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;

public class NetworkLogsActivity {
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
    public void getNetworkLogsActivity() {
        devTools.send(Fetch.enable(Optional.empty(), Optional.empty()));
        devTools.addListener(Network.responseReceived(),response ->
        {
            Response requestResponse = response.getResponse();
            System.out.println(requestResponse.getStatus());
            System.out.println(requestResponse.getUrl());
            assertFalse(requestResponse.getStatus().toString().startsWith("4"));
        });
        driver.get("https://rahulshettyacademy.com/angularAppdemo");
        driver.findElement(By.cssSelector("button[routerlink*='library']")).click();
    }

    @Test
    public void mockingNetworkLogs()
    {
        devTools.send(Fetch.enable(Optional.empty(), Optional.empty()));
        //Pause the normal request
        // Update the request
        //Continue sending your updated request
        devTools.addListener(Fetch.requestPaused(), request ->
        {
            if(request.getRequest().getUrl().contains("shetty"))
            {
                String mockedUrl = request.getRequest().getUrl().replace("=shetty", "=BadGuy");
                System.out.println("Mocked URL " + mockedUrl);
                devTools.send(Fetch.continueRequest(request.getRequestId() ,
                        Optional.of(mockedUrl),
                        Optional.of(request.getRequest().getMethod()),
                        Optional.empty(),
                        Optional.empty(),
                        Optional.empty()));
            }

            else
                devTools.send(Fetch.continueRequest(request.getRequestId() ,
                        Optional.of(request.getRequest().getUrl()),
                        Optional.of(request.getRequest().getMethod()),
                        Optional.empty(),
                        Optional.empty(),
                        Optional.empty()));
        });
        driver.get("https://rahulshettyacademy.com/angularAppdemo/");
        driver.findElement(By.cssSelector("button[routerlink*='library']")).click();
        wait=new WebDriverWait(driver, Duration.ofSeconds(3));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.tagName("p")));
        assertEquals(driver.findElement(By.tagName("p")).getText(),"Oops only 1 Book available");
    }

    @Test
    public void testFailedNetworkRequests()
    {
        //Define using pattern request class the targeted API
        //Send enable request with that pattern
        // Add listener with request pause to the whole requests and fail the targeted one using the request id
        // Continue sending the API after failing it

       Optional <List<RequestPattern>> pattern = Optional.of(Arrays.asList(new RequestPattern(Optional.of("*GetBook*"), Optional.empty(), Optional.empty())));
        devTools.send(Fetch.enable(pattern, Optional.empty()));
        devTools.addListener(Fetch.requestPaused(), request ->
        {   //Fail a request with specifying reason
            devTools.send(Fetch.failRequest(request.getRequestId(), ErrorReason.FAILED));
        });
        driver.get("https://rahulshettyacademy.com/angularAppdemo");
        driver.findElement(By.cssSelector("button[routerlink*='library']")).click();
    }

    @Test
    public void blockingUnwantedNetworkRequests()
    {
        System.out.println("Time before blocking images and css " + System.currentTimeMillis());
        devTools.send(Network.enable(Optional.empty(), Optional.empty(),Optional.empty()));
        devTools.send(Network.setBlockedURLs(ImmutableList.of("*.jpg", "*.css")));
        driver.get("https://rahulshettyacademy.com/angularAppdemo");
        driver.findElement(By.linkText("Browse Products")).click();
        driver.findElement(By.linkText("Selenium")).click();
        driver.findElement(By.cssSelector(".add-to-cart")).click();
        assertEquals(driver.findElement(By.tagName("p")).getText(),"THIS PRODUCT IS ALREADY ADDED TO CART");
        System.out.println("Time after blocking images and css " + System.currentTimeMillis());
    }


    @AfterMethod
    public void tearDown() {
        driver.quit();
    }
}


