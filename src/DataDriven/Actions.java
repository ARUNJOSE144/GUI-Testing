package DataDriven;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.relevantcodes.extentreports.LogStatus;

import Modal.SubCase;
import Modal.SystemProperty;

public class Actions {

	static SystemProperty systemProperty = new SystemProperty();

	public void doAction(SubCase subCase, ChromeDriver driver) throws IOException {

		try {
			if (Utils.validate(subCase.getOperation())) {
				Utils.addOrRemoveBoarder(subCase, true);
				switch (subCase.getOperation().toLowerCase()) {
				case "type":
					type(subCase, driver);
					break;
				case "click":
					click(subCase, driver);
					break;
				case "goto":
					break;
				case "wait":
					waitFor(subCase, driver);
					break;
				case "verify":
					verify(subCase, driver);
					break;

				default:
					commonVerify(subCase, false, "Invalid Action -> " + subCase.getOperation());
					throw new Exception("Invalid Action : " + subCase.getOperation());
				}
			}

			if ((!subCase.getOperation().equalsIgnoreCase("wait")) && Utils.validate(subCase.getTime())) {
				TimeUnit.SECONDS.sleep((int) Float.parseFloat(subCase.getTime()));
			}
			// Utils.addOrRemoveBoarder(subCase,false);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			// commonVerify(subCase, false);
			// Utils.addOrRemoveBoarder(subCase,false);
		}

	}

	public void type(SubCase subCase, ChromeDriver driver) throws IOException {
		try {
			switch (subCase.getLocatorType().toLowerCase()) {
			case "name":
				driver.findElement(By.name(subCase.getKey())).sendKeys(Keys.chord(Keys.CONTROL, "a", Keys.DELETE));
				driver.findElement(By.name(subCase.getKey())).sendKeys(subCase.getData());

				break;
			case "class":
				driver.findElement(By.className(subCase.getKey())).sendKeys(Keys.chord(Keys.CONTROL, "a", Keys.DELETE));
				driver.findElement(By.className(subCase.getKey())).sendKeys(subCase.getData());
				break;
			case "id":
				driver.findElement(By.id(subCase.getKey())).sendKeys(Keys.chord(Keys.CONTROL, "a", Keys.DELETE));
				driver.findElement(By.id(subCase.getKey())).sendKeys(subCase.getData());
				break;
			case "xpath":
				driver.findElement(By.xpath(subCase.getKey())).sendKeys(Keys.chord(Keys.CONTROL, "a", Keys.DELETE));
				driver.findElement(By.xpath(subCase.getKey())).sendKeys(subCase.getData());
				break;

			default:
				commonVerify(subCase, false, "Invalid locatorType -> " + subCase.getLocatorType());
				throw new Exception("Invalid locatorType : " + subCase.getLocatorType());
			}
			commonVerify(subCase, true, "");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			commonVerify(subCase, false, "Invalid locatorType -> " + subCase.getLocatorType());
		}

	}

	public void click(SubCase subCase, ChromeDriver driver) throws IOException {

		try {
			switch (subCase.getLocatorType().toLowerCase()) {
			case "name":
				driver.findElement(By.name(subCase.getKey())).click();
				break;
			case "class":
				driver.findElement(By.className(subCase.getKey())).click();
				break;
			case "id":
				driver.findElement(By.id(subCase.getKey())).click();
				break;
			case "xpath":
				driver.findElement(By.xpath(subCase.getKey())).click();
				break;

			default:
				commonVerify(subCase, false, "Invalid locatorType -> " + subCase.getLocatorType());
				throw new Exception("Invalid locatorType : " + subCase.getLocatorType());
			}
			commonVerify(subCase, true, "");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			commonVerify(subCase, false, "Invalid locatorType -> " + subCase.getLocatorType());
		}

	}

	public void waitFor(SubCase subCase, ChromeDriver driver) throws IOException {
		try {
			WebDriverWait wait = new WebDriverWait(driver, (int) Float.parseFloat(subCase.getTime()));
			switch (subCase.getLocatorType().toLowerCase()) {
			case "name":
				wait.until(ExpectedConditions.presenceOfElementLocated(By.name(subCase.getKey())));
				break;
			case "url":
				wait.until(ExpectedConditions
						.urlToBe(systemProperty.properties.getProperty("BASE_URL") + subCase.getKey()));
				break;
			case "class":
				wait.until(ExpectedConditions.presenceOfElementLocated(By.className(subCase.getKey())));
				break;
			case "id":
				wait.until(ExpectedConditions.presenceOfElementLocated(By.id(subCase.getKey())));
				break;
			case "xpath":
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(subCase.getKey())));
				break;

			default:
				commonVerify(subCase, false, "Invalid locatorType : " + subCase.getLocatorType());
				throw new Exception("Invalid locatorType -> " + subCase.getLocatorType());
			}

			commonVerify(subCase, true, "");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			commonVerify(subCase, false, "Invalid locatorType -> " + subCase.getLocatorType());
		}
	}

	public void verify(SubCase subCase, ChromeDriver driver) throws IOException {
		if (driver.getCurrentUrl()
				.equalsIgnoreCase(systemProperty.properties.getProperty("BASE_URL") + subCase.getKey())) {
			SeleniumTest.extentTest.log(LogStatus.PASS, getFormattedMsg(subCase, true));
		} else {
			SeleniumTest.extentTest.log(LogStatus.FAIL, getFormattedMsg(subCase, false));
			Utils.takeScreenShot();
		}
	}

	public void commonVerify(SubCase subCase, boolean isPass, String msg) throws IOException {
		subCase.setErrorMsg(msg);
		if (isPass) {
			SeleniumTest.extentTest.log(LogStatus.PASS, getFormattedMsg(subCase, true));
		} else {
			SeleniumTest.extentTest.log(LogStatus.FAIL, getFormattedMsg(subCase, false));
			Utils.takeScreenShot();

		}
	}

	public String getFormattedMsg(SubCase subCase, boolean isPass) {
		String msg = "";

		if (!isPass) {
			msg += Utils.validate(subCase.getErrorMsg()) ? " Error Msg  : " + subCase.getErrorMsg() : "";
		}

		msg += Utils.validate(subCase.getOperation()) ? "  ,Action  : " + subCase.getOperation() : "";
		msg += Utils.validate(subCase.getLocatorType()) ? "  ,Locator Type  : " + subCase.getLocatorType() : "";
		msg += Utils.validate(subCase.getKey()) ? "  ,Key  : " + subCase.getKey() : "";
		msg += Utils.validate(subCase.getData()) ? "  ,Data  : " + subCase.getData() : "";
		msg += Utils.validate(subCase.getUrl()) ? "  ,Url  : " + subCase.getUrl() : "";
		msg += Utils.validate(subCase.getTime()) ? "  ,Time out(Seconds)  : " + subCase.getTime() : "";
		return msg;
	}

}
