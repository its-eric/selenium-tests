package xyz.nagyadam.test;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
//import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Search Google example.
 *
 * @author Rahul
 */
public class Main {
    static WebDriver driver;
    static Wait<WebDriver> wait;

    public static void main(String[] args) {
        driver = new HtmlUnitDriver();
        wait = new WebDriverWait(driver, 30);

        String url = "https://www.google.com/";
        driver.get(url);

        boolean result;
        try {
            result = firstPageContainsQAANet() &&
                     pageContainsHelloWorld() &&
                     ableToRequestNewPassword() &&
                     canSelectManyColors();
        } catch(Exception e) {
            e.printStackTrace();
            result = false;
        } finally {
            driver.close();
        }

        System.out.println("Tests " + (result? "passed." : "failed."));
        if (!result) {
            System.exit(1);
        }
    }

    private static boolean firstPageContainsQAANet() {
        //type search query
        driver.findElement(By.name("q")).sendKeys("qa automation\n");

        // click search
        driver.findElement(By.name("btnG")).click();

        // Wait for search to complete
        wait.until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver webDriver) {
                System.out.println("Searching ...");
                return webDriver.findElement(By.id("resultStats")) != null;
            }
        });

        // Look for QAAutomation.net in the results
        return driver.findElement(By.tagName("body")).getText().contains("qaautomation.net");
    }

    private static boolean pageContainsHelloWorld() {
        driver.get("http://selenium.thinkcode.se/helloWorld");
        return driver.findElement(By.id("headline")).getText().contains("Hello, world!");
    }
    
    private static boolean ableToRequestNewPassword() {
        driver.get("http://selenium.thinkcode.se/requestPassword");
        driver.findElement(By.id("account")).sendKeys("test");
        driver.findElement(By.name("submit")).click();

        wait.until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver webDriver) {
                System.out.println("Submitting form ...");
                return driver.findElement(By.id("confirmation")) != null;
            }
        });

        return driver.findElement(By.id("confirmation")).getText().contains("A new password has been sent to test");
    }

    private static boolean canSelectManyColors() {
        driver.get("http://selenium.thinkcode.se/selectColor");
        driver.findElement(By.cssSelector("[value=red]")).click();
        driver.findElement(By.cssSelector("[value=green]")).click();
        driver.findElement(By.cssSelector("[value=Submit]")).click();

        wait.until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver webDriver) {
                System.out.println("Submitting form ...");
                return driver.findElements(By.name("color")) != null;
            }
        });

        
        for (WebElement element : driver.findElements(By.name("color"))) {
            if(! element.getText().contains("red") && ! element.getText().contains("green")) return false;
        }
        
        return true;
    }

}

