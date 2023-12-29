package selenium4_SelfPracticing;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.v85.log.Log;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class CDPLogs {

        ChromeDriver driver ;

        @BeforeClass
        public void setUp() {
            WebDriverManager.chromedriver().setup();
            driver= new ChromeDriver();
            driver.manage().window().maximize();
        }

        @Test
    public void viewBrowserConcoleLogs(){
            //get the DevTools & create a session
            DevTools devTools = driver.getDevTools();
            devTools.createSession();

            // Enable the console logs
            devTools.send(Log.enable());

            // add a listener for the console logs
            devTools.addListener(Log.entryAdded() , logEntry -> {
                System.out.println("-----------");
                System.out.println("Level: " + logEntry.getLevel());
                System.out.println("Text: " + logEntry.getText());
                System.out.println("Broken URL : " + logEntry.getUrl());
            });
            //load the AUT
            driver.get("https://the-internet.herokuapp.com/broken_images");
        }


}
