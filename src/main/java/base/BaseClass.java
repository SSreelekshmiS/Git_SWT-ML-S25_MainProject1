package base;

import java.time.Duration;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

public class BaseClass {

	protected WebDriver driver;
	protected WebDriverWait wait;
	protected final String baseUrl = "https://demowebshop.tricentis.com";

	// This section will Runs before each test method
	@BeforeMethod
	public void setUp() {
		// Configuring Chrome browser 
		ChromeOptions options = new ChromeOptions();

		// Launches Chrome browser
		driver = new ChromeDriver(options);
		driver.manage().window().maximize();
		// Explicit wait of 15 seconds
		wait = new WebDriverWait(driver, Duration.ofSeconds(15));
		// Open the Demo Web Shop website
		driver.get(baseUrl);
	}

	@AfterMethod(alwaysRun = true)
	public void tearDown() {
		if (driver != null) {
			driver.quit();
		}
	}
}
