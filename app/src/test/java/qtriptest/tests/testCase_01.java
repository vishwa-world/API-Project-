package qtriptest.tests;

import qtriptest.DP;
import qtriptest.DriverFactory;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.*;
import org.testng.asserts.SoftAssert;

import qtriptest.pages.HomePage;
import qtriptest.pages.LoginPage;
import qtriptest.pages.RegisterPage;

public class testCase_01 {
    static ChromeDriver driver;

    @BeforeTest()
    public static void createDriver() {
        // IMPORTANT!: Enter the Driver Location here
        DriverFactory sbc1= DriverFactory.getInstanceOfSingletonBrowserClass();
		driver = sbc1.getDriver();
    }

    @Test(description = "Verify user registration - login - logout", dataProvider = "data-provider" , dataProviderClass = DP.class, groups = {"Login Flow"})
    public static void TestCase01(String UserName, String Password) throws InterruptedException {
        HomePage home = new HomePage(driver);
        home.gotoHomePage();
        Thread.sleep(2000);
        home.clickRegister();
        RegisterPage register = new RegisterPage(driver);
        register.registerNewUser(UserName, Password, Password, true);
        Thread.sleep(3000);//TODO:UNWANTED ALERT REMOVE
        //driver.switchTo().alert().dismiss();//TODO:UNWANTED ALERT REMOVE
        String username = register.lastGeneratedUsername;
        LoginPage Login = new LoginPage(driver);
        Login.performLogin(username, Password);
        Thread.sleep(3000);//TODO:UNWANTED ALERT REMOVE
        //driver.switchTo().alert().dismiss();//TODO:UNWANTED ALERT REMOVE
        Assert.assertTrue(home.isUserLoggedIn());
        home.logOutUser();
        Thread.sleep(3000);
        Assert.assertFalse(home.isUserLoggedIn());
        home.gotoHomePage();
    }

    @AfterSuite
    public static void quitDriver() throws InterruptedException
    {
        driver.manage().deleteAllCookies();
        driver.resetInputState();
    }

}
