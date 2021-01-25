package com.api.ask.autorizationapi;

import java.util.HashMap;

import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.api.ask.db.DB_Operations;
import com.aventstack.extentreports.Status;

import confProp.ConfigPropExtractData;
import io.restassured.http.Method;
import io.restassured.response.Response;
import testBase.TestBase;

public class GET_ActivateNewlyCreatedStudentAccount extends TestBase {

	public static DB_Operations dbOps = new DB_Operations();

	@Test
	public void activateNewlyCreatedAccount() throws Throwable {

		test = extent.createTest("activateNewlyCreatedAccount");
		// Retriving email from config.proprieties
		String emailOfNewlyCreatedAccount = ConfigPropExtractData.getPropValueByKey("email");

		// Quering DB by email and store all data in HashMap
		HashMap<String, String> dbData = extractDataFromDB("users", "email", emailOfNewlyCreatedAccount);

		// Retriving activationCode from HashMap
		String activationCode = dbData.get("activationCode");
		test.log(Status.INFO, "According to DB User's Activation Code for " + "[" + emailOfNewlyCreatedAccount
				+ "] is [" + activationCode + "]");

		// Retriving userId from HashMap
		String userId = dbData.get("id");
		test.log(Status.INFO,
				"According to DB User ID for [" + emailOfNewlyCreatedAccount + "]" + " is [" + userId + "]");

		// Sending request and storing the responce
		Response response = httpRequest.request(Method.GET, "/activate/" + userId + "/" + activationCode);
		test.log(Status.INFO, "Sent GET request for activating Student account");

		// Validating Status Code
		int actualStatusCode = response.getStatusCode();
		if (actualStatusCode == 200) {
			test.log(Status.INFO, "Actual Status Code [" + actualStatusCode + "]");
		} else if (actualStatusCode == 422) {
			test.log(Status.WARNING, "Actual Status Code [" + actualStatusCode + "]"
					+ " Activation failed. User not found or has already been activated");
		} else if (actualStatusCode == 423) {
			test.log(Status.WARNING,
					"Actual Status Code [" + actualStatusCode + "]" + " Activation failed. Activation code mismatch");
		} else {
			test.log(Status.WARNING, "Actual Status Code [" + actualStatusCode + "]");
		}
		Assert.assertEquals(actualStatusCode, 200);
	}

}
