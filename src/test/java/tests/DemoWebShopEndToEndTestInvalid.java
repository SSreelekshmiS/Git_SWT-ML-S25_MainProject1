
package tests;

import java.io.IOException;
import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import base.BaseClass;
import pages.RegistrationPage;
import utils.ExcelUtils;

public class DemoWebShopEndToEndTestInvalid extends BaseClass {

	private WebDriverWait wait;

	// Read user from Excel
	@DataProvider(name = "registrationData")
	public Object[][] NegativeCheckData() throws IOException {
		return ExcelUtils.getData("ValidRegistration2NegCheck");
	}

	@Test(dataProvider = "registrationData")
	public void negativeCheckoutTest(String firstName, String lastName, String email, String password,
			String confirmPassword, String searchProduct) {

		wait = new WebDriverWait(driver, Duration.ofSeconds(25));
		try {
			// 1. Open Demo Web Shop
			driver.get("https://demowebshop.tricentis.com/");

			// 2. New User Registration
			RegistrationPage registerPage = new RegistrationPage(driver);
			// Registration steps
			registerPage.openRegistrationPage();
			registerPage.selectGender("Female");
			registerPage.enterFirstName(firstName);
			registerPage.enterLastName(lastName);
			registerPage.enterEmail(email);
			registerPage.enterPassword(password);
			registerPage.enterConfirmPassword(confirmPassword);
			registerPage.clickRegister();
			Assert.assertTrue(wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".result")))
					.getText().contains("Your registration completed"), "Registration failed");

			// 3. LOGOUT after Registration
			click(By.cssSelector("a.ico-logout"));

			// 4. User LOGIN
			click(By.linkText("Log in"));
			type(By.id("Email"), email);
			type(By.id("Password"), password);
			click(By.cssSelector("input.login-button"));
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("a.ico-logout")));
			Assert.assertTrue(driver.findElement(By.cssSelector("a.ico-logout")).isDisplayed(), "Login failed");

			// 5. Search for a product
			WebElement searchBox = driver.findElement(By.id("small-searchterms"));
			searchBox.sendKeys(searchProduct);
			driver.findElement(By.cssSelector("input.button-1.search-box-button")).click();

			// Open the first matching product
			wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("h2.product-title a"))).click();

			// 6. Adding selected Product to Cart
			wait.until(ExpectedConditions.elementToBeClickable(By.id("add-to-cart-button-51"))).click();

			wait.until(ExpectedConditions.or(ExpectedConditions.visibilityOfElementLocated(By.id("bar-notification")),
					ExpectedConditions.textToBePresentInElementLocated(By.cssSelector("span.cart-qty"), "1")));
			System.out.println("Product added to cart");

			// 7. Open the shopping cart
			wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("a.ico-cart"))).click();

			// Wait for the cart page to load
			wait.until(ExpectedConditions.urlContains("cart"));

			// Verify the cart page using its table or empty-cart message
			boolean cartHasItems = wait
					.until(driver -> !driver.findElements(By.cssSelector("table.cart tbody tr.cart-item-row")).isEmpty()
							|| !driver.findElements(By.cssSelector(".order-summary-content")).isEmpty());

			Assert.assertTrue(cartHasItems, "Cart page did not display cart items or cart content.");

			// Confirm at least one product row exists
			Assert.assertTrue(driver.findElements(By.cssSelector("tr.cart-item-row")).size() > 0,
					"No product found in the shopping cart.");

			System.out.println("Cart verified successfully");

			// 8. Accept Terms of Service
			WebElement terms = wait.until(ExpectedConditions.elementToBeClickable(By.id("termsofservice")));
			if (!terms.isSelected()) {
				terms.click();
			}
			// STEP 11: Click Checkout
			WebElement checkoutButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("checkout")));

			checkoutButton.click();
			System.out.println("Checkout button clicked");

			// 9. Wait for the billing address section
			// Do NOT wait specifically for billing-address-select
			wait.until(ExpectedConditions.or(
					ExpectedConditions.visibilityOfElementLocated(By.id("billing-buttons-container")),
					ExpectedConditions.visibilityOfElementLocated(By.id("BillingNewAddress_FirstName"))));
			System.out.println("Checkout page opened: " + driver.getCurrentUrl());

			// 10. Select New Address only if the dropdown exists
			if (!driver.findElements(By.id("billing-address-select")).isEmpty()) {
				Select addressSelect = new Select(driver.findElement(By.id("billing-address-select")));
				if (addressSelect.getOptions().size() > 1) {
					addressSelect.selectByValue("");
				}
			}

			// 11. Leave mandatory billing fields blank
			clearIfPresent(By.id("BillingNewAddress_FirstName"));
			clearIfPresent(By.id("BillingNewAddress_LastName"));
			clearIfPresent(By.id("BillingNewAddress_Email"));
			clearIfPresent(By.id("BillingNewAddress_City"));
			clearIfPresent(By.id("BillingNewAddress_Address1"));
			clearIfPresent(By.id("BillingNewAddress_ZipPostalCode"));
			clearIfPresent(By.id("BillingNewAddress_PhoneNumber"));

			// Select a valid country if available.
			// Leave the mandatory text fields blank.
			if (!driver.findElements(By.id("BillingNewAddress_CountryId")).isEmpty()) {
				Select country = new Select(driver.findElement(By.id("BillingNewAddress_CountryId")));
				if (country.getOptions().size() > 1) {
					country.selectByIndex(1);
				}
			}

			// 12. Click Continue without entering mandatory details
			WebElement continueButton = wait.until(ExpectedConditions
					.elementToBeClickable(By.cssSelector("#billing-buttons-container input.button-1")));
			continueButton.click();

			// 13. Verify that validation errors are displayed
			wait.until(ExpectedConditions.or(
					ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".field-validation-error")),
					ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".validation-summary-errors"))));
			boolean fieldErrors = driver.findElements(By.cssSelector(".field-validation-error")).stream()
					.anyMatch(WebElement::isDisplayed);
			boolean summaryErrors = driver.findElements(By.cssSelector(".validation-summary-errors")).stream()
					.anyMatch(WebElement::isDisplayed);

			Assert.assertTrue(fieldErrors || summaryErrors,
					"Negative test failed: no mandatory-field validation appeared.");

			System.out.println("PASS: Checkout was blocked because mandatory billing fields were blank.");
			System.out.println("Validation messages:");
			driver.findElements(By.cssSelector(".field-validation-error, .validation-summary-errors")).stream()
					.filter(WebElement::isDisplayed).map(WebElement::getText).filter(message -> !message.isBlank())
					.forEach(System.out::println);

		} catch (Exception e) {
			System.out.println("End-to-end test failed for: " + email);
			throw new RuntimeException(e);
		}
	}

	private void type(By id, String firstName) {
		// TODO Auto-generated method stub

	}

	private void click(By linkText) {
		// TODO Auto-generated method stub

	}

	// Helper method: clear a field only if it exists
	private void clearIfPresent(By locator) {
		if (!driver.findElements(locator).isEmpty()) {
			WebElement element = driver.findElement(locator);
			element.clear();
		}
	}
}
