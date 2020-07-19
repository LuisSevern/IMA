package main.java.com.ima.dev;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import main.java.com.ima.dev.dto.LoggingPath;
import main.java.com.ima.dev.dto.Q3CustomerInfo;
import main.java.com.ima.dev.service.DBConnection;

/**
 * Servlet implementation class Q4ChangeWaterMIUMeterServlet
 */

public class Q4ChangeWaterMIUMeterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Q4ChangeWaterMIUMeterServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//Logging
		LoggingPath.setLpath(getServletContext().getRealPath("/logs/logs.txt"));

		// First Request
		long woNo=0;
		RequestDispatcher dispatcher = null;		
		int woPre = Integer.parseInt(request.getParameter("waterWoPrefix"));
		try{
			woNo = Long.parseLong(request.getParameter("waterWoNumber"));
		} catch (NumberFormatException e){
			request.setAttribute("errormsg", "Error Message: The format of the Work Order "
					+ request.getParameter("waterWoNumber") + " is wrong.");
			dispatcher = request.getRequestDispatcher("Q3changewmtype.jsp");
		}

		String ignoreSuffix = request.getParameter("noSuffix");
		boolean addSuffix = true;
		if (ignoreSuffix != null && ignoreSuffix.equalsIgnoreCase("on")){
			addSuffix = false;
		}
		
		try {
			DBConnection dbconn = new DBConnection();
			Q3CustomerInfo customerInfo;
			if(woPre == 2)
			{
				customerInfo = dbconn.getWaterCustomerDetailsPDA(woPre,
						woNo);
				dbconn.closeConnection();								
			}else if (woPre == 3)
			{
				if (addSuffix){
					String woSuf = request.getParameter("suffix");
					woNo = Long.parseLong(woNo+"0"+woSuf);
				}				
				
				customerInfo = dbconn.getWaterCustomerDetails(woPre,
					woNo);
				dbconn.closeConnection();								
			}else{
				if (addSuffix){
					woNo = Long.parseLong(woNo+"01");
				}
				
				customerInfo = dbconn.getWaterCustomerDetails(woPre,
					woNo);
				dbconn.closeConnection();
			}
			if (customerInfo.getWorkorderID() != null
					&& customerInfo.getMeterSerial() != null
					&& customerInfo.getRfSerial() != null) {
				if (request.getParameter("changemeter") != null) {
					request.setAttribute("customerInfo", customerInfo);
					dispatcher = request
							.getRequestDispatcher("Q4changewmverify.jsp");
					dispatcher.forward(request, response);
				} else if (request.getParameter("changemiu") != null) {
					// changemiu button is clicked
					request.setAttribute("customerInfo", customerInfo);
					dispatcher = request
							.getRequestDispatcher("Q4changemiuverify.jsp");
					dispatcher.forward(request, response);
				}
			}else {
					request.setAttribute("errormsg",
							"Error Message: Could not retrieve information for workorder "
									+ woNo + ".");
					dispatcher = request
							.getRequestDispatcher("Q4changewmmiu.jsp");
					dispatcher.forward(request, response);
				}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
