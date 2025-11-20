package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class AddUserPage {

    private WebDriver driver;
    private WebDriverWait wait;

    private By userRoleDropdown = By.xpath("//label[text()='User Role']/parent::div/following-sibling::div//div[contains(@class,'oxd-select-text')]");
    private By userRoleAdmin = By.xpath("//div[@role='option']//span[text()='Admin']");
    private By employeeNameInput = By.xpath("//input[@placeholder='Type for hints...']");
    private By employeeSuggestion = By.xpath("//div[@role='listbox']//span");
    private By usernameInput = By.xpath("//label[text()='Username']/parent::div/following-sibling::div//input");
    private By statusDropdown = By.xpath("//label[text()='Status']/parent::div/following-sibling::div//div[contains(@class,'oxd-select-text')]");
    private By statusEnabled = By.xpath("//div[@role='option']//span[text()='Enabled']");
    private By passwordInput = By.xpath("//label[text()='Password']/parent::div/following-sibling::div//input");
    private By confirmPasswordInput = By.xpath("//label[text()='Confirm Password']/parent::div/following-sibling::div//input");
    private By saveBtn = By.xpath("//button[normalize-space()='Save']");
    private By loggedUserName = By.xpath("//p[@class='oxd-userdropdown-name']");

    public AddUserPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(12));
    }
    
    public String getLoggedEmployeeName() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(loggedUserName)).getText();
    }

    // Add user flow
    public void addUser(String uname, String employeeName) {
        wait.until(ExpectedConditions.elementToBeClickable(userRoleDropdown)).click();
        wait.until(ExpectedConditions.elementToBeClickable(userRoleAdmin)).click();

        WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(employeeNameInput));
        input.clear();
        input.sendKeys(employeeName);
        wait.until(ExpectedConditions.visibilityOfElementLocated(employeeSuggestion)).click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(usernameInput)).sendKeys(uname);

        wait.until(ExpectedConditions.elementToBeClickable(statusDropdown)).click();
        wait.until(ExpectedConditions.elementToBeClickable(statusEnabled)).click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(passwordInput)).sendKeys("Test@1234");
        wait.until(ExpectedConditions.visibilityOfElementLocated(confirmPasswordInput)).sendKeys("Test@1234");

        wait.until(ExpectedConditions.elementToBeClickable(saveBtn)).click();

        // wait until save completes (save button disappears)
        wait.until(ExpectedConditions.invisibilityOfElementLocated(saveBtn));
    }

    // Edit username on the edit page (assumes you are in the edit user page)
    public void updateUsername(String newUsername) {
    	WebElement usernameBox = wait.until(ExpectedConditions.visibilityOfElementLocated(usernameInput));

        usernameBox.click();
        usernameBox.sendKeys(Keys.CONTROL + "a");
        usernameBox.sendKeys(Keys.DELETE);
        usernameBox.sendKeys(newUsername);

        driver.findElement(saveBtn).click();

        // WAIT FOR SAVE TO COMPLETE → Table must appear again
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h5[text()='System Users']")));
    }
}
