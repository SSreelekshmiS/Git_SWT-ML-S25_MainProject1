
package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class RegistrationPage {

	WebDriver driver;
	WebDriverWait wait;

	// Constructor
	public RegistrationPage(WebDriver driver) {
		this.driver = driver;
		this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
	}

	// Locators
	By registerLink = By.linkText("Register");
	By maleRadio = By.id("gender-male");
	By femaleRadio = By.id("gender-female");
	By firstNameField = By.id("FirstName");
	By lastNameField = By.id("LastName");
	By emailField = By.id("Email");
	By passwordField = By.id("Password");
	By confirmPasswordField = By.id("ConfirmPassword");
	By registerButton = By.id("register-button");
	By successMessage = By.cssSelector(".result");

	// Open Registration Page
	public void openRegistrationPage() {
		wait.until(ExpectedConditions.elementToBeClickable(registerLink)).click();
	}

	// Select Gender
	public void selectGender(String gender) {
		if (gender.equalsIgnoreCase("Male")) {
			wait.until(ExpectedConditions.elementToBeClickable(maleRadio)).click();
		} else if (gender.equalsIgnoreCase("Female")) {
			wait.until(ExpectedConditions.elementToBeClickable(femaleRadio)).click();
		}
	}

	// Enter First Name
	public void enterFirstName(String firstName) {
		wait.until(ExpectedConditions.visibilityOfElementLocated(firstNameField)).sendKeys(firstName);
	}

	// Enter Last Name
	public void enterLastName(String lastName) {
		wait.until(ExpectedConditions.visibilityOfElementLocated(lastNameField)).sendKeys(lastName);
	}

	// Enter Email
	public void enterEmail(String email) {
		wait.until(ExpectedConditions.visibilityOfElementLocated(emailField)).sendKeys(email);
	}

	// Enter Password
	public void enterPassword(String password) {
		wait.until(ExpectedConditions.visibilityOfElementLocated(passwordField)).sendKeys(password);
	}

	// Enter Confirm Password
	public void enterConfirmPassword(String confirmPassword) {
		wait.until(ExpectedConditions.visibilityOfElementLocated(confirmPasswordField)).sendKeys(confirmPassword);
	}

	// Click Register
	public void clickRegister() {
		wait.until(ExpectedConditions.elementToBeClickable(registerButton)).click();
	}

	// Get Registration Result
	public String getRegistrationMessage() {
		return wait.until(ExpectedConditions.visibilityOfElementLocated(successMessage)).getText();
	}
}
