package Modal;

import java.util.List;

public class Sheet {

	List<TestCase> testCases;
	List<SubCase> rowData;
	String sheetName;

	public List<TestCase> getTestCases() {
		return testCases;
	}

	public void setTestCases(List<TestCase> testCases) {
		this.testCases = testCases;
	}

	public List<SubCase> getRowData() {
		return rowData;
	}

	public void setRowData(List<SubCase> rowData) {
		this.rowData = rowData;
	}

	public String getSheetName() {
		return sheetName;
	}

	public void setSheetName(String sheetName) {
		this.sheetName = sheetName;
	}

}
