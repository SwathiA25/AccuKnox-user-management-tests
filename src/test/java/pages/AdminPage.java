package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class AdminPage {

	private WebDriver driver;
	private WebDriverWait wait;

	private By adminMenu = By.xpath("//span[text()='Admin']");
	private By addBtn = By.xpath("//button[normalize-space()='Add']");
	private By usernameSearchInput = By.xpath("//label[text()='Username']/parent::div/following-sibling::div//input");
	private By searchBtn = By.xpath("//button[normalize-space()='Search']");
	private By resultsRows = By.xpath("//div[@class='oxd-table-body']/div");
	private By firstEditIcon = By.xpath("(//i[contains(@class,'bi-pencil-fill')])[1]");
	private By firstDeleteIcon = By.xpath("(//i[contains(@class,'bi-trash')])[1]");
	private By firstRowCheckbox = By
			.xpath("(//div[@class='oxd-table-body']//div[@class='oxd-checkbox-wrapper']//label)[1]");
	private By deleteSelectedBtn = By.xpath("//button[normalize-space()='Delete Selected']");
	private By confirmDeleteBtn = By.xpath("//button[normalize-space()='Yes, Delete']");

	public AdminPage(WebDriver driver) {
		this.driver = driver;
		this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
	}

	public void goToAdmin() {
		wait.until(ExpectedConditions.elementToBeClickable(adminMenu)).click();

		// Wait for table to load fully
		wait.until(ExpectedConditions.visibilityOfElementLocated(resultsRows));
	}

	public void clickAdd() {
		wait.until(ExpectedConditions.elementToBeClickable(addBtn)).click();
	}

	public void searchUser(String uname) {
		WebElement searchBox = wait.until(ExpectedConditions.visibilityOfElementLocated(usernameSearchInput));

		// click inside box
		searchBox.click();

		// FULL CLEAR — prevents double typing issue
		searchBox.sendKeys(Keys.CONTROL + "a");
		searchBox.sendKeys(Keys.DELETE);

		// type fresh value
		searchBox.sendKeys(uname);

		// click search
		wait.until(ExpectedConditions.elementToBeClickable(searchBtn)).click();

		// wait for table update
		wait.until(ExpectedConditions.or(ExpectedConditions.visibilityOfElementLocated(resultsRows),
				ExpectedConditions.presenceOfElementLocated(By.xpath("//span[text()='No Records Found']"))));
	}

	public boolean isUserPresent(String uname) {
		// Wait for table to be present OR No Records Found message
		wait.until(ExpectedConditions.or(ExpectedConditions.presenceOfElementLocated(resultsRows),
				ExpectedConditions.presenceOfElementLocated(By.xpath("//span[text()='No Records Found']"))));

		// Now fetch rows safely
		List<WebElement> rows = driver.findElements(resultsRows);

		// If no rows, user not found
		if (rows.size() == 0) {
			return false;
		}

		// Loop safely and re-fetch each row to avoid stale element
		for (int i = 0; i < rows.size(); i++) {
			WebElement row = driver.findElements(resultsRows).get(i);

			String text = row.getText();

			if (text.contains(uname)) {
				return true;
			}
		}
		return false;
	}

	public void clickEditFirst() {
		wait.until(ExpectedConditions.elementToBeClickable(firstEditIcon)).click();
	}

	// Optional: click delete icon directly
	public void clickDeleteFirst() {
		wait.until(ExpectedConditions.elementToBeClickable(firstDeleteIcon)).click();
	}

	public void selectFirstCheckbox() {
		// Ensure table loaded
		wait.until(ExpectedConditions.visibilityOfElementLocated(resultsRows));

		WebElement cb = wait.until(ExpectedConditions.elementToBeClickable(firstRowCheckbox));

		cb.click();
	}

	public void deleteSelected() {
		wait.until(ExpectedConditions.elementToBeClickable(deleteSelectedBtn)).click();
		wait.until(ExpectedConditions.elementToBeClickable(confirmDeleteBtn)).click();

		// Wait for table to refresh
		wait.until(ExpectedConditions.or(
				ExpectedConditions.presenceOfElementLocated(By.xpath("//span[text()='No Records Found']")),
				ExpectedConditions.visibilityOfElementLocated(resultsRows)));
	}
}
