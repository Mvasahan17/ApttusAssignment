package com.automation.apttus.Assginment;

import java.io.IOException;

import org.testng.annotations.Test;

import com.automation.seleniumFramework.PageObjects.PageGenricsAPI;
import com.automation.seleniumFramework.PageObjects.PageGenricsWEB;

public class CodingTest {
	
	PageGenricsWEB pg = new PageGenricsWEB();
	PageGenricsAPI api = new PageGenricsAPI();

	@Test
	public void Web_Requirement1() throws IOException, InterruptedException {

		pg.lanuchBrowser("practiceURL");
		pg.signIN_User("email_id", "pwd_user");
		pg.CategoryT_shirts();
		pg.addToCart();
	}

	@Test
	public void API_Requirement3() 
	{	
		api.ResponseVerification("endPointURL",401);
		api.registerStations("endPointURL",201);
		api.getStations("endPointURL",200);
		api.DeleteStation("endPointURL",204);
		api.checkStations("endPointURL",404);

	}


}







