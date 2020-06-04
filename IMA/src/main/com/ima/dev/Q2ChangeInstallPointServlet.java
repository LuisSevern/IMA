package main.com.ima.dev;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import main.com.ima.dev.dto.LoggingPath;
import main.com.ima.dev.dto.Q2CustomerInfo;
import main.com.ima.dev.service.DBConnection;

/**
 * Servlet implementation class Q2ChangeInstallPointServlet
 */

public class Q2ChangeInstallPointServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Q2ChangeInstallPointServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//System.out.println("Hello from get method");
		//Logging test
		LoggingPath.setLpath(getServletContext().getRealPath("/logs/logs.txt"));
		RequestDispatcher dispatcher = null;
		long usedWoRefNumber = 0, actualWoRefNumber = 0;
		String usedWoID = "";
		
		// Error Control in AMM IE. Begin
		//First Request
		int usedWoRefPrefix = Integer.parseInt(request.getParameter("usedWoRefPrefix"));			
		String ignoreSuffix = request.getParameter("noSuffix");
		boolean addSuffix = true;
		if (ignoreSuffix != null && ignoreSuffix.equalsIgnoreCase("on")){
			addSuffix = false;
		}
		try{
			if (addSuffix){
				if(usedWoRefPrefix == 1) 
				{
					usedWoRefNumber = Long.parseLong(request.getParameter("usedWoRefNumber")+"01");				
				}else if (usedWoRefPrefix == 3) 
				{
					usedWoRefNumber = Long.parseLong(request.getParameter("usedWoRefNumber")+"0"+request.getParameter("suffix"));				
				}
			} else {
				usedWoRefNumber = Long.parseLong(request.getParameter("usedWoRefNumber"));
			}
			usedWoID = Long.toString(usedWoRefPrefix).concat(Long.toString(usedWoRefNumber));
		} catch(NumberFormatException e){
			request.setAttribute("errormsg", "Error Message: The format of the Work Order " + request.getParameter("usedWoRefNumber") + " is wrong.");
			dispatcher = request.getRequestDispatcher("Q2changeinstallpointsingle.jsp");
		}				
		
		//Second Request
		int actualWoRefPrefix = Integer.parseInt(request.getParameter("actualWoRefPrefix"));
		String actualWoID = "";
		String ignoreSuffix2 = request.getParameter("noSuffix2");
		boolean addSuffix2 = true;
		if (ignoreSuffix2 != null && ignoreSuffix2.equalsIgnoreCase("on")){
			addSuffix2 = false;
		}
		try{
			if (addSuffix2){
				if(actualWoRefPrefix == 1) {
					actualWoRefNumber = Long.parseLong(request.getParameter("actualWoRefNumber")+"01");				
				} else if (actualWoRefPrefix == 3) {
					actualWoRefNumber = Long.parseLong(request.getParameter("actualWoRefNumber")+"0"+request.getParameter("suffix2"));				
				}
			} else {
				actualWoRefNumber = Long.parseLong(request.getParameter("actualWoRefNumber"));
			}
			actualWoID = Long.toString(actualWoRefPrefix).concat(Long.toString(actualWoRefNumber));
		} catch (NumberFormatException e){
			request.setAttribute("errormsg", "Error Message: The format of the Work Order " + request.getParameter("actualWoRefNumber") + " is wrong.");
			dispatcher = request.getRequestDispatcher("Q2changeinstallpointsingle.jsp");
		}
		// Error Control in AMM IE. End
		
		//Meter Serial number
		String meterSN = request.getParameter("meterSN");

		try {		
			DBConnection dbconn = new DBConnection();
			int resultQry1 = dbconn.countWoAndMeter(usedWoRefPrefix, usedWoRefNumber, meterSN);
			int resultQry2 = dbconn.checkWoExistsAPDAElectricity(actualWoRefPrefix, actualWoRefNumber);
			//we have to save the install point somewhere
			request.setAttribute("actualInstallPoint", new Integer(resultQry2));
			//make a check that workorders given are not the same
			if (resultQry1 == 1 && resultQry2 != 0 && !usedWoID.equals(actualWoID)){
				Q2CustomerInfo customerInfo = dbconn.returnCustomerInfo(usedWoRefPrefix, usedWoRefNumber);
				request.setAttribute("customerInfo", customerInfo);
				Q2CustomerInfo customerInfo2 = dbconn.returnCustomerInfo(actualWoRefPrefix, actualWoRefNumber);
				dbconn.closeConnection();
				request.setAttribute("customerInfo2", customerInfo2);
				dispatcher = request.getRequestDispatcher("Q2changeinstallpointverify.jsp");
				dispatcher.forward(request, response);
			} else if(resultQry1 != 1){
				request.setAttribute("errormsg","Error Message: No match found for Executed Workorder "+usedWoRefNumber+" and Meter "+meterSN+".");
				dispatcher = request.getRequestDispatcher("Q2changeinstallpointsingle.jsp");
				dispatcher.forward(request, response);
			} else if(resultQry2 == 0){
				request.setAttribute("errormsg","Error Message: Workorder "+actualWoRefNumber+" is not in APDA/INIT status or does not exist.");
				dispatcher = request.getRequestDispatcher("Q2changeinstallpointsingle.jsp");
				dispatcher.forward(request, response);
			} else if(usedWoID.equals(actualWoID)){
				request.setAttribute("errormsg","Error Message: Workorder "+actualWoRefNumber+" is set for both Executed Workorder and Actual Workorder");
				dispatcher = request.getRequestDispatcher("Q2changeinstallpointsingle.jsp");
				dispatcher.forward(request, response);
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
