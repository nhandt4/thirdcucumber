package thirdcucumber.pageobjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

public class Desmos1 {
    WebDriver driver;
    private WebDriverWait waitFunc;
    @FindBy(xpath = "//div[@dcg-command='clearall']")
    public WebElement btnClearall;
    @FindAll(
            @FindBy(xpath = "//span[@class='dcg-keypad-btn dcg-btn-dark-on-gray']")
    )
    public List<WebElement> digitElements;

    @FindAll(
            @FindBy(xpath = "//span[@class='dcg-mq-root-block']/span[@class='dcg-mq-binary-operator']/following-sibling::span")
    )
    public List<WebElement> resultElements;


    @FindAll(
            @FindBy(xpath = "//span[@class='dcg-keypad-btn dcg-btn-light-on-gray']")
    )
    public List<WebElement> operatorElements;

    public Desmos1(WebDriver driver)
    {   this.driver = driver;
        PageFactory.initElements(this.driver,this);
        this.waitFunc = new WebDriverWait(this.driver, 20);

    }
    public void enterInput(String operand1, String operand2, String operator){
        this.enterNumber(operand1);
        WebElement operatorEle = getElement(operatorElements, operator);
        if (operatorEle !=null){
            waitFunc.until(ExpectedConditions.elementToBeClickable(operatorEle)).click();
        }
        this.enterNumber(operand2);

    }
    public String getResult(){
        String result = "";
        for(WebElement ele: resultElements){
            result +=waitFunc.until(ExpectedConditions.visibilityOf(ele)).getText().trim();
        }
        return result;
    }
    public void clickClearall(){
        btnClearall.click();
    }

    public void enterNumber(String number){
        System.out.println(number.length());
        for(int i = 0; i < number.length(); i++){
            String s = String.valueOf(number.charAt(i));
            System.out.println(s);
            WebElement soDuElement= getElement(digitElements, String.valueOf(number.charAt(i)));
            if (soDuElement != null)
                waitFunc.until(ExpectedConditions.elementToBeClickable(soDuElement)).click();
        }
    }

    public WebElement getElement(List<WebElement> lstElements, String compare){
        System.out.println(compare);
        for(WebElement ele: lstElements){
            if(compare.equalsIgnoreCase(waitFunc.until(ExpectedConditions.visibilityOf(ele)).getAttribute("dcg-command")))
                return ele;
        }
        return null;
    }
}
