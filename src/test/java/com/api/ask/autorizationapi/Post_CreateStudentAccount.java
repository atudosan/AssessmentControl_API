package com.api.ask.autorizationapi;

import java.util.HashMap;

import org.json.simple.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.api.ask.db.DB_Operations;
import com.aventstack.extentreports.Status;

import confProp.ConfigPropExtractData;
import io.restassured.http.Method;
import io.restassured.response.Response;
import testBase.TestBase;

public class Post_CreateStudentAccount extends TestBase {

	@SuppressWarnings("unchecked")
	@Test
	public void createAccount() throws Throwable {

		test = extent.createTest("createAccount");

		// Create request body - request payload
		JSONObject signUpInfo = new JSONObject();
		signUpInfo.put("email", ConfigPropExtractData.getPropValueByKey("email"));
		signUpInfo.put("name", ConfigPropExtractData.getPropValueByKey("name"));
		signUpInfo.put("group", ConfigPropExtractData.getPropValueByKey("group"));
		signUpInfo.put("password", ConfigPropExtractData.getPropValueByKey("password"));
		test.log(Status.INFO, "Built body request(email, name, group, password)");

		// specifing in header that we send json type as payload
		httpRequest.header("Content-Type", "application/json");
		test.log(Status.INFO, "Specified body type in Header request ");

		// attaching json object to our body request
		httpRequest.body(signUpInfo.toJSONString());
		test.log(Status.INFO, "Atached body info to request object");

		// Responce Object
		Response response = httpRequest.request(Method.POST, "/sign-up");
		test.log(Status.INFO, "Send POST Request");
		
		SoftAssert softAssert = new SoftAssert();

		int actualStatusCode = response.getStatusCode();	
		softAssert.assertEquals(actualStatusCode, 200);
		if (actualStatusCode == 200) {
			test.log(Status.INFO, "Actual Status Code [" + actualStatusCode + "]");
		} else {
			test.log(Status.WARNING, "Actual Status Code [" + actualStatusCode + "]"
					+ " but should be [200]");
		}

		String actualStatus = response.jsonPath().getString("status");
		softAssert.assertEquals(actualStatus, "success");
		if (actualStatus.contains("success")) {
			test.log(Status.INFO, "Actual Status [" + actualStatus + "]");
		} else {
			test.log(Status.WARNING, "Actual Status [" + actualStatus + "]"
					+ " but should be [success]");
		}
		

		String actualMessage = response.jsonPath().getString("message");
		softAssert.assertEquals(actualMessage, "User was created");
		if (actualMessage.contains("User was created")) {
			test.log(Status.INFO, "Actual Status [" + actualMessage + "]");
		} else {
			test.log(Status.WARNING, "Actual Status [" + actualMessage + "]"
					+ " but should be [success]");
		}
	

		softAssert.assertAll();
	}

}
