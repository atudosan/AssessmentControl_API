package com.api.ask.autorizationapi;

import org.json.simple.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;
import io.restassured.*;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class TC001PostSignIn {

	@Test
	public void signIntoAccount() {

		// Specify base URI
		RestAssured.baseURI = "http://ask-stage.portnov.com/api/v1";

		// Request object
		RequestSpecification httpRequest = RestAssured.given();

		// Create request body - request payload
		JSONObject requestParams = new JSONObject();

		requestParams.put("email", "atudosan@yahoo.com");
		requestParams.put("password", "12345");

		// specifing in header that we send json type as payload
		httpRequest.header("Content-Type", "application/json");
		
		// attaching json object to our body request
		httpRequest.body(requestParams.toJSONString());

		// Responce Object
		Response response = httpRequest.request(Method.POST, "/sign-in");

		// status code validation
		int statusCode = response.getStatusCode();
		Assert.assertEquals(statusCode, 200);

		String responseBody = response.getBody().asString();
		//System.out.println(responseBody);
		
		String token = response.jsonPath().get("token");
		
		System.out.println("token = "+token);

	}

}
