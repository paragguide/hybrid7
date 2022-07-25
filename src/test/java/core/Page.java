package core;

import java.io.FileInputStream;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Parameters;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

import utility.screenshot;
import utility.monitoringMail;
import utility.TestConfig;

public class Page {
	public WebDriver driver = null;
	
	public Logger logs =null;
	
	public ExtentTest test=null;
	
	public ExtentReports report=null;
	
	public String reportname;
	   
	@Parameters({"reportname"})
	@BeforeTest
	public void beforeSuite(String reportname)
	{
		report = new ExtentReports(System.getProperty("user.dir")+"\\test-output\\"+reportname+".html");
		  
		  test = report.startTest(reportname);

		  this.reportname = reportname;  // assign local to global
	}
	/**/
	
  @Parameters({ "browser","url","properties" })
  @BeforeMethod
  public void beforeTest(String browser,String url,String properties) throws Exception {
	    
	  Properties R = new Properties();
	  FileInputStream is = new FileInputStream(System.getProperty("user.dir")+"\\src\\test\\resources\\"+properties+".properties");
	  R.load(is);	
	 PropertyConfigurator.configure(R);  // load

	  logs = Logger.getLogger(properties);
	  logs.debug(properties + "logs initialized");
	  
	  /* */

		if(browser.equals("firefox"))
		{
			System.setProperty("webdriver.gecko.driver", "D:\\browserdrivers\\geckodriver.exe");
			driver = new FirefoxDriver();
			logs.debug("firefox init");
			test.log(LogStatus.PASS, "Navigated to Firefox URL");
		}
		else if(browser.equals("chrome"))
		{
			System.setProperty("webdriver.chrome.driver","D:\\browserdrivers\\chromedriver.exe");
			  driver=new ChromeDriver();
			  
			  logs.debug("chrome inint");
			  
			 test.log(LogStatus.PASS, "Navigated to the chrome URL");
		}
		driver.navigate().to(url);
		logs.debug("url open.."+url);
		//test.log(LogStatus.PASS, "Navigated to the specified URL"+url);
		Thread.sleep(2000);  
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(30L, TimeUnit.SECONDS); 
		
  }

  @AfterMethod
  public void afterTest() throws Exception {
	  
	  
	  driver.quit();  // close browser
	  logs.debug("driver quit");
	  test.log(LogStatus.PASS, "Browser close");
	  
	 
  }
 
  @AfterTest
  public void afterSuite()
  {
	  // end extent report
	  report.endTest(test);
	  report.flush();
      logs.debug("report created..");
      // send mail
      monitoringMail mail=new monitoringMail();
	  logs.debug("gmail server init..");
	  test.log(LogStatus.PASS, "Gmail Server inint");
	  try{
		  logs.debug(TestConfig.server);
		  logs.debug(TestConfig.from);
		  logs.debug(TestConfig.to);
		  logs.debug(TestConfig.subject);
		  logs.debug(TestConfig.messageBody);
		  TestConfig.attachmentPath = System.getProperty("user.dir")+"\\test-output\\"+reportname+".html";
		  logs.debug(TestConfig.attachmentPath);
		  TestConfig.attachmentName = reportname;
		  logs.debug(TestConfig.attachmentName);
		  
		mail.sendMail(TestConfig.server, TestConfig.from, TestConfig.to, TestConfig.subject, TestConfig.messageBody, TestConfig.attachmentPath, TestConfig.attachmentName);
		Thread.sleep(7000);
		System.out.println("awake...mail ");
	  }catch(Exception e){e.printStackTrace();}   
		logs.debug("mail sent");
		test.log(LogStatus.PASS, "Mail sent");  
	
  }
  /**/
}
