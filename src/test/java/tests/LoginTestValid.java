
package tests;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import base.BaseClass;
import pages.LoginPage;
import utils.ExcelUtils;

public class LoginTestValid extends BaseClass {

	@DataProvider(name = "ValidloginData")
	public Object[][] getLoginData() throws Exception {
		return ExcelUtils.getData("ValidLogin");
	}

	@Test(dataProvider = "ValidloginData")
	public void verifyLogin(String email, String password, String expectedResult) {

		// Create Login page object
		LoginPage loginPage = new LoginPage(driver);

		// Open the login page
		loginPage.openLoginPage();

		// Enter data from Excel
		loginPage.enterEmail(email);
		loginPage.enterPassword(password);

		// Submit the login form
		loginPage.clickLogin();

		// Check the actual result
		String actualResult = loginPage.getLoginResult();

		System.out.println("Email: " + email);
		System.out.println("Expected Result: " + expectedResult);
		System.out.println("Actual Result: " + actualResult);

		Assert.assertEquals(actualResult, "Success", "Valid login failed for: " + email);
		System.out.println("Message: Login " + actualResult);

	}
}