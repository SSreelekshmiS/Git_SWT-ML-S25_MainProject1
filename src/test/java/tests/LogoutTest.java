
package tests;

import org.testng.Assert;
import org.testng.annotations.Test;

import base.BaseClass;
import pages.LoginPage;
import pages.LogoutPage;

public class LogoutTest extends BaseClass {

	@Test
	public void verifyLoginAndLogout() {

		// Create LoginPage object
		LoginPage loginPage = new LoginPage(driver);

		// Open the Login page
		loginPage.openLoginPage();

		// Enter valid credentials
		loginPage.enterEmail("testusers11@gmail.com");
		loginPage.enterPassword("Tester1@1");

		// Click Login
		loginPage.clickLogin();

		// Verify login is successful
		String actualResult = loginPage.getLoginResult();
		Assert.assertEquals(actualResult, "Success", "Login failed. Cannot continue with logout test.");

		// Create Logout object
		LogoutPage logoutPage = new LogoutPage(driver);
		logoutPage.clickLogout();
		Assert.assertTrue(logoutPage.isLoginLinkDisplayed(), "Logout failed: Login link was not displayed.");

		// Verify logout is successful
		Assert.assertTrue(logoutPage.isLoginLinkDisplayed(), "Logout failed. Login link was not displayed.");
		System.out.println("Login and Logout test passed successfully.");
	}
}
