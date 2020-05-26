package thirdcucumber.common;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.remote.MobileCapabilityType;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import thirdcucumber.stepdefinitions.Calculate;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class DriverManager {
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
        browserName = "ANDROID";//(browserName != null) ? browserName : "ANDROID";
        String version = System.getProperty("version");
        String deviceName = System.getProperty("device");
        switch (DriverManager.Browser.valueOf(browserName.toUpperCase())) {
            case ANDROID:
                DesiredCapabilities capabilities = new DesiredCapabilities();
                capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, "Android");
                capabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION, "10");
                capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, "R58M42Z22SX");
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

