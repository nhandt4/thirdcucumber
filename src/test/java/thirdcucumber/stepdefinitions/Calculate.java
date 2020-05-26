
package thirdcucumber.stepdefinitions;

import com.google.gson.Gson;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.remote.MobileCapabilityType;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import thirdcucumber.common.DriverManager;
import thirdcucumber.pageobjects.Desmos1;
import thirdcucumber.pageobjects.JsonDataReader;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class Calculate {
    WebDriver driver;
    public Sample sample;
    public Desmos1 desmos;
    public Calculate(){
        this.driver = DriverManager.getDriver();
        this.desmos = new Desmos1(this.driver);
    }
    @Given("The Scientific page is displayed on the browser")
    public void theScientificPageIsDisplayedOnTheBrowser() {
        this.driver.get("https://www.desmos.com/scientific");
        this.driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
    }

    @When("User get the {string} data by reading the json data file")
    public void userGetTheDataByReadingTheJsonDataFile(String sample1) {
        JsonDataReader jsonDataReader = new JsonDataReader();
        sample = jsonDataReader.getSample(sample1);



    }

    @And("User attempt to perform the operator functionality")
    public void userAttemptToPerformTheOperatorFunctionality() {
        desmos.enterInput(sample.getOperand1(), sample.getOperand2(), sample.getOperator());

    }

    @Then("The result of the operator matches with the {string} result in the json data file")
    public void theResultOfTheOperatorMatchesWithTheResultInTheJsonDataFile(String sample1) {
        Assert.assertEquals(sample.getResult(), desmos.getResult());
        desmos.clickClearall();
    }
}
