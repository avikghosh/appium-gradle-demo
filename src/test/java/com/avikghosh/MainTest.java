package com.avikghosh;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class MainTest {
    private AndroidDriver<AndroidElement> driver;

    @BeforeTest
    public void setupAppium() throws MalformedURLException {
        System.out.println("In Before Test, setting up Appium Driver");
        DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
        desiredCapabilities.setCapability("platformName", "Android");
        desiredCapabilities.setCapability("deviceName", "emulator-5554");
        desiredCapabilities.setCapability("automationName", "UiAutomator2");
        desiredCapabilities.setCapability("appPackage", "com.android.dialer");
        desiredCapabilities.setCapability("appActivity", ".DialtactsActivity");
        driver = new AndroidDriver<>(new URL("http://0.0.0.0:4723/wd/hub"), desiredCapabilities);

    }

    @Test
    public void appiumDemoTest() {
        int screenshotCounter = 1;
        System.out.println("Screenshots will be output to : " + System.getProperty("user.dir") + "/screenshots/");
        By addFavoriteLink = By.id("com.android.dialer:id/emptyListViewAction");
        By emptyContactMsg = By.id("com.android.dialer:id/emptyListViewMessage");
        String expectedText = "You don't have any contacts yet";
        WebDriverWait wait = new WebDriverWait(driver, 5);
        ExpectedCondition addFavoriteCondition = ExpectedConditions.presenceOfElementLocated(addFavoriteLink);
        wait.until(addFavoriteCondition);
        saveScreenshot(screenshotCounter++);
        driver.findElement(addFavoriteLink).click();
        saveScreenshot(screenshotCounter++);
        ExpectedCondition emptyContactsCondition = ExpectedConditions.presenceOfElementLocated(emptyContactMsg);
        wait.until(emptyContactsCondition);
        String actualText = driver.findElement(emptyContactMsg).getText();
        try {
            Assert.assertEquals(actualText, expectedText);
            System.out.println("Empty Contacts Text Assertion Passed, Actual: " + actualText + " Expected: "+ expectedText);
        } catch (AssertionError e) {
            Assert.fail("Empty Contacts Text Assertion Failed, Actual: " + actualText + " Expected: "+ expectedText);
        }
        saveScreenshot(screenshotCounter++);

    }

    @AfterTest
    public void tearDown() {
        System.out.println("In After Test, closing driver");
        driver.quit();
    }

    private void saveScreenshot(int screenshotCounter) {
        try {
            File screenshot = driver.getScreenshotAs(OutputType.FILE);
            FileUtils.copyFile(screenshot, new File(System.getProperty("user.dir") + "/screenshots/"+ screenshotCounter + ".png"));
        } catch (IOException e) {
            System.out.println("There was an exception in copying the file to the required directory" + e.getMessage());
        }
    }
}
