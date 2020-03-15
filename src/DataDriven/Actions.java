package DataDriven;

import java.io.IOException;

import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.relevantcodes.extentreports.LogStatus;

import Modal.SubCase;

public class Actions {

	public void doAction(SubCase subCase, ChromeDriver driver) throws IOException {

		try {
			if (Utils.validate(subCase.getOperation())) {
				switch (subCase.getOperation().toLowerCase()) {
				case "type":
					type(subCase, driver);
					break;
				case "click":
					click(subCase, driver);
					break;
				case "goto":
					driver.get(Utils.BASE_URL + subCase.getUrl());
					break;
				case "wait":
					waitFor(subCase, driver);
					break;
				case "verify":
					verify(subCase, driver);
					break;

				default:
					break;
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			//commonVerify(subCase, false);
		}

	}

	public void type(SubCase subCase, ChromeDriver driver) throws IOException {
		try {
			switch (subCase.getLocatorType().toLowerCase()) {
			case "name":
				driver.findElement(By.name(subCase.getKey())).sendKeys(subCase.getData());
				break;
			case "class":
				driver.findElement(By.className(subCase.getKey())).sendKeys(subCase.getData());
				break;

			default:
				break;
			}
			commonVerify(subCase, true);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			commonVerify(subCase, false);
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
			case "xpath":
				driver.findElement(By.xpath(subCase.getKey())).click();
				break;

			default:
				break;
			}
			commonVerify(subCase, true);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			commonVerify(subCase, false);
		}

	}

	public void waitFor(SubCase subCase, ChromeDriver driver) throws IOException {
		try {
			WebDriverWait wait = new WebDriverWait(driver, (int) Float.parseFloat(subCase.getTime()));
			switch (subCase.getLocatorType().toLowerCase()) {
			case "url":
				wait.until(ExpectedConditions.urlToBe(Utils.BASE_URL + subCase.getKey()));
				break;

			default:
				break;
			}

			commonVerify(subCase, true);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			commonVerify(subCase, false);
		}
	}

	public void verify(SubCase subCase, ChromeDriver driver) throws IOException {
		if (driver.getCurrentUrl().equalsIgnoreCase(Utils.BASE_URL + subCase.getKey())) {
			SeleniumTest.extentTest.log(LogStatus.PASS, getFormattedMsg(subCase, true));
		} else {
			SeleniumTest.extentTest.log(LogStatus.FAIL, getFormattedMsg(subCase, false));
			Utils.takeScreenShot();
		}
	}

	public void commonVerify(SubCase subCase, boolean isPass) throws IOException {
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
