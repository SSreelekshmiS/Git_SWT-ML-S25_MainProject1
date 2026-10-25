
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
import utils.ExcelUtils;

public class DemoWebShopEndToEndTestValid extends BaseClass {

	private WebDriverWait wait;

	// Read user from Excel
	@DataProvider(name = "registrationData")
	public Object[][] registrationData() throws IOException {
		return ExcelUtils.getData("ValidRegistration1");
	}

	@Test(dataProvider = "registrationData")
	public void completeShoppingJourney(String firstName, String lastName, String email, String password,
			String confirmPassword, String searchProduct) {

		wait = new WebDriverWait(driver, Duration.ofSeconds(20));
		try {
			// 1. Open Demo Web Shop
			driver.get("https://demowebshop.tricentis.com/");

			// 2. New User Registration
			click(By.linkText("Register"));
			click(By.id("gender-male"));
			type(By.id("FirstName"), firstName);
			type(By.id("LastName"), lastName);
			type(By.id("Email"), email);
			type(By.id("Password"), password);
			type(By.id("ConfirmPassword"), confirmPassword);
			click(By.id("register-button"));

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

			// 5. Searching a Product
			type(By.id("small-searchterms"), searchProduct);
			click(By.cssSelector("input.button-1.search-box-button"));
			// Open the first matching product
			click(By.cssSelector("h2.product-title a"));

			// 6. Adding selected Product to Cart
			click(By.cssSelector("input[id^='add-to-cart-button-']"));
			// Wait until the add-to-cart notification or cart quantity updates
			wait.until(ExpectedConditions.or(
					ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".bar-notification")),
					ExpectedConditions.visibilityOfElementLocated(By.cssSelector("span.cart-qty"))));

			// 7. Verifying Cart data
			click(By.cssSelector("a.ico-cart"));
			Assert.assertTrue(
					wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".cart"))).isDisplayed(),
					"Shopping cart is not displayed");
			Assert.assertTrue(driver.findElements(By.cssSelector(".cart-item-row")).size() > 0,
					"No product found in the cart");
			// Accept terms and proceed to checkout
			if (!driver.findElement(By.id("termsofservice")).isSelected()) {
				click(By.id("termsofservice"));
			}
			click(By.id("checkout"));

			// 8. Entering Billing Address
			fillIfVisible("BillingNewAddress_FirstName", firstName);
			fillIfVisible("BillingNewAddress_LastName", lastName);
			fillIfVisible("BillingNewAddress_Email", email);
			fillIfVisible("BillingNewAddress_Company", "Test Company");
			fillIfVisible("BillingNewAddress_City", "New York");
			fillIfVisible("BillingNewAddress_Address1", "123 Test Street");
			fillIfVisible("BillingNewAddress_Address2", "Suite 1");
			fillIfVisible("BillingNewAddress_ZipPostalCode", "10001");
			fillIfVisible("BillingNewAddress_PhoneNumber", "5551234567");
			fillIfVisible("BillingNewAddress_FaxNumber", "5551234568");

			selectFirstValidOption("BillingNewAddress_CountryId");
			selectFirstValidOption("BillingNewAddress_StateProvinceId");
			clickContinue("billing-buttons-container");

			// 9. In-Store Pickup Option (Or Entering Shipping Address)
			By pickupCheckbox = By.id("PickUpInStore");
			// Wait until the checkbox is visible
			WebElement pickup = wait.until(ExpectedConditions.visibilityOfElementLocated(pickupCheckbox));

			// Check and select the checkbox if not already checked
			if (!pickup.isSelected()) {
				try {
					wait.until(ExpectedConditions.elementToBeClickable(pickupCheckbox)).click();
				} catch (org.openqa.selenium.ElementClickInterceptedException e) {
					// If the checkbox is covered, click its associated label
					driver.findElement(By.cssSelector("label[for='PickUpInStore']")).click();
				}
			}
			// Verify that the checkbox is checked
			wait.until(driver -> driver.findElement(pickupCheckbox).isSelected());
			Assert.assertTrue(driver.findElement(pickupCheckbox).isSelected(),
					"In-Store Pickup checkbox was not checked!");
			System.out.println("In-Store Pickup checkbox checked successfully.");
			// Continue to the next checkout step
			clickContinue("shipping-buttons-container");

			// 10. Selecting Shipping method
			if (isVisible("shipping-method-buttons-container")) {
				selectFirstRadio("input[name='shippingoption']");
				clickContinue("shipping-method-buttons-container");
			}

			// 11. Selecting Payment Method
			System.out.println("Payment Method started for: " + email);
			By paymentSection = By.id("payment-method-buttons-container");

			// Wait for the Payment Method section
			wait.until(ExpectedConditions.visibilityOfElementLocated(paymentSection));

			// Select the payment option
			By paymentRadio = By.cssSelector("input[name='paymentmethod']");
			WebElement radio = wait.until(ExpectedConditions.elementToBeClickable(paymentRadio));
			if (!radio.isSelected()) {
				radio.click();
			}

			// Verify selection
			wait.until(driver -> driver.findElement(paymentRadio).isSelected());
			System.out.println("Payment radio selected.");

			// Locate the actual Continue button
			By continueButton = By
					.cssSelector("#payment-method-buttons-container " + "input.payment-method-next-step-button");

			// Wait until Continue is enabled and clickable
			wait.until(ExpectedConditions.elementToBeClickable(continueButton));

			// Scroll into view and click a freshly located element
			WebElement button = driver.findElement(continueButton);
			((org.openqa.selenium.JavascriptExecutor) driver)
					.executeScript("arguments[0].scrollIntoView({block:'center'});", button);
			button = wait.until(ExpectedConditions.elementToBeClickable(continueButton));
			button.click();
			System.out.println("Payment Continue clicked.");

			// Wait for the checkout to advance
			wait.until(ExpectedConditions.or(
					ExpectedConditions.visibilityOfElementLocated(By.id("payment-info-buttons-container")),
					ExpectedConditions.visibilityOfElementLocated(By.id("confirm-order-buttons-container"))));
			System.out.println("Payment Method completed for: " + email);

