package com.automation.seleniumFramework.PageObjects;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;

import io.github.bonigarcia.wdm.WebDriverManager;

public class PageGenricsWEB  {

	public static JavascriptExecutor js;
	public static WebDriver driver;

	public PageGenricsWEB(){
		WebDriverManager.chromedriver().setup();
		ChromeOptions options = new ChromeOptions();
		options.addArguments("--disable-notifications");
		driver = new ChromeDriver(options);	  
	}

	
	By email =By.id("email");
	By pwd = By.id("passwd");
	By login = By.id("SubmitLogin");
	By login_title=By.className("login");
	//Could not use direct locators for most of the elements 
	By addToCart=By.xpath("//p[@id='add_to_cart']/button");
	By sucs_msg=By.xpath("(//div[@id='layer_cart']//div/h2)[1]");
	By name_product	=By.xpath("//div[@id='layer_cart']//div/span[text()='Faded Short Sleeve T-shirts']");
	By color_product=By.xpath("//div[@id='layer_cart']//div/span[contains(text(),'Orange')]");
	By quty_product=By.xpath("(//div[@id='layer_cart']//div[@class='layer_cart_product_info']//div)[1]");
	By price_product=By.xpath("(//div[@id='layer_cart']//div[@class='layer_cart_product_info']//div)[2]");
	By checkout = By.xpath("//div[@id='layer_cart']//div/a");
	By tab_tshrts= By.xpath("//header/div[3]/div[1]/div[1]/div[6]/ul[1]/li[3]/a[1]");
	By product_check= By.xpath("(//div[@class='product-count'])[1]");
	By result_tshrt=By.xpath("//ul/li//div//h5/a[@title='Faded Short Sleeve T-shirts']");
	/**
	 * 
	 * @param url
	 * @throws IOException
	 */ 
	public void lanuchBrowser(String url)   {

		try {
			driver.manage().deleteAllCookies();
			driver.manage().window().maximize();
			driver.get(PageUtilities.getProValue(url));
			driver.manage().timeouts().pageLoadTimeout(40, TimeUnit.SECONDS);
			driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	/**\
	 * 
	 * @param username
	 * @param password
	 * @throws InterruptedException 
	 */
	public void signIN_User(String username , String password) throws InterruptedException {

		try {
			Thread.sleep(2000);
			driver.findElement(login_title).click();
			driver.manage().timeouts().pageLoadTimeout(20, TimeUnit.SECONDS);

			if(driver.getTitle().equalsIgnoreCase("Login - My Store")) {
				driver.findElement(email).sendKeys(PageUtilities.getProValue(username));
				driver.findElement(pwd).sendKeys(PageUtilities.getProValue(password));
				driver.findElement(login).click();
			}else {
				System.out.println("check the Application");
			}

		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	/**
	 * 
	 * @throws InterruptedException
	 */
	public void CategoryT_shirts() throws InterruptedException{
		Actions act = new Actions(driver);

		try {
			driver.manage().timeouts().pageLoadTimeout(20, TimeUnit.SECONDS);
			//WebElement btn_TShirts = driver.findElement(By.linkText("T-shirts"));
			WebElement btn_TShirts = driver.findElement(tab_tshrts);
			act.moveToElement(btn_TShirts).doubleClick().build().perform();

			driver.findElement(product_check).isEnabled();
			Thread.sleep(2000);
			WebElement product_tshrt = driver.findElement(result_tshrt);

			if(product_tshrt.isDisplayed()) {
				act.moveToElement(product_tshrt).build().perform();
				act.click(product_tshrt).build().perform();
			}		
			driver.manage().timeouts().pageLoadTimeout(20, TimeUnit.SECONDS);

			System.out.println(driver.getTitle());

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void addToCart() {
		try {

			driver.findElement(addToCart).click();
			Thread.sleep(2000);
			String success_msg  =driver.findElement(sucs_msg).getText();
			String pdt_name 	=driver.findElement(name_product).getText();
			String pdt_color    =driver.findElement(color_product).getText();
			String pdt_Quty     =driver.findElement(quty_product).getText();
			String pdt_price    =driver.findElement(price_product).getText();

			Assert.assertEquals(success_msg,"Product successfully added to your shopping cart");
			Assert.assertEquals(pdt_name,"Faded Short Sleeve T-shirts");
			Assert.assertEquals(pdt_color,"Orange, S");
			Assert.assertEquals(pdt_Quty,"Quantity 1");
			Assert.assertEquals(pdt_price,"Total $16.51");

			driver.findElement(checkout).click();

			System.out.println(driver.getTitle());

			driver.quit();

		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		driver.quit();

	}
}
