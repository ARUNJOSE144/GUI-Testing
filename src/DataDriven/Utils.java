package DataDriven;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;

import com.relevantcodes.extentreports.LogStatus;

import Modal.SubCase;

public class Utils {
	static String BASE_URL = "http://localhost:3000";
	// static String BASE_URL = "http://10.0.0.95:9094/CoMS-UI-Testing";

	static String DRIVER_PATH = System.getProperty("user.dir")
			+ "\\SeleniumWebDriver\\chromedriver_win32\\chromedriver.exe";
	static String OUTPUT_FOLDER_PREFIX = "testResults\\result";
	static String OUTPUT_FILE_PREFIX = "Report";
	static String INPUT_FILE_PATH = "\\testCase\\CommissionSystemTestCases.xlsx";
	static int IMPLICIT_WAIT = 3;

	static String NEW_OUTPUT_FOLDER_PATH = "";
	static String OUTPUT_FOLDER = System.getProperty("user.dir");
	static String VIDEO_PATH = "";
	static String VIDEO_FILE_NAME = "";
	static String VIDEO_FORMAT = ".mov";
	static boolean VIDEO_REQUIRED = true;

	static boolean validate(String val) {
		if (val != null && !val.equalsIgnoreCase(""))
			return true;
		else
			return false;
	}

	static void takeScreenShot() throws IOException {
		try {
			TakesScreenshot scrShot = ((TakesScreenshot) SeleniumTest.driver);
			File SrcFile = scrShot.getScreenshotAs(OutputType.FILE);
			File DestFile = new File(NEW_OUTPUT_FOLDER_PATH + "\\Img\\Img" + getDateString() + ".png");

			FileUtils.copyFile(SrcFile, DestFile);
			SeleniumTest.extentTest.log(LogStatus.INFO, SeleniumTest.extentTest.addScreenCapture(DestFile.getPath()));
			if (Utils.VIDEO_REQUIRED)
				SeleniumTest.extentTest.log(LogStatus.INFO, "<a href='" + Utils.VIDEO_PATH + Utils.VIDEO_FILE_NAME
						+ VIDEO_FORMAT + "'><span>Download Video</span>");
		} catch (WebDriverException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	static String getDateString() {
		Date d = new Date();
		return d.getDate() + "_" + d.getMonth() + "_" + d.getYear() + "_" + d.getHours() + "_" + d.getMinutes() + "_"
				+ d.getSeconds();

	}

	static void copyFile() throws IOException {
		File sourceFile = new File(System.getProperty("user.dir") + INPUT_FILE_PATH);
		File destFile = new File(NEW_OUTPUT_FOLDER_PATH + INPUT_FILE_PATH);

		FileUtils.copyFile(sourceFile, destFile);
	}

	public static void addOrRemoveBoarder(SubCase subCase, boolean isAdd) {
		WebElement element = null;
		if (validate(subCase.getKey()) && (!subCase.getLocatorType().equalsIgnoreCase("url"))) {
			try {

				switch (subCase.getLocatorType()) {
				case "name":
					element = SeleniumTest.driver.findElement(By.name(subCase.getKey()));
					break;

				case "id":
					element = SeleniumTest.driver.findElement(By.id(subCase.getKey()));
					break;

				case "class":
					element = SeleniumTest.driver.findElement(By.className(subCase.getKey()));
					break;

				case "xpath":
					element = SeleniumTest.driver.findElement(By.xpath(subCase.getKey()));
					break;

				default:
					break;
				}

				JavascriptExecutor js = (JavascriptExecutor) SeleniumTest.driver;
				if (isAdd)
					js.executeScript("arguments[0].setAttribute('style', 'border: 3px solid red')", element);
				else
					js.executeScript("arguments[0].setAttribute('style', 'border: 0px')", element);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

}
