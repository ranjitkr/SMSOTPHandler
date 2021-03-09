package com.ranjit.utilities;

import java.util.concurrent.TimeUnit;
import java.util.stream.StreamSupport;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.google.inject.spi.Message;
import com.mongodb.connection.Stream;
import com.twilio.Twilio;
import com.twilio.base.ResourceSet;

import io.github.bonigarcia.wdm.WebDriverManager;

public class OTPSMSHandle {
	
	public static final String ACCOUNT_SID="ACe7cae32db72d72a9b1d3ae0af7bfa96d";
	public static final String AUTH_TOKEN="5f9dae9fe035b681ff9a681b451064d3";
	

	

	public static void main(String[] args) {
		WebDriverManager.chromedriver().setup();
		WebDriver driver=new ChromeDriver();
		driver.manage().window().maximize();
		driver.manage().deleteAllCookies();
		driver.manage().timeouts().pageLoadTimeout(30L, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
		driver.get("https://www.amazon.in/");
		
		Actions action=new Actions(driver);
		WebElement signInLink = driver.findElement(By.xpath("//div[@id='nav-tools']/descendant::a[2]"));
		action.moveToElement(signInLink).perform();
		
		WebElement startHere = driver.findElement(By.xpath("//a[text()='Start here.' and @rel='nofollow']"));
		WebDriverWait wait=new WebDriverWait(driver, 20);
		wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath("//a[text()='Start here.' and @rel='nofollow']")));
		startHere.click();
		
		
		driver.findElement(By.id("ap_customer_name")).sendKeys("ranjitTest");
		WebElement dropdown = driver.findElement(By.xpath("//div[@class='a-fixed-left-grid-inner']/descendant::span[@class='a-button-inner']"));
		wait.until(ExpectedConditions.visibilityOf(dropdown));
		dropdown.click();
		
		driver.findElement(By.xpath("//li[@role='option']/a[contains(text(),'United States +1')]")).click();
		driver.findElement(By.xpath("//input[@data-validation-id='phoneNumber']")).sendKeys("14803866219");
		
		driver.findElement(By.id("ap_password")).sendKeys("Ranjit@093246");
		driver.findElement(By.id("continue")).click();
		
		// get the OTP using Twilio APIs:
				Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
				String smsBody = getMessage();
				System.out.println(smsBody);
				String OTPNumber = smsBody.replaceAll("[^-?0-9]+", " ");
				System.out.println(OTPNumber);
				
				driver.findElement(By.id("auth-pv-enter-code")).sendKeys(OTPNumber);

			}

	public static String getMessage() {
		return getMessages().filter(m -> m.getDirection().compareTo(Message.Direction.INBOUND) == 0)
				.filter(m -> m.getTo().equals("+13343734019")).map(Message::getBody).findFirst()
				.orElseThrow(IllegalStateException::new);
	}

	private static Stream<Message> getMessages() {
		ResourceSet<Message> messages = Message.reader(ACCOUNT_SID).read();
		return StreamSupport.stream(messages.spliterator(), false);
	}


		
	

}
