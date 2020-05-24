package thirdcucumber;

import io.cucumber.junit.CucumberOptions;
import io.cucumber.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        plugin = {"pretty", "html:target/report"},
        glue = {"thirdcucumber/stepdefinitions"},
        features = "src\\test\\resources\\thirdcucumber\\caculate.feature")
public class RunCucumberTest {
}