//
			// 12. Payment Information
			// The required fields depend on the chosen payment method.
			if (isVisible("payment-info-buttons-container")) {
				clickContinue("payment-info-buttons-container");
			}

			// 13. Confirming Order
			By confirmContainer = By.id("confirm-order-buttons-container");
			// Wait for the Confirm Order section
			@SuppressWarnings("unused")
			WebElement confirmSection = wait.until(ExpectedConditions.visibilityOfElementLocated(confirmContainer));

			// Find the Confirm button inside the section
			By confirmButton = By.cssSelector(
					"#confirm-order-buttons-container " + "input[type='button'], " + "#confirm-order-buttons-container "
							+ "button, " + "#confirm-order-buttons-container " + "input[type='submit']");

			WebElement confirm = wait.until(ExpectedConditions.elementToBeClickable(confirmButton));
			// Scroll the button into view
			((org.openqa.selenium.JavascriptExecutor) driver)
					.executeScript("arguments[0].scrollIntoView({block:'center'});", confirm);

			// Click Confirm
			confirm.click();
			System.out.println("Confirm button clicked.");

			// Wait for successful order confirmation
			wait.until(ExpectedConditions.urlContains("checkout/completed"));
			Assert.assertTrue(driver.getCurrentUrl().contains("checkout/completed"),
					"Order confirmation page was not reached.");
			System.out.println("Order completed successfully!");

			// 14. LogOut after shopping
			click(By.cssSelector("a.ico-logout"));
			Assert.assertTrue(wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("a.ico-login")))
					.isDisplayed(), "Logout failed");
			System.out.println("End-to-end test passed for: " + email);

		} catch (Exception e) {
			System.out.println("End-to-end test failed for: " + email);
			throw new RuntimeException(e);
		}
	}

	// Enter text into a field
	private void type(By locator, String value) {
		WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
		element.clear();
		element.sendKeys(value);
	}

	// Click an element after it becomes clickable
	private void click(By locator) {
		wait.until(ExpectedConditions.elementToBeClickable(locator)).click();
	}

	// Fill a checkout field only when it is displayed
	private void fillIfVisible(String id, String value) {
		if (isVisible(id)) {
			type(By.id(id), value);
		}
	}

	// Check whether an element is displayed
	private boolean isVisible(String id) {
		for (WebElement element : driver.findElements(By.id(id))) {
			if (element.isDisplayed()) {
				return true;
			}
		}
		return false;
	}

	// Select the first available valid option in a dropdown
	private void selectFirstValidOption(String id) {
		if (isVisible(id)) {
			Select dropdown = new Select(driver.findElement(By.id(id)));

			if (dropdown.getOptions().size() > 1) {
				dropdown.selectByIndex(1);
			}
		}
	}

	// Select the first available radio option
	private void selectFirstRadio(String locator) {
		for (WebElement radio : driver.findElements(By.cssSelector(locator))) {
			if (radio.isDisplayed() && radio.isEnabled()) {
				if (!radio.isSelected()) {
					radio.click();
				}
				return;
			}
		}
	}

	// Click the Continue button in the specified checkout section
	private void clickContinue(String containerId) {
		By container = By.id(containerId);
		wait.until(ExpectedConditions.visibilityOfElementLocated(container));
		WebElement section = driver.findElement(container);
		WebElement button = section
				.findElement(By.cssSelector("button, input[type='button'], " + "input[type='submit']"));
		wait.until(ExpectedConditions.elementToBeClickable(button)).click();
	}
}
