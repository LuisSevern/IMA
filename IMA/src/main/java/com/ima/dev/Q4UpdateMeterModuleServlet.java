package main.java.com.ima.dev;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import main.java.com.ima.dev.dto.LoggingPath;
import main.java.com.ima.dev.service.DBConnection;

/**
 * Servlet implementation class Q4UpdateMeterModuleServlet
 */

public class Q4UpdateMeterModuleServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Q4UpdateMeterModuleServlet() {
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
		
		String oldMeterSerial = request.getParameter("hiddenOldWmSerial");
		String newMeterSerial = request.getParameter("changedWMSerial");
		String placeID = request.getParameter("hiddenPlaceID");
		int woID = Integer.parseInt(request.getParameter("hiddenWoID"));
		DBConnection dbconn = new DBConnection();
		try {
			
			int meterExists = dbconn.countExistingMeter(newMeterSerial);
			if (meterExists!=0) {
				// error page here
				request.setAttribute("result", "1. Error updating meter.  Not Allowed - Meter Number already exists in AMM.");
				dbconn.closeConnection();
				RequestDispatcher dispatcher = request.getRequestDispatcher("error.jsp");
				dispatcher.forward(request, response);
			} else 
			{
				boolean updateWaterMeter = dbconn.updateWaterMeterDeviceSN(newMeterSerial, oldMeterSerial, placeID);		
				request.setAttribute("updateWaterMeter", new Boolean(updateWaterMeter));
				//check result
				if(updateWaterMeter==true)
				{
					boolean updateWaterAsset = dbconn.updateWMSerialAsset(dbconn.getInstallPointFromInstallPlace(placeID),newMeterSerial,oldMeterSerial);
					boolean updateWaterDevice = dbconn.updateWoDevWM(woID, oldMeterSerial, newMeterSerial);
					boolean updateWaterMeterWO = dbconn.updateWaterWODeviceSerial(newMeterSerial, woID);
					boolean updateWaterMeterArgs = dbconn.updateWaterWODeviceSerialArgs(newMeterSerial, woID);
					boolean updateInstPointWaterMeter = dbconn.updateInstallPointWaterMeterBySerial(dbconn.getInstallPointFromInstallPlace(placeID),oldMeterSerial,newMeterSerial);
					dbconn.closeConnection();				
					RequestDispatcher dispatcher = request.getRequestDispatcher("Q3changewmtyperesult.jsp");
					dispatcher.forward(request, response);
				} else {
					// error page here
					request.setAttribute("result", "2. Error updating meter.  Not Allowed - Meter Number already exists in AMM.");
					dbconn.closeConnection();
					RequestDispatcher dispatcher = request.getRequestDispatcher("error.jsp");
					dispatcher.forward(request, response);
				}
					
			}
		} catch (Exception e) {
			// error page here
			request.setAttribute("result", "Database: Error updating meter. ");
			dbconn.closeConnection();
			RequestDispatcher dispatcher = request.getRequestDispatcher("error.jsp");
			dispatcher.forward(request, response);
		} 
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
