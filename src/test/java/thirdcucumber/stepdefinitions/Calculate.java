
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
import thirdcucumber.pageobjects.Desmos1;

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
        this.driver = Hooks.driver;
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

    public static class DriverManager {
        private static ThreadLocal<WebDriver> driver = new ThreadLocal<WebDriver>();
        private static Properties properties;
        private final String propertyFilePath= "src\\test\\resources\\configuration.properties";
        public DriverManager(){
            BufferedReader reader = null;
            try {
                System.out.println(propertyFilePath);
                reader = new BufferedReader(new FileReader(propertyFilePath));
                properties = new Properties();
                try { properties.load(reader); }
                catch (IOException e) { e.printStackTrace(); }
            } catch (FileNotFoundException e) {
                throw new RuntimeException("Properties file not found at path : " + propertyFilePath);
            }finally {
                try { if(reader != null) reader.close(); }
                catch (IOException ignore) {}
            }
        }
        public static String getSamplePath(){
            String samplePath = properties.getProperty("samplePath");
            if(samplePath!= null) return samplePath;
            else throw new RuntimeException("Test Data Resource Path not specified in the Configuration.properties file for the Key:testDataResourcePath");
        }
        private static enum Browser {
            FIREFOX,
            CHROME,
            IE,
            ANDROID,
            IOS
        }

        public static WebDriver createInstance(String browserName) throws MalformedURLException {
            WebDriver driver;
            browserName = (browserName != null) ? browserName : "firefox";
            String version = System.getProperty("version");
            String deviceName = System.getProperty("device");
            switch (Browser.valueOf(browserName.toUpperCase())) {
                case ANDROID:
                    DesiredCapabilities capabilities = new DesiredCapabilities();
                    capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, "Android");
                    capabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION, version);
                    capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, deviceName);
                    capabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME, "UIAutomator2");
                    capabilities.setCapability(MobileCapabilityType.BROWSER_NAME, "Chrome");
                    URL url = new URL("http://127.0.0.1:4723/wd/hub");

                    driver = new AndroidDriver(url, capabilities);
                    break;
                case IOS:
                    DesiredCapabilities iosCap = new DesiredCapabilities();
                    iosCap.setCapability(MobileCapabilityType.PLATFORM_NAME, "iOS");
                    iosCap.setCapability(MobileCapabilityType.PLATFORM_VERSION, version);
                    iosCap.setCapability(MobileCapabilityType.AUTOMATION_NAME, "XCUITest");
                    iosCap.setCapability(MobileCapabilityType.BROWSER_NAME, "Safari");
                    iosCap.setCapability(MobileCapabilityType.DEVICE_NAME, deviceName);

                    URL iosUrl = new URL("http://127.0.0.1:4723/wd/hub");

                    driver = new IOSDriver(iosUrl, iosCap);
                case IE:
                    driver = new InternetExplorerDriver();
                    break;
                case CHROME:
                    System.setProperty("webdriver.chrome.driver", "src\\drivers\\chromedriver.exe");
                    driver = new ChromeDriver();
                    break;
                default:
                    System.setProperty("webdriver.gecko.driver", "src\\drivers\\geckodriver.exe");
                    driver = new FirefoxDriver();
                    break;
            }
            // maximize browser's window on start
            driver.manage().window().maximize();
            return driver;
        }

        public static WebDriver getDriver() {
            if (driver.get() == null) {
                String browserName = System.getProperty("browser", "chrome");
                try {
                    setWebDriver(createInstance(browserName));
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
            return driver.get();
        }

        public static void setWebDriver(WebDriver driver) {
            driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
            DriverManager.driver.set(driver);
        }

        public static void closeWebDriver()
        {
            driver.get().quit();
            driver.remove();
        }
        }

    public static class Hooks {
        public static WebDriver driver;

        @Before
        public void initTest(){
            driver = DriverManager.getDriver();
            this.driver.manage().window().maximize();


        }
        @After
        public void finishTC(Scenario scenario){
            if(scenario.isFailed()){
                final byte[] screenshots = ((TakesScreenshot)this.driver).getScreenshotAs(OutputType.BYTES);
                scenario.embed(screenshots, "image/png");
            }
            DriverManager.closeWebDriver();
        }
    }

    public static class JsonDataReader {
        DriverManager driverManager = new DriverManager();
        private final String customerFilePath = driverManager.getSamplePath()+ "Sample.json";
        private List<Sample> sampleList;

        public JsonDataReader(){
            sampleList = getSampleData();
        }

        private List<Sample> getSampleData() {
            Gson gson = new Gson();
            BufferedReader bufferReader = null;
            try {
                bufferReader = new BufferedReader(new FileReader(customerFilePath));
                Sample[] samples = gson.fromJson(bufferReader, Sample[].class);
                return Arrays.asList(samples);
            }catch(FileNotFoundException e) {
                throw new RuntimeException("Json file not found at path : " + customerFilePath);
            }finally {
                try { if(bufferReader != null) bufferReader.close();}
                catch (IOException ignore) {}
            }
        }

        public final Sample getSample(String sampleId){
            for(Sample sample : sampleList) {

                if(sample.getSample().equalsIgnoreCase(sampleId)) return sample;
            }
            return null;
        }


    }
}
