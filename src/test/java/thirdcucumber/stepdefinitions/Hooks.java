package thirdcucumber.stepdefinitions;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import thirdcucumber.common.DriverManager;

public class Hooks {

    @Before
    public void initTest(){



    }
    @After
    public void finishTC(Scenario scenario){
        if(scenario.isFailed()){
            final byte[] screenshots = ((TakesScreenshot)DriverManager.getDriver()).getScreenshotAs(OutputType.BYTES);
            scenario.embed(screenshots, "image/png");
        }
        DriverManager.closeWebDriver();
    }
}
