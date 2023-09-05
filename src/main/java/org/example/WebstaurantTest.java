package org.example;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class WebstaurantTest
{

    public static void main(String[] args) {

        //Setup WebDriver using WebDriverManager
        WebDriverManager.chromedriver().setup();
        WebDriver driver = new ChromeDriver();

        driver.manage().window().maximize();

        try {
            //Opening the website
            driver.get("https://www.webstaurantstore.com/");

            //Searching for 'stainless work table'
            WebElement searchInput = driver.findElement(By.id("searchval"));
            searchInput.sendKeys("stainless work table");
            searchInput.submit();

            // Checking the results have the word "Table" in its title
            java.util.List<WebElement> productTitles = driver.findElements(By.cssSelector(".product-title"));
            boolean allTitlesContainTable = true;
            for (WebElement title : productTitles) {
                if (!title.getText().toLowerCase().contains("table")) {
                    allTitlesContainTable = false;
                    break;
                }
            }
            if (allTitlesContainTable) {
                System.out.println("All products have 'Table' in the title.");
            } else {
                System.out.println("Not all products have 'Table' in the title.");
            }

            //Adding the last item to the Cart
            java.util.List<WebElement> addToCartButtons = driver.findElements(By.cssSelector("input[data-testid='itemAddCart']"));
            if (!addToCartButtons.isEmpty()) {
                WebElement lastAddToCartButton = addToCartButtons.get(addToCartButtons.size() - 1);
                lastAddToCartButton.click();
            }

            // Opening the Cart

            driver.get("https://www.webstaurantstore.com/viewcart.cfm");

            // Waiting for the empty cart button to be visible
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
            wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector(".emptyCartButton")));

            // Empty Cart
            WebElement emptyCartButton = driver.findElement(By.cssSelector(".emptyCartButton"));
            emptyCartButton.click();

            //Tried to do This line of code to catch the modal and close it.
            //Had to solve it differently since I could not catch the alert and there is no id at all
            //driver.switchTo().alert().accept();

            // wait to let the modal box be visible
            WebDriverWait waitModal = new WebDriverWait(driver, Duration.ofSeconds(5));
            waitModal.until(ExpectedConditions.visibilityOfElementLocated(
                    By.className("ReactModal__Content")));


            // Fetching the modal content
            System.out.println("Fetching modal body content and asserting it");
            WebElement modalContentBody = driver.findElement(By.className("ReactModal__Content"));

            // Finding the button to Empty the cart and close the modal
            WebElement modalAcceptButton = modalContentBody.findElement(By.xpath(".//button[contains(text(),'Empty')]"));
            modalAcceptButton.click();
            System.out.println("Test Finished correctly");


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Close the browser
            driver.quit();
        }
    }
}

