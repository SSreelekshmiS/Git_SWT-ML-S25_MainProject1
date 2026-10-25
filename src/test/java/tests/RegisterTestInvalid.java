
package tests;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import org.testng.Assert;
import org.testng.annotations.Test;

import base.BaseClass;
import pages.RegistrationPage;

public class RegisterTestInvalid extends BaseClass {

	@Test
	public void verifyRegistrationWithMissingMandatoryFields() {

		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
		// Accessing application via base class
		RegistrationPage registerPage = new RegistrationPage(driver);

		// Setting up test data
		String firstName = "NEW Registered User";
		String lastName = "Test";
		// Registration steps
		registerPage.openRegistrationPage();
		registerPage.selectGender("Female");
		registerPage.enterFirstName(firstName);
		registerPage.enterLastName(lastName);

		// Leaving Email, Password and Confirm Password blank

		// STEP 4: Click Register
		driver.findElement(By.id("register-button")).click();

		// STEP 5: Wait for validation messages
		wait.until(ExpectedConditions.or(
				ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".validation-summary-errors")),
				ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".field-validation-error"))));

		// STEP 6: Verify registration was rejected
		boolean hasFieldErrors = driver.findElements(By.cssSelector(".field-validation-error")).stream()
				.anyMatch(WebElement::isDisplayed);

		boolean hasSummaryErrors = driver.findElements(By.cssSelector(".validation-summary-errors")).stream()
				.anyMatch(WebElement::isDisplayed);

		Assert.assertTrue(hasFieldErrors || hasSummaryErrors,
				"Expected validation errors for missing mandatory fields.");

		// Verify registration did not succeed
		Assert.assertTrue(driver.findElements(By.cssSelector(".result")).isEmpty()
				|| !driver.findElement(By.cssSelector(".result")).getText().contains("Your registration completed"),
				"Registration unexpectedly succeeded with missing fields.");
		System.out.println("PASS: Registration was rejected because mandatory fields were blank.");

		// Print validation messages
		driver.findElements(By.cssSelector(".field-validation-error, .validation-summary-errors")).stream()
				.filter(WebElement::isDisplayed).map(WebElement::getText).filter(message -> !message.isBlank())
				.forEach(System.out::println);
	}
}
