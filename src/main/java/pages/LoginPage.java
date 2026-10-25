
package pages;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import base.BaseClass;

public class LoginPage extends BaseClass {

	private WebDriver driver;
	private WebDriverWait wait;
	private By loginLink = By.className("ico-login");
	private By email = By.id("Email");
	private By password = By.id("Password");
	private By loginButton = By.cssSelector("input.button-1.login-button");
	private By logoutLink = By.className("ico-logout");
	private By errorMessage = By.cssSelector(".validation-summary-errors");

	// Constructor
	public LoginPage(WebDriver driver) {
		this.driver = driver;
		this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
	}

	// Open the login page
	public void openLoginPage() {
		driver.findElement(loginLink).click();
	}

	// Entering email
	public void enterEmail(String useremail) {
		driver.findElement(email).clear();
		wait.until(ExpectedConditions.visibilityOfElementLocated(email)).sendKeys(useremail);
	}

	// Entering password
	public void enterPassword(String userpassword) {
		driver.findElement(password).clear();
		driver.findElement(password).sendKeys(userpassword);
	}

	// Clicking Login
	public void clickLogin() {
		driver.findElement(loginButton).click();
	}

	// Wait for either a successful login or an error message
	public String getLoginResult() {
		wait.until(d -> !d.findElements(logoutLink).isEmpty() || !d.findElements(errorMessage).isEmpty());
		if (!driver.findElements(logoutLink).isEmpty()) {
			return "Success";
		}
		return "Failure";
	}

	// Read the displayed error message
	public String getErrorMessage() {
		if (!driver.findElements(errorMessage).isEmpty()) {
			return driver.findElement(errorMessage).getText();
		}
		return "";
	}

	public boolean isLoginSuccessful() {
		try {
			wait.until(ExpectedConditions.visibilityOfElementLocated(logoutLink));
			return true;
		} catch (TimeoutException e) {
			return false;
		}
	}
}
