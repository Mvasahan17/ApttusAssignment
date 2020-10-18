package com.automation.seleniumFramework.PageObjects;

import java.io.IOException;
import java.util.HashMap;

import org.codehaus.jackson.map.JsonMappingException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.Assert;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class PageGenricsAPI {
	JSONObject JsonObj;
	JSONArray JsonArr;
	HashMap<String , String> data ;
	public static String station_payload_01;
	public  static String station_payload_02;




	public JSONObject getJsonObj(Response respCont) throws JsonMappingException, IOException, Throwable {
		JsonObj = new JSONObject(respCont.body().asString());
		return JsonObj;
	}
	public JSONArray getJsonArr(Response respCont) throws JsonMappingException, IOException, Throwable {
		JsonArr = new JSONArray(respCont.body().asString());
		return JsonArr;
	}

	/**
	 * 
	 * @param testURL
	 * @param HTTP_RESPONSE_CODE
	 */
	public void ResponseVerification(String testURL , int HTTP_RESPONSE_CODE) {
		String strMsgErrCode = null;

		try {
			RestAssured.baseURI=PageUtilities.getProValue(testURL);

			RequestSpecification request = RestAssured.given();
			Response response = request.get();

			//Verifing HTTP RESPONSE CODE 
			Assert.assertEquals(response.getStatusCode(), HTTP_RESPONSE_CODE);
			System.out.println("HTTP RESPONSE CODE  : " + response.getStatusCode());

			//Verifing Response Body
			JSONObject jsonObj = getJsonObj(response);
			if(jsonObj.has("cod") && jsonObj.has("message")) {

				String err_cod = jsonObj.getString("cod");
				String Msg = jsonObj.getString("message");

				if(err_cod.isEmpty() || Msg.isEmpty()) {
					strMsgErrCode = null;
				}else {

					strMsgErrCode ="Error Code - "+ err_cod+ "  AND  " + "Error Msg - " +"'"+Msg+"'";
					System.out.println(strMsgErrCode);
					Thread.sleep(2000);
				}
			}else {
				System.out.println("Verify error code exist or not " +
						"<font color='red'>Error codee do not exists</font>");
			}


		} catch (Throwable e) {
			e.printStackTrace();
		}

	}
	/**
	 * 
	 * @param url
	 * @param HTTP_RESPONSE_CODE
	 */
	public void registerStations(String url , int HTTP_RESPONSE_CODE) {

		data=PageUtilities.readDataFromExcel("Testdata", "Testcase", "10101");

		try {
			RestAssured.baseURI=PageUtilities.getProValue(url);
			RequestSpecification request = RestAssured.given();
			request.queryParam(PageUtilities.getProValue("Auth_key"), PageUtilities.getProValue("Auth_value"));
			request.header("Content-Type","application/json");		

			Response response_paylod_01 = request.spec(request).body(data.get("Input_1")).post();
			Response response_paylod_02 = request.spec(request).body(data.get("Input_2")).post();

			Assert.assertEquals(response_paylod_01.getStatusCode(), HTTP_RESPONSE_CODE);
			Assert.assertEquals(response_paylod_02.getStatusCode(), HTTP_RESPONSE_CODE);


			JSONObject jsonObj_01 = getJsonObj(response_paylod_01);
			JSONObject jsonObj_02 = getJsonObj(response_paylod_02);

			station_payload_01 = jsonObj_01.getString("ID");
			station_payload_02 = jsonObj_02.getString("ID");

			System.out.println("HTTP RESPONSE CODE for payload_01  : " + response_paylod_01.getStatusCode() +"\n" 
					+"Station ID for payLoad_01  : " +station_payload_01);
			System.out.println("HTTP RESPONSE CODE for payload_02  : " + response_paylod_01.getStatusCode() +"\n"
					+"Station ID for payLoad_02  : " +station_payload_02);


		} catch (Throwable e) {
			e.printStackTrace();
		}

	}
	/**
	 * 	
	 * @param url
	 * @param HTTP_RESPONSE_CODE
	 */
	public void getStations(String url ,int HTTP_RESPONSE_CODE) {

		try {
			RestAssured.baseURI=PageUtilities.getProValue(url);
			RequestSpecification request = RestAssured.given();
			request.queryParam(PageUtilities.getProValue("Auth_key"), PageUtilities.getProValue("Auth_value"));
			Response response = request.get();

			Assert.assertEquals(response.getStatusCode(), HTTP_RESPONSE_CODE);

			JSONArray jsonObj_01 = getJsonArr(response);

			for(int i =0; i<=jsonObj_01.length()-1;i++) {

				String station_id = JsonArr.getJSONObject(i).getString("id");

				if(station_id.equals(station_payload_01) || station_id.equals(station_payload_02)) {

					System.out.println(station_id  + "   : Successfully stored in the DB and their values are the same as specified in the registration request");

				} 

			}

		} catch (Throwable e) {
			e.printStackTrace();
		}


	}
	/**
	 * 
	 * @param url
	 * @param HTTP_RESPONSE_CODE
	 */
	public void DeleteStation(String url , int HTTP_RESPONSE_CODE) {

		try {
			RestAssured.baseURI=PageUtilities.getProValue(url);
			RequestSpecification request = RestAssured.given();
			request.queryParam(PageUtilities.getProValue("Auth_key"), PageUtilities.getProValue("Auth_value"));

			Response response_st01 = request.spec(request).delete("/"+station_payload_01);
			Response response_st02 = request.spec(request).delete("/"+station_payload_02);


			Assert.assertEquals(response_st01.getStatusCode(), HTTP_RESPONSE_CODE);
			Assert.assertEquals(response_st02.getStatusCode(), HTTP_RESPONSE_CODE);

			System.out.println("Delet the created station id_01  :"+ station_payload_01 +"\n" +
					"Delet the created station id_02  :" + station_payload_02);


		} catch(Throwable e) {
			e.printStackTrace();
		}

	}
	/**
	 * 
	 * @param url
	 * @param HTTP_RESPONSE_CODE
	 */
	public void checkStations(String url, int HTTP_RESPONSE_CODE) {

		try {
			RestAssured.baseURI=PageUtilities.getProValue(url);
			RequestSpecification request = RestAssured.given();
			request.queryParam(PageUtilities.getProValue("Auth_key"), PageUtilities.getProValue("Auth_value"));

			Response response_st01 = request.spec(request).get("/"+station_payload_01);
			Response response_st02 = request.spec(request).get("/"+station_payload_02);


			Assert.assertEquals(response_st01.getStatusCode(), HTTP_RESPONSE_CODE);
			Assert.assertEquals(response_st02.getStatusCode(), HTTP_RESPONSE_CODE);


			JSONObject jsonObj_01 = getJsonObj(response_st01);
			JSONObject jsonObj_02 = getJsonObj(response_st02);

			station_payload_01 = jsonObj_01.getString("message");
			station_payload_02 = jsonObj_02.getString("message");

			if(station_payload_01.equalsIgnoreCase("Station not found") && 
					station_payload_02.equalsIgnoreCase("Station not found")) {

				System.out.println("HTTP RESPONSE CODE for payload_01  : " + response_st01.getStatusCode() +"\n" 
						+"Error message for Search Station  : " +station_payload_01);
				System.out.println("HTTP RESPONSE CODE for payload_02  : " + response_st02.getStatusCode() +"\n"
						+"Error message for Search Station  : " + station_payload_02 );


			}


		} catch(Throwable e) {
			e.printStackTrace();
		}

	}
}