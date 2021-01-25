package testBase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;

import com.api.ask.db.DB_Operations;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

import confProp.ConfigPropExtractData;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class TestBase {

	protected static RequestSpecification httpRequest = RestAssured.given();
	protected RequestSpecification uri;
	protected Response response;
	protected ExtentReports extent;
	protected ExtentTest test;

	@BeforeClass
	public void setUp() throws Exception {
		String uriString = ConfigPropExtractData.getPropValueByKey("url");
		uri = new RequestSpecBuilder().setBaseUri(uriString).build();
		httpRequest = RestAssured.given(uri);
	}

	@BeforeTest
	public void setExtentReport() throws Exception {
		SimpleDateFormat format = new SimpleDateFormat("dd-MM-yy HH-mm-ss");
		Date date = new Date();
		String actualDate = format.format(date);
		String reportPath = System.getProperty("user.dir") + "/Reports/ExecutionReport_" + actualDate + ".html";

		ExtentSparkReporter sparkReport = new ExtentSparkReporter(reportPath);
		extent = new ExtentReports();
		extent.attachReporter(sparkReport);

		sparkReport.config().setDocumentTitle("DocumentTitle");
		sparkReport.config().setTheme(Theme.DARK);
		sparkReport.config().setReportName("ReportName");

		String url = ConfigPropExtractData.getPropValueByKey("url");

		extent.setSystemInfo("Executed on envirnoment: ", url);
		extent.setSystemInfo("Executed on OS: ", System.getProperty("os.name"));
		extent.setSystemInfo("Executed by User: ", System.getProperty("user.name"));
	}

	@AfterMethod
	public void tearDown(ITestResult result) {
		if (result.getStatus() == ITestResult.FAILURE) {
			test.log(Status.FAIL, "TEST CASE [" + result.getName() + "] IS FAILED on"
					+ " [" + result.getThrowable() + "]");
		} else if (result.getStatus() == ITestResult.SKIP) {
			test.log(Status.SKIP, "Test Case [" + result.getName() + "] IS SKIPED.");
		} else if (result.getStatus() == ITestResult.SUCCESS) {
			test.log(Status.PASS, "Test Case [" + result.getName() + "] IS PASSED.");
		}
	}

	@AfterTest
	public void endReport() {
		extent.flush();
	}
	
	
	// this method will extract data from DB
		protected HashMap<String, String> extractDataFromDB(String nameOfTable, String nameOfColumn, String recordByColumn)
				throws Throwable {
			HashMap<String, String> dbData = null;
			DB_Operations dbOps = new DB_Operations();

			// build sql query by passing nameOfTable,
			String sqlQuery = "SELECT * FROM `" + nameOfTable + "` WHERE " + nameOfColumn + " = '" + recordByColumn + "'";
			dbData = dbOps.getSqlResultInMap(sqlQuery);

			return dbData;
		}

}
