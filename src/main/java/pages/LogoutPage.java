
package pages;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

public class LogoutPage {

	private WebDriver driver;
	private WebDriverWait wait;

	private By logoutLink = By.cssSelector("a.ico-logout");
	private By loginLink = By.cssSelector("a.ico-login");

	// Constructor
	public LogoutPage(WebDriver driver) {
		this.driver = driver;
		this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
	}

	// Click Logout
	public void clickLogout() {
		wait.until(d -> !d.findElements(logoutLink).isEmpty());

		driver.findElement(logoutLink).click();
	}

	// Verify Login link is displayed after logout
	public boolean isLoginLinkDisplayed() {
		return wait.until(d -> !d.findElements(loginLink).isEmpty());
	}
}
