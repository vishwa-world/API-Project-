package qtriptest.pages;


import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.AjaxElementLocatorFactory;

import qtriptest.SeleniumWrapper;

public class LoginPage {
    ChromeDriver driver;

    @FindBy(id="floatingInput")
    WebElement emailTextBox;

    @FindBy(id="floatingPassword")
    WebElement passwordTextBox;
    
    @FindBy(xpath = "//button[text()='Login to QTrip']")
    WebElement loginButton;

    public LoginPage(ChromeDriver driver)
    {
     this.driver = driver;   
     driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
     PageFactory.initElements(new AjaxElementLocatorFactory(driver, 20), this);
    }

    public void performLogin(String username , String password)
    {
        SeleniumWrapper.sendKeys(emailTextBox, username);
        SeleniumWrapper.sendKeys(passwordTextBox, password);
        try {
            SeleniumWrapper.click(loginButton, this.driver);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
