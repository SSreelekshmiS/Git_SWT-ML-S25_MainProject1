
package tests;

import org.testng.Assert;
import org.testng.annotations.Test;

import base.BaseClass;
import pages.RegistrationPage;

public class RegisterTestValid extends BaseClass {

	@Test
	public void verifyNewUserRegistration() {

		// Accessing application via base class
		RegistrationPage registerPage = new RegistrationPage(driver);

		// Setting up test data
		String firstName = "NEW Registered User";
		String lastName = "Temp";
		String email = "tempuser" + System.currentTimeMillis() + "@gmail.com";
		String password = "tempuser@2453";
		String confirmPassword = "tempuser@2453";

		// Registration steps
		registerPage.openRegistrationPage();
		registerPage.selectGender("Female");
		registerPage.enterFirstName(firstName);
		registerPage.enterLastName(lastName);
		registerPage.enterEmail(email);
		registerPage.enterPassword(password);
		registerPage.enterConfirmPassword(confirmPassword);
		registerPage.clickRegister();

		// Verify registration success
		String actualMessage = registerPage.getRegistrationMessage();
		Assert.assertTrue(actualMessage.contains("Your registration completed"),
				"Registration failed. Actual message: " + actualMessage);
		System.out.println("Registration successful for: " + email);
	}
}
