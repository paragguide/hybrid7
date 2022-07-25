package testcases;

import java.io.FileInputStream;
//import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.Statement;
import java.util.Properties;

import org.openqa.selenium.By;
import org.testng.SkipException;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.LogStatus;

//import utility.ExcelConfig;
import utility.POIConfig;
import utility.screenshot;
import core.Page;
public class LoginTestSeeker extends Page {
	
	@Test(dataProvider="getData")
	public void test(String id,String pwd,String key) throws Exception
	{
		if(key.equals("FALSE") )
		     throw new SkipException("Skipping LoginTest exception");
		else
		logs.debug("test method called "+id+" , "+pwd);
		test.log(LogStatus.PASS,"test method called "+id+" , "+pwd );
		
		Properties OR = new Properties();
		FileInputStream fis = new FileInputStream(System.getProperty("user.dir")+"\\src\\test\\resources\\OR.properties");
		OR.load(fis);
		logs.debug("OR reposotry loaded"); // logs is global variable in Page which is inherited
		test.log(LogStatus.PASS, "OR reposotry loaded");
		String uidxpath=OR.getProperty("username");
		String pwdxpath=OR.getProperty("password");
		String loginbtnxpath=OR.getProperty("login");
		String errmsgxpath=OR.getProperty("errmsg");
		
		
		logs.debug("data loaded from OR file");
		driver.findElement(By.xpath(uidxpath)).clear();
		driver.findElement(By.xpath(uidxpath)).sendKeys(id); // driver is global in Page which is inherited , id is getting data from array 
		
		driver.findElement(By.xpath(pwdxpath)).clear();
		driver.findElement(By.xpath(pwdxpath)).sendKeys(pwd); // data is passed by array .. data added to array in getData() from excel
		screenshot s=new screenshot();
		  s.captureScreenShot(driver,"seekerr-before-login-"+id);
		
		driver.findElement(By.xpath(loginbtnxpath)).click();
		s.captureScreenShot(driver,"seekerr-after-login-"+id);
		String actmsg=driver.findElement(By.xpath(errmsgxpath)).getText();
		
		logs.debug("data entered in web form from excel "+id+" , "+pwd+" status "+actmsg);
		test.log(LogStatus.PASS,"data entered in web form from excel "+id+" , "+pwd+" status "+actmsg);
		
		//String expmsg="Authentication Failed!";
		if(actmsg != null)
		{
			Connection con=POIConfig.makeConnection("c:\\core\\selenium\\Book3.xls"); // have to make connection as before data is added to array from getData() yaha connection open nahi hai
			Statement stm=con.createStatement();
			String qry="insert into Sheet2(username,password,status)values('"+id+"','"+pwd+"','Not Member'"+")";
			
		stm.executeUpdate(qry);		  
		con.commit();
		con.close();
		logs.debug("Not member");
		test.log(LogStatus.PASS,"Not member");
		}
		else{
			Connection con=POIConfig.makeConnection("c:\\core\\selenium\\Book3.xls"); // have to make connection as before data is added to array from getData() yaha connection open nahi hai
			Statement stm=con.createStatement();
			String qry="insert into Sheet2(username,password,status)values('"+id+"','"+pwd+"','Member'"+")";
			
		stm.executeUpdate(qry);		  
		con.commit();
		con.close();
		logs.debug("member");
		test.log(LogStatus.FAIL, "member");
		}
		
	}
	
	@DataProvider
	public static Object[][] getData() throws Exception{
		//System.out.println("data loaded from excel");
		return utility.POIConfig.getData("c:\\core\\selenium\\Book3.xls","Sheet1");
	}

}
