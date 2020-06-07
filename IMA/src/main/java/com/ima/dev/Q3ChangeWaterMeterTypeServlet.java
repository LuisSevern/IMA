package main.java.com.ima.dev;

import java.io.IOException;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import main.java.com.ima.dev.dto.LoggingPath;
import main.java.com.ima.dev.dto.Q3CustomerInfo;
import main.java.com.ima.dev.service.DBConnection;

/**
 * Servlet implementation class Q3ChangeWaterMeterType
 */

public class Q3ChangeWaterMeterTypeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Q3ChangeWaterMeterTypeServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//Logging
		LoggingPath.setLpath(getServletContext().getRealPath("/logs/logs.txt"));

		//First Request
		int woPre = Integer.parseInt(request.getParameter("waterWoPrefix"));
		long woNo = 0;
		RequestDispatcher dispatcher = null;		
		try{
			woNo = Long.parseLong(request.getParameter("waterWoNumber"));
		} catch (NumberFormatException e) {
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
			}else if (woPre == 3)
			{	
				if (addSuffix){
					String woSuf = request.getParameter("suffix");
					woNo = Long.parseLong(woNo+"0"+woSuf);
				}				
				customerInfo = dbconn.getWaterCustomerDetails(woPre,
					woNo);
			}else
			{
				if (addSuffix){
					woNo = Long.parseLong(woNo+"01");
				}
					
				customerInfo = dbconn.getWaterCustomerDetails(woPre,
					woNo);
			}
			if(customerInfo.getWorkorderID()!=null && customerInfo.getMeterSerial()!=null && customerInfo.getRfSerial()!=null)
			{
				if (request.getParameter("changemeter") != null) {
					//changemeter button is clicked
					Map meterTypes = dbconn.getWaterMeterTypes();
					//creating application variable
					ServletContext sc = getServletContext();
					sc.setAttribute("meterTypes", meterTypes);
					request.setAttribute("customerInfo", customerInfo);
					dbconn.closeConnection();					
					dispatcher = request.getRequestDispatcher("Q3changewmtypeverify.jsp");
					dispatcher.forward(request, response);
				}else if (request.getParameter("changemiu") != null) {
					//changemiu button is clicked
					Map miuTypes = dbconn.getOndeoMiuTypes();
					ServletContext sc = getServletContext();
					sc.setAttribute("miuTypes", miuTypes);
					if (customerInfo.getRfTypeID() == 15){
						// If the type of the module is a Tx1,
						// supposedly the type to change will be Tx4
						// because it will be correct type of module
						String[] waterMeters = dbconn.getMetersSerialsAssociatedFromModuleSerial(customerInfo.getRfSerial());
						customerInfo.setWaterMetersAssociated(waterMeters);
					}
					request.setAttribute("customerInfo", customerInfo);
					dbconn.closeConnection();					
					dispatcher = request.getRequestDispatcher("Q3changemiutypeverify.jsp");
					dispatcher.forward(request, response);
				}
			}else
			{
				request.setAttribute("errormsg","Error Message: Could not retrieve information for workorder "+woNo+".");
				dbconn.closeConnection();				
				dispatcher = request.getRequestDispatcher("Q3changewmtype.jsp");
				dispatcher.forward(request, response);
			}
		}catch (Exception e) {
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
