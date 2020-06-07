package main.java.com.ima.dev;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import main.java.com.ima.dev.dto.LoggingPath;
import main.java.com.ima.dev.dto.Q1Premise;
import main.java.com.ima.dev.service.DBConnection;

/**
 * Servlet implementation class SimpleServlet
 */

public class Q1PremiseDetailsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// System.out.println("Hello from get method");
		// System.out.println(getServletContext().getRealPath("/logs/logs.txt"));
		// PrintWriter writer = response.getWriter();
		// writer.println("<h3>Hello in HTML</h3>");

		// Logging test
		LoggingPath.setLpath(getServletContext().getRealPath("/logs/logs.txt"));
		System.out.println(LoggingPath.getLpath());
		int WoRefPrefix = 0, WoRefPrefix2 = 0;
		// Error Control in AMM IE. Begin
		// The maximun value of the int type is 2147483647, maximun value of the
		// field WOP.WORK_ORDER.REFERENCENUMBER could be 9999999999.
		// Change of type from int to long
		long WoRefNumber = 0, WoRefNumber2 = 0;
		boolean errorFound = false;
		// Error Control in AMM IE. End
		RequestDispatcher dispatcher = null;
		
		String ignoreSuffix = request.getParameter("noSuffix");
		boolean addSuffix = true;
		if (ignoreSuffix != null && ignoreSuffix.equalsIgnoreCase("on")){
			addSuffix = false;
		}
		try {
			// Error Control in AMM IE. Begin
			// Possible exceptions in the conversion of the data are catched.
			// First Request
			try {
				WoRefPrefix = Integer.parseInt(request.getParameter("woRefPrefix"));
				
				String wo = request.getParameter("woRefNumber");				
				if (addSuffix){
					if(WoRefPrefix == 1) 
					{
						WoRefNumber = Long.parseLong(wo + "01");				
					}else if (WoRefPrefix == 3) 
					{
						WoRefNumber = Long.parseLong(wo+"0"+request.getParameter("suffix"));				
					}			
				} else {
					WoRefNumber = Long.parseLong(wo);
				}
			} catch (NumberFormatException e) {
				request.setAttribute("errormsg",
						"Error Message: The format of the Work Order "
								+ request.getParameter("woRefNumber")
								+ " is wrong.");
				dispatcher = request
						.getRequestDispatcher("Q1changeinstallpoint.jsp");
			}

			String MeterSN = request.getParameter("MeterSN");

			// Second Request
			String ignoreSuffix2 = request.getParameter("noSuffix2");
			boolean addSuffix2 = true;
			if (ignoreSuffix2 != null && ignoreSuffix2.equalsIgnoreCase("on")){
				addSuffix2 = false;
			}
			try {
				WoRefPrefix2 = Integer.parseInt(request.getParameter("woRefPrefix2"));
				
				String wo2 = request.getParameter("woRefNumber2");
				if (addSuffix2){
					if(WoRefPrefix2 == 1) 
					{
						WoRefNumber2 = Long.parseLong(wo2+"01");				
					}else if (WoRefPrefix2 == 3)
					{
						WoRefNumber2 = Long.parseLong(wo2+"0"+request.getParameter("suffix2"));				
					}			
				} else {
					WoRefNumber2 = Long.parseLong(wo2);
				}
			} catch (NumberFormatException e) {
				request.setAttribute("errormsg",
						"Error Message: The format of the Work Order "
								+ request.getParameter("woRefPrefix2")
								+ " is wrong.");
				dispatcher = request
						.getRequestDispatcher("Q1changeinstallpoint.jsp");
			}
			// Error Control in AMM IE. End

			String MeterSN2 = request.getParameter("MeterSN2");

			DBConnection dbconn = new DBConnection();

//			 System.err.println("IN SERVLET");
//			 String neuronid1 = dbconn.testDataBase();
//			 System.err.println("RETRIEVED VALUE: " + neuronid1);
//			 dbconn.closeConnection();
//			 dbconn.getNewConnection();
//			 String neuronid2 = dbconn.testDataBase();
//			 dbconn.closeConnection();
//			 dbconn.getNewConnection();
//			 String neuronid3 = dbconn.testDataBase();
//			 dbconn.closeConnection();			 
//			 String neuronid = neuronid1 + " " + neuronid2 + " " + neuronid3;
//			 System.err.println("STRING TO SHOWING: " + neuronid);
//			 request.setAttribute("meterId", neuronid);
//			 RequestDispatcher view =
//			 request.getRequestDispatcher("showMeterId.jsp");
//			 view.forward(request, response);

			// Error Control in AMM IE. Begin
			// Change in the methods invocation
			// first result
			Q1Premise premiseInfo = dbconn.returnPremiseInfo(WoRefPrefix,
					WoRefNumber, MeterSN);
			request.setAttribute("premiseInfo", premiseInfo);
			// second result
			Q1Premise premiseInfo2 = dbconn.returnPremiseInfo(WoRefPrefix2,
					WoRefNumber2, MeterSN2);
			// Error Control in AMM IE. End
			// Error Control in AMM IE. Begin
			// Closing the DB connection
			dbconn.closeConnection();
			// Error Control in AMM IE. End
			request.setAttribute("premiseInfo2", premiseInfo2);
			// initializing dispatcher
			if (premiseInfo.getWORK_ORDER_ID() != null
				&& premiseInfo2.getWORK_ORDER_ID() != null
				&& premiseInfo.getWORK_ORDER_ID() != premiseInfo2.getWORK_ORDER_ID()) {
				dispatcher = request.getRequestDispatcher("Q1changeinstallpointverify.jsp");
			} else if (premiseInfo.getWORK_ORDER_ID() == null) {
				request.setAttribute("errormsg",
						"Error Message: Could not retrieve information for workorder "
								+ WoRefNumber + ".");
				errorFound = true;
				dispatcher = request.getRequestDispatcher("Q1changeinstallpoint.jsp");
			} else if ((!errorFound) && (premiseInfo2.getWORK_ORDER_ID() == null)) {
				request.setAttribute("errormsg",
						"Error Message: Could not retrieve information for workorder "
								+ WoRefNumber2 + ".");
				errorFound = true;
				dispatcher = request.getRequestDispatcher("Q1changeinstallpoint.jsp");
			} else if ((!errorFound) && (premiseInfo.getWORK_ORDER_ID().contains(premiseInfo2.getWORK_ORDER_ID()))){
				request.setAttribute("errormsg", "Error Message: The same workorder was entered twice.");
				dispatcher = request.getRequestDispatcher("Q1changeinstallpoint.jsp");
			}
			dispatcher.forward(request, response);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}

}
