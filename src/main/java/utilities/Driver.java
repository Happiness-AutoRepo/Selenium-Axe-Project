package utilities;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import io.github.bonigarcia.wdm.WebDriverManager;

public class Driver {

	private static WebDriver driver;

	public static WebDriver getInstance() {
		if (driver == null || ((RemoteWebDriver) driver).getSessionId() == null) {
			switch (ConfigurationReader.getProperty("browser")) {
			case "firefox":
				System.setProperty("webdriver.gecko.driver", ConfigurationReader.getProperty("gecko.driver.path"));
				driver = new FirefoxDriver();
				break;
			case "chrome":
				WebDriverManager.chromedriver().setup();
//				System.setProperty("webdriver.chrome.driver", ConfigurationReader.getProperty("chrome.driver.path"));
				driver = new ChromeDriver();
				break;
			case "ie":
				System.setProperty("webdriver.ie.driver", ConfigurationReader.getProperty("ie.driver.path"));
				driver = new InternetExplorerDriver();
				break;
			case "safari":
				driver = new SafariDriver();
				break;
			default:
				System.setProperty("webdriver.chrome.driver", ConfigurationReader.getProperty("chrome.driver.path"));
				driver = new ChromeDriver();
			}
		}
		return driver;
	}

	public static void closeDriver() {
		if (driver != null) {
			driver.quit();
			driver = null;
		}
	}
}