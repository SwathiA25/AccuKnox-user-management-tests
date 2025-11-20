package base;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.*;

import utils.ExtentReportManager;
import utils.ScreenshotUtil;

import java.lang.reflect.Method;
import java.time.Duration;

public class BaseTest {

	protected static WebDriver driver;
	protected static ExtentReports extent;
	protected ExtentTest test;

	// application constants
	protected static final String BASE_URL = "https://opensource-demo.orangehrmlive.com/web/index.php/auth/login";
	protected static final String USER = "Admin";
	protected static final String PASS = "admin123";

	@BeforeClass(alwaysRun = true)
	public void beforeClass() {
		// initialize ExtentReports once
		extent = ExtentReportManager.getExtent();

		// initialize browser once for all tests in this class
		WebDriverManager.chromedriver().setup();
		driver = new ChromeDriver();
		driver.manage().window().maximize();

		// small implicit wait (explicit waits used mainly)
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));

		// open app
		driver.get(BASE_URL);
	}

	@BeforeMethod(alwaysRun = true)
	public void beforeMethod(Method method) {
		// create Extent node/test for each @Test method
		test = extent.createTest(method.getName());
	}

	@AfterMethod(alwaysRun = true)
	public void afterMethod(org.testng.ITestResult result) {
		// nothing extra here — screenshots & report attachments are handled within
		// tests
	}

	@AfterClass(alwaysRun = true)
	public void afterClass() {
		if (driver != null) {
			driver.quit();
		}
		if (extent != null) {
			extent.flush();
		}
	}

	// helper wrapper to capture and attach screenshot for success/failure from
	// tests
	protected String captureAndAttach(String name, String status) {
		String path = ScreenshotUtil.capture(driver, name);
		if (path != null) {
			test.info(status + " - screenshot: " + path);
		}
		return path;
	}
}
