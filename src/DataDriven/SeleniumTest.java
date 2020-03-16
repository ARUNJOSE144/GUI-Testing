package DataDriven;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.openqa.selenium.chrome.ChromeDriver;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;

import Modal.Sheet;
import Modal.SubCase;
import Modal.Test;
import Modal.TestCase;
import atu.testrecorder.ATUTestRecorder;
import atu.testrecorder.exceptions.ATUTestRecorderException;

public class SeleniumTest {

	static ChromeDriver driver;
	static Test test = null;
	ReadExcelFile file;
	static ExtentTest extentTest;
	static ExtentReports report;
	static ATUTestRecorder recorder;

	public static void main(String arg[]) throws InterruptedException, IOException, ATUTestRecorderException {
		SeleniumTest seleniumTest = new SeleniumTest();
		seleniumTest.readFile();
		Utils.NEW_OUTPUT_FOLDER_PATH = Utils.OUTPUT_FOLDER + "\\" + Utils.OUTPUT_FOLDER_PREFIX + "_"
				+ Utils.getDateString();
		report = new ExtentReports(
				Utils.NEW_OUTPUT_FOLDER_PATH + "\\" + Utils.OUTPUT_FILE_PREFIX + "_" + Utils.getDateString() + ".html");

		seleniumTest.openUrl();
		seleniumTest.executeTestCases(test);
		Utils.copyFile();
		report.flush();
		seleniumTest.quit(driver);

	}

	public static void startTest(TestCase testCase) {
		extentTest = report.startTest(testCase.getTestCaseName());
	}

	public void openUrl() throws InterruptedException {
		try {
			System.setProperty("webdriver.chrome.driver", Utils.DRIVER_PATH);
			driver = new ChromeDriver();
			driver.manage().timeouts().implicitlyWait(Utils.IMPLICIT_WAIT, TimeUnit.SECONDS);

			driver.manage().window().maximize();
			driver.get(Utils.BASE_URL);

		} catch (Exception e) { // TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void readFile() {
		file = new ReadExcelFile(System.getProperty("user.dir") + Utils.INPUT_FILE_PATH);
		test = new Test();
		List<Sheet> rowSheets = new ArrayList<Sheet>();
		for (int i = 0; i < file.wb.getNumberOfSheets(); i++) {

			Sheet sheet = new Sheet();
			sheet.setRowData(getRowSheet(i, file.wb.getSheetAt(i)));
			rowSheets.add(sheet);

		}
		test.setRowSheets(rowSheets);

		// Convering To DTO modal
		List<Sheet> sheets = new ArrayList<Sheet>();
		for (Sheet rowSheet : test.getRowSheets()) {
			Sheet sheet = getSheet(rowSheet);
			sheets.add(sheet);
		}

		test.setSheets(sheets);
	}

	public Sheet getSheet(Sheet rowSheet) {
		Sheet sheet = new Sheet();
		TestCase testCase = null;
		SubCase subCase = null;
		List<SubCase> subCases = null;
		List<TestCase> testCases = new ArrayList<TestCase>();

		for (SubCase rowSubCase : rowSheet.getRowData()) {
			if (rowSubCase != null && !Utils.validate(rowSubCase.getOperation())) {
				continue;
			}

			if (rowSubCase != null && rowSubCase.getOperation().equalsIgnoreCase("start")) {
				testCase = new TestCase();
				testCase.setTestCaseName(rowSubCase.getData());
				subCases = new ArrayList<SubCase>();
				continue;
			}

			if (rowSubCase != null && rowSubCase.getOperation().equalsIgnoreCase("end")) {
				testCase.setSubCases(subCases);
				testCases.add(testCase);
				continue;
			}

			if (rowSubCase != null && Utils.validate(rowSubCase.getOperation())) {
				subCases.add(rowSubCase);
				continue;
			}

		}

		sheet.setTestCases(testCases);

		return sheet;
	}

	public List<SubCase> getRowSheet(int sheetNumber, XSSFSheet sheet) {
		List<SubCase> sheetData = new ArrayList<SubCase>();
		System.out.println("No of records : " + sheet.getLastRowNum());
		for (int i = 1; i <= sheet.getLastRowNum(); i++) {
			SubCase subCase = new SubCase();
			subCase.setOperation(file.getData(sheetNumber, i, 0));
			subCase.setLocatorType(file.getData(sheetNumber, i, 1));
			subCase.setKey(file.getData(sheetNumber, i, 2));
			subCase.setData(file.getData(sheetNumber, i, 3));
			subCase.setUrl(file.getData(sheetNumber, i, 4));
			subCase.setTime(file.getData(sheetNumber, i, 5));
			subCase.setErrorMsg(file.getData(sheetNumber, i, 6));
			sheetData.add(subCase);
		}
		return sheetData;
	}

	public void executeTestCases(Test test) throws InterruptedException, IOException, ATUTestRecorderException {
		for (Sheet sheet : test.getSheets()) {
			System.out.println(">>>>>>>>>>>>>>>>>>>>>  Sheet start>>>>>>>>>>>>>>>>>>>>>>>>>");
			for (TestCase testCase : sheet.getTestCases()) {
				System.out.println("----------------------  Test Case start---------------------------");
				startTest(testCase);
				startVideo();
				for (SubCase subCase : testCase.getSubCases()) {
					System.out.println("---------------------- Sub Test Case start---------------------------");
					System.out.println("Operation : " + subCase.getOperation());
					System.out.println("Locator Type : " + subCase.getLocatorType());
					System.out.println("Key: " + subCase.getKey());
					System.out.println("Data : " + subCase.getData());
					System.out.println("Url : " + subCase.getUrl());
					System.out.println("TimeOut : " + subCase.getTime());
					System.out.println("Error Msg : " + subCase.getErrorMsg());

					Actions actions = new Actions();
					actions.doAction(subCase, driver);

					System.out.println("---------------------- Sub Test Case  end---------------------------");
				}
				if (Utils.VIDEO_REQUIRED)
					recorder.stop();

				endTest();
				// TimeUnit.SECONDS.sleep(1);
				System.out.println("----------------------  Test Case end---------------------------");
			}
			System.out.println(">>>>>>>>>>>>>>>>>>>>>  Sheet  end>>>>>>>>>>>>>>>>>>>>>>>>>");
		}
	}

	public void quit(ChromeDriver driver) throws ATUTestRecorderException {

		driver.quit();

	}

	public static void endTest() {
		report.endTest(extentTest);
	}

	public static void startVideo() throws ATUTestRecorderException {
		if (Utils.VIDEO_REQUIRED) {
			Utils.VIDEO_PATH = Utils.NEW_OUTPUT_FOLDER_PATH+"\\video";
			new File(Utils.VIDEO_PATH).mkdir();
			Utils.VIDEO_FILE_NAME = "video" + Utils.getDateString();
			recorder = new ATUTestRecorder(Utils.VIDEO_PATH, Utils.VIDEO_FILE_NAME, false);
			recorder.start();
		}
	}

}
