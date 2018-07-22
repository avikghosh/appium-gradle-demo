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
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;

public class MainTest  {
    private AndroidDriver<AndroidElement> driver;

    @BeforeTest
    public void setupAppium() throws MalformedURLException {
        System.out.println("Setup Driver");
        DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
        desiredCapabilities.setCapability("platformName", "Android");
        desiredCapabilities.setCapability("deviceName", "emulator-5554");
        desiredCapabilities.setCapability("automationName", "UiAutomator2");
        desiredCapabilities.setCapability("appPackage", "com.android.dialer");
        desiredCapabilities.setCapability("appActivity", ".DialtactsActivity");
        driver = new AndroidDriver<>(new URL("http://0.0.0.0:4723/wd/hub"), desiredCapabilities);
        System.out.println("Driver Created Successfully");

    }

    @Test
    public void appiumDemoTest(Method method) {
        int screenshotCounter = 1;
        String testMethodName = method.getName();
        System.out.println("Screenshots will be output to : " + System.getProperty("user.dir") + "/screenshots/");
        By addFavoriteLink = By.id("com.android.dialer:id/emptyListViewAction");
        By emptyContactMsg = By.id("com.android.dialer:id/emptyListViewMessage");
        String expectedText = "You don't have any contacts yet";
        WebDriverWait wait = new WebDriverWait(driver, 5);
        ExpectedCondition addFavoriteCondition = ExpectedConditions.presenceOfElementLocated(addFavoriteLink);
        wait.until(addFavoriteCondition);
        saveScreenshot(testMethodName, screenshotCounter++);
        driver.findElement(addFavoriteLink).click();
        saveScreenshot(testMethodName, screenshotCounter++);
        ExpectedCondition emptyContactsCondition = ExpectedConditions.presenceOfElementLocated(emptyContactMsg);
        wait.until(emptyContactsCondition);
        String actualText = driver.findElement(emptyContactMsg).getText();
        try {
            Assert.assertEquals(actualText, expectedText);
            System.out.println("Empty Contacts Text Assertion Passed, Actual: " + actualText + " Expected: "+ expectedText);
        } catch (AssertionError e) {
            System.out.println("Empty Contacts Text Assertion Failed, Actual: " + actualText + " Expected: "+ expectedText);
            Assert.fail();
        }
        saveScreenshot(testMethodName, screenshotCounter++);

    }

    @AfterTest
    public void tearDown() {
        driver.quit();
        System.out.println("Driver teardown successful.");
    }

    private void saveScreenshot(String testMethodName, int screenshotCounter) {
        try {
            File screenshot = driver.getScreenshotAs(OutputType.FILE);
            FileUtils.copyFile(screenshot, new File(System.getProperty("user.dir") + "/screenshots/" + testMethodName + "/ " + screenshotCounter + ".png"));
            System.out.println("Screenshot successfully saved to " + System.getProperty("user.dir") + "/screenshots/" + testMethodName + "/ " + screenshotCounter + ".png");
        } catch (IOException e) {
            System.out.println("There was an exception in copying the file to the required directory" + e.getMessage());
        }
    }
}
