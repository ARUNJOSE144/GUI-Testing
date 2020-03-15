package DataDriven;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriverException;

import com.relevantcodes.extentreports.LogStatus;

public class Utils {
	static String BASE_URL = "http://localhost:3000";
	//static String BASE_URL = "http://10.0.0.95:9094/CoMS-UI-Testing";
	
	
	
	static String DRIVER_PATH = "C:\\Program Files\\SeleniumWebDriver\\chromedriver_win32\\chromedriver.exe";
	static String OUTPUT_FOLDER_PREFIX = "testResults\\result";
	static String OUTPUT_FILE_PREFIX = "Report";
	static String INPUT_FILE_PATH = "\\testCase\\CommissionSystemTestCases.xlsx";
	static int IMPLICIT_WAIT = 3;

	static String NEW_OUTPUT_FOLDER_PATH = "";
	static String OUTPUT_FOLDER = System.getProperty("user.dir");

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
			SeleniumTest.extentTest.log(LogStatus.FAIL, SeleniumTest.extentTest.addScreenCapture(DestFile.getPath()));
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
		SeleniumTest.extentTest.log(LogStatus.FAIL, SeleniumTest.extentTest.addScreenCapture(destFile.getPath()));
	}

}
