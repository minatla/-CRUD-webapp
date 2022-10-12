/*  Name: Mina Lam
     Course: CNT 4714 – Spring 2022 – Project Four 
     Assignment title:  A Three-Tier Distributed Web-Based Application 
     Date:  April 24, 2022 
*/

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import jakarta.servlet.*;
import jakarta.servlet.http.*;

public class ClientUserAppServlet extends HttpServlet {
	
	private Connection connection;
	private Statement statement;

	@Override
	public void init(ServletConfig config) throws ServletException {
		// override
		super.init(config);
		
		
		try 
		{
			Class.forName(config.getInitParameter("databaseDriver"));
			connection = DriverManager.getConnection(config.getInitParameter("databaseName"),
					config.getInitParameter("username"), config.getInitParameter("password"));
			statement = connection.createStatement();
		}

		catch (Exception e) 
		{
			e.printStackTrace();
			throw new UnavailableException(e.getMessage());
		}

	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String textBox = request.getParameter("textBox");
		String textBoxLC = textBox.toLowerCase();
		String result = null;
		
		//tries select
		if (textBoxLC.contains("select")) 
		{

			try 
			{
				result = doSelectQuery(textBoxLC);
			} catch (SQLException e) 
			{
				result = "<span>" + e.getMessage() + "</span>";

				e.printStackTrace();
			}
		}
		//does CRUD functions
		else {
			try 
			{
				result = doUpdateQuery(textBoxLC);
			}catch(SQLException e) 
			{
				result = "<span>" + e.getMessage() + "</span>";

				e.printStackTrace();
			}
		}

		HttpSession session = request.getSession();
		session.setAttribute("result", result);
		session.setAttribute("textBox", textBox);
		RequestDispatcher dispatch = getServletContext().getRequestDispatcher("/clientHome.jsp");
		dispatch.forward(request, response);
	}

	

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

	// executes a select query and creates table 
	public String doSelectQuery(String textBox) throws SQLException {
		String result;
		
		ResultSet table = statement.executeQuery(textBox);

		ResultSetMetaData metaData = table.getMetaData();
		int numOfCol = metaData.getColumnCount();
		String tableHTMLStart = "<div class='container-fluid'><div class='row justify-content-center'><div class='table-responsive-sm-10 table-responsive-md-10 table-responsive-lg-10'><table class='table'>";
		String tableColumns = "<thead class='thead-dark'><tr>";
		
		for (int i = 1; i <= numOfCol; i++) 
		{
			tableColumns += "<th scope='col'>" + metaData.getColumnName(i) + "</th>";
		}

		tableColumns += "</tr></thead>";

		String tableBodyHTML = "<tbody>";
		while (table.next()) 
		{
			tableBodyHTML += "<tr>";
			for (int i = 1; i <= numOfCol; i++) 
			{
				if (i == 1)
					tableBodyHTML += "<td scope'row'>" + table.getString(i) + "</th>";
				else
					tableBodyHTML += "<td>" + table.getString(i) + "</th>";
			}
			tableBodyHTML += "</tr>";
		}

		tableBodyHTML += "</tbody>";
		String tableHTMLEnd = "</table></div></div></div>";
		result = tableHTMLStart + tableColumns + tableBodyHTML + tableHTMLEnd;

		return result;
	}
	
	private String doUpdateQuery(String textBoxLC) throws SQLException {
		String result = null;
		int numOfRowsUpdated = 0;
		
		//checks quantity
		ResultSet beforeQuantityCheck = statement.executeQuery("select COUNT(*) from shipments where quantity >= 100");
		beforeQuantityCheck.next();
		int numOfShipmentsGreaterThan100Before = beforeQuantityCheck.getInt(1);
		 
		statement.executeUpdate("create table shipmentsBeforeUpdate like shipments");
		statement.executeUpdate("insert into shipmentsBeforeUpdate select * from shipments");
		
		//executes update
		numOfRowsUpdated = statement.executeUpdate(textBoxLC);
		result = "<div> The statement executed succesfully.</div><div>" + numOfRowsUpdated + " row(s) affected</div>";
		
		ResultSet afterQuantityCheck = statement.executeQuery("select COUNT(*) from shipments where quantity >= 100");
		afterQuantityCheck.next();
		int numOfShipmentsGreaterThan100After = afterQuantityCheck.getInt(1);
		
		result += "<div>" + numOfShipmentsGreaterThan100Before + " < " + numOfShipmentsGreaterThan100After + "</div>";
		
		if(numOfShipmentsGreaterThan100Before < numOfShipmentsGreaterThan100After) 
		{

			int numberOfRowsAffectedAfterIncrement = statement.executeUpdate("update suppliers set status = status + 5 where snum in ( select distinct snum from shipments left join shipmentsBeforeUpdate using (snum, pnum, jnum, quantity) where shipmentsBeforeUpdate.snum is null)");
			
			result += "<div>Business Logic Detected! - Updating Supplier Status</div>";
			result += "<div>Business Logic Updated " + numberOfRowsAffectedAfterIncrement + " Supplier(s) status marks</div>";
		}
		else if(numOfShipmentsGreaterThan100Before == numOfShipmentsGreaterThan100After)
		{
			result += "<div> Business Logic Not Triggered!<div>";
		}
		
		statement.executeUpdate("drop table shipmentsBeforeUpdate");
		
		return result;
	}

}