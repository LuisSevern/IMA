package main.com.ima.dev;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import main.com.ima.dev.dto.LoggingPath;
import main.com.ima.dev.dto.Q6MWCustomerInfo;
import main.com.ima.dev.service.DBConnection;

/**
 * Servlet implementation class Q6ChangeInstallPointWaterServlet
 */

public class Q6ChangeInstallPointWaterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Q6ChangeInstallPointWaterServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//Logging test
		LoggingPath.setLpath(getServletContext().getRealPath("/logs/logs.txt"));
		RequestDispatcher dispatcher = null;		
				
		//First Request
		int WoRefPrefix = Integer.parseInt(request.getParameter("waterWoPrefix"));
		long WoRefNumber = 0;
		String ignoreSuffix = request.getParameter("noSuffix");
		boolean addSuffix = true;
		if (ignoreSuffix != null && ignoreSuffix.equalsIgnoreCase("on")){
			addSuffix = false;
		}		
		try{
			if(WoRefPrefix == 1) 
			{
				if (addSuffix){
					WoRefNumber = Long.parseLong(request.getParameter("waterWoNumber")+"01");				
				} else {
					WoRefNumber = Long.parseLong(request.getParameter("waterWoNumber"));				
				}
			}else if (WoRefPrefix == 3) 
			{
				if (addSuffix){
					WoRefNumber = Long.parseLong(request.getParameter("waterWoNumber")+"0"+request.getParameter("suffix"));				
				} else {
					WoRefNumber = Long.parseLong(request.getParameter("waterWoNumber"));				
				}
			}	
		} catch(NumberFormatException e){
			request.setAttribute("errormsg","Error Message: The format of the Work Order " + request.getParameter("waterWoNumber") + " is wrong.");
			dispatcher = request.getRequestDispatcher("Q6changeinstallpointwater.jsp");			
		}
		//String MeterSN = request.getParameter("MeterSN");
		
		//Second Request
		int WoRefPrefix2 = Integer.parseInt(request.getParameter("waterWoprefix2"));
		long WoRefNumber2 = 0;
		String ignoreSuffix2 = request.getParameter("noSuffix2");
		boolean addSuffix2 = true;
		if (ignoreSuffix2 != null && ignoreSuffix2.equalsIgnoreCase("on")){
			addSuffix2 = false;
		}		
		try{			
			if(WoRefPrefix2 == 1) 
			{
				if (addSuffix2){
					WoRefNumber2 = Long.parseLong(request.getParameter("waterWoNumber2")+"01");				
				} else {
					WoRefNumber2 = Long.parseLong(request.getParameter("waterWoNumber2"));				
				}
			}else if (WoRefPrefix2 == 3) 
			{
				if (addSuffix2){
					WoRefNumber2 = Long.parseLong(request.getParameter("waterWoNumber2")+"0"+request.getParameter("suffix2"));				
				} else {
					WoRefNumber2 = Long.parseLong(request.getParameter("waterWoNumber2"));				
				}				
			}
		} catch(NumberFormatException e){
			request.setAttribute("errormsg","Error Message: The format of the Work Order " + request.getParameter("waterWoNumber2") + " is wrong.");
			dispatcher = request.getRequestDispatcher("Q6changeinstallpointwater.jsp");						
		}
		//String MeterSN2 = request.getParameter("MeterSN2");

				try {		
					DBConnection dbconn = new DBConnection();
					//first result
					Q6MWCustomerInfo customerInfo = dbconn.returnWaterCustomerInfo(WoRefPrefix,WoRefNumber);
					request.setAttribute("customerInfo", customerInfo);
					//second result
					Q6MWCustomerInfo customerInfo2 = dbconn.returnWaterCustomerInfo(WoRefPrefix2,WoRefNumber2);
					request.setAttribute("customerInfo2", customerInfo2);
					dbconn.closeConnection();
					//initialize dispatcher
					if(customerInfo.getWorkOrderID()!=null && customerInfo2.getWorkOrderID()!=null && !customerInfo.getWorkOrderID().contains(customerInfo2.getWorkOrderID()))
					{
						dispatcher = request.getRequestDispatcher("Q6changeinstallpointwaterverify.jsp");
					}else if(customerInfo.getWorkOrderID()==null && customerInfo2.getWorkOrderID()==null)
					{
						request.setAttribute("errormsg","Error Message: Could not retrieve information for workorder "+WoRefNumber+" and workorder "+WoRefNumber2+".");
						dispatcher = request.getRequestDispatcher("Q6changeinstallpointwater.jsp");
					}else if(customerInfo.getWorkOrderID()==null)
					{
						request.setAttribute("errormsg","Error Message: Could not retrieve information for workorder "+WoRefNumber+".");
						dispatcher = request.getRequestDispatcher("Q6changeinstallpointwater.jsp");
					}else if(customerInfo2.getWorkOrderID()==null)
					{
						request.setAttribute("errormsg","Error Message: Could not retrieve information for workorder "+WoRefNumber2+".");
						dispatcher = request.getRequestDispatcher("Q6changeinstallpointwater.jsp");
					}else if(customerInfo.getWorkOrderID().contains(customerInfo2.getWorkOrderID()))
					{
						request.setAttribute("errormsg","Error Message: The same workorder was entered twice.");
						dispatcher = request.getRequestDispatcher("Q6changeinstallpointwater.jsp");
					}
					dispatcher.forward(request, response);
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
