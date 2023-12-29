package selenium4_SelfPracticing;

import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.v117.emulation.Emulation;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class GeoLocation {
    ChromeDriver driver;

    @Test
    public void setGeoLocation()
    {   Map locationCoordinates = new HashMap()
        {{put("latitude",30.082620);
          put("longitude",31.202700);
          put("accuracy",1);
        }};
        driver=new ChromeDriver();
        driver.executeCdpCommand("EmulationTest.setGeolocationOverride",
                locationCoordinates
                );
        driver.get("https://www.where-am-i.net");
        driver.findElement(By.xpath("/html/body/div/div[2]/div[1]/div[2]/div[2]/button[2]")).click();
        driver.findElement(By.id("btnMyLocation")).click();
    }

    @Test
    public void mockGeoLocation_DevTools(){
        driver=new ChromeDriver();
        DevTools devTools = driver.getDevTools();
        devTools.createSession();
        devTools.send(Emulation.setGeolocationOverride(Optional.of(52.5043),
                Optional.of(13.4501),
                Optional.of(1)));
        driver.get("https://my-location.org/");
    }
}
