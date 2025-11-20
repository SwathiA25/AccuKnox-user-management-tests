package tests;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.*;
import utils.ScreenshotUtil;

public class UserManagementTest extends BaseTest {

	public static String createdUsername;
	public static String currentEmployeeName; // dynamic employee name

	@Test(priority = 1)
	public void testLogin() {
		try {
			LoginPage login = new LoginPage(driver);
			login.login(USER, PASS);

			Assert.assertTrue(driver.getCurrentUrl().contains("/dashboard"), "Dashboard not reached");

			AddUserPage header = new AddUserPage(driver);
			currentEmployeeName = header.getLoggedEmployeeName();

			String s = ScreenshotUtil.capture(driver, "testLogin_success");
			test.pass("Login successful").addScreenCaptureFromPath(s);

		} catch (Exception e) {
			String s = ScreenshotUtil.capture(driver, "testLogin_failure");
			test.fail("Login failed: " + e.getMessage()).addScreenCaptureFromPath(s);
			throw e;
		}
	}

	@Test(priority = 2, dependsOnMethods = "testLogin")
	public void testNavigateToAdminModule() {
		try {
			AdminPage admin = new AdminPage(driver);
			admin.goToAdmin();

			String s = ScreenshotUtil.capture(driver, "testNavigateToAdmin_success");
			test.pass("Navigated to Admin module").addScreenCaptureFromPath(s);

		} catch (Exception e) {
			String s = ScreenshotUtil.capture(driver, "testNavigateToAdmin_failure");
			test.fail("Navigation to Admin failed: " + e.getMessage()).addScreenCaptureFromPath(s);
			throw e;
		}
	}

	@Test(priority = 3, dependsOnMethods = "testNavigateToAdminModule")
	public void testAddNewUser() {
		try {
			AdminPage admin = new AdminPage(driver);
			AddUserPage add = new AddUserPage(driver);

			admin.clickAdd();

			createdUsername = "SampleUser_" + System.currentTimeMillis();

			add.addUser(createdUsername, currentEmployeeName);

			String s = ScreenshotUtil.capture(driver, "testAddNewUser_success");
			test.pass("User added: " + createdUsername).addScreenCaptureFromPath(s);

		} catch (Exception e) {
			String s = ScreenshotUtil.capture(driver, "testAddNewUser_failure");
			test.fail("Add user failed: " + e.getMessage()).addScreenCaptureFromPath(s);
			throw e;
		}
	}

	@Test(priority = 4, dependsOnMethods = "testAddNewUser")
	public void testSearchNewlyCreatedUser() {
		try {
			AdminPage admin = new AdminPage(driver);
			admin.searchUser(createdUsername);

			boolean found = admin.isUserPresent(createdUsername);
			Assert.assertTrue(found, "Created user not found");

			String s = ScreenshotUtil.capture(driver, "testSearchNewUser_success");
			test.pass("User found: " + createdUsername).addScreenCaptureFromPath(s);

		} catch (Exception e) {
			String s = ScreenshotUtil.capture(driver, "testSearchNewUser_failure");
			test.fail("Search failed: " + e.getMessage()).addScreenCaptureFromPath(s);
			throw e;
		}
	}

	@Test(priority = 5, dependsOnMethods = "testSearchNewlyCreatedUser")
	public void testEditAllPossibleDetails_EditUsername() {
		try {
			AdminPage admin = new AdminPage(driver);
			AddUserPage add = new AddUserPage(driver);

			admin.searchUser(createdUsername);
			admin.clickEditFirst();

			String newUsername = createdUsername + "_edited";
			add.updateUsername(newUsername);

			createdUsername = newUsername;

			String s = ScreenshotUtil.capture(driver, "testEditUser_success");
			test.pass("Username updated").addScreenCaptureFromPath(s);

		} catch (Exception e) {
			String s = ScreenshotUtil.capture(driver, "testEditUser_failure");
			test.fail("Edit failed: " + e.getMessage()).addScreenCaptureFromPath(s);
			throw e;
		}
	}

	@Test(priority = 6, dependsOnMethods = "testEditAllPossibleDetails_EditUsername")
	public void testValidateUpdatedDetails() {
		try {
			AdminPage admin = new AdminPage(driver);
			admin.searchUser(createdUsername);

			Assert.assertTrue(admin.isUserPresent(createdUsername), "Edited user not found");

			String s = ScreenshotUtil.capture(driver, "testValidateUpdatedDetails_success");
			test.pass("Validation success").addScreenCaptureFromPath(s);

		} catch (Exception e) {
			String s = ScreenshotUtil.capture(driver, "testValidateUpdatedDetails_failure");
			test.fail("Validation failed: " + e.getMessage()).addScreenCaptureFromPath(s);
			throw e;
		}
	}

	@Test(priority = 7, dependsOnMethods = "testValidateUpdatedDetails")
	public void testDeleteUser() {
		try {
			AdminPage admin = new AdminPage(driver);
			admin.searchUser(createdUsername);

			admin.selectFirstCheckbox();
			admin.deleteSelected();

			admin.searchUser(createdUsername);
			Assert.assertFalse(admin.isUserPresent(createdUsername), "User still exists after deletion");

			String s = ScreenshotUtil.capture(driver, "testDeleteUser_success");
			test.pass("User deleted").addScreenCaptureFromPath(s);

		} catch (Exception e) {
			String s = ScreenshotUtil.capture(driver, "testDeleteUser_failure");
			test.fail("Delete failed: " + e.getMessage()).addScreenCaptureFromPath(s);
			throw e;
		}
	}
}
