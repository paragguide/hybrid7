package utility;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

public class POIConfig {
	
	
	static Connection con=null;
	
	public static Connection makeConnection(String workbook)throws Exception
	{
		Class.forName("com.googlecode.sqlsheet.Driver");
		con = DriverManager.getConnection("jdbc:xls:file:"+workbook);
		return con;
	}
	
	public static Object[][] getData(String workbook,String sheetname) throws Exception
    {


con=makeConnection(workbook);
System.out.println("connected by poi..");

Statement stm=con.createStatement();  // default top to bottom


ResultSet rs=stm.executeQuery("select * from "+sheetname); // Sheet name 
ResultSetMetaData rsmt=rs.getMetaData();
int columncount=rsmt.getColumnCount();

rs.last();
int rowcount=rs.getRow();

System.out.println(columncount+" , "+rowcount);
rs.beforeFirst();

Object data[][] = new Object[rowcount][columncount]; //-> size of array 
			
for(int rowNum = 1 ; rowNum <= rowcount ; rowNum++){ 
				
for(int colNum=1 ; colNum<= columncount; colNum++){
                 rs.absolute(rowNum); // point to row  
	String data1= rs.getString(colNum); // getting values from excel
	
		data[rowNum-1][colNum-1]= data1 ; //adding table data in  array , array starts from 0
				}
			}
			
//System.out.println(data);
return data;
    }
	
	public void closeConnection() throws SQLException
	{
		con.close();
	}


}
