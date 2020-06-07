package main.java.com.ima.dev;

import java.io.IOException;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import main.java.com.ima.dev.dto.LoggingPath;
import main.java.com.ima.dev.service.DBConnection;


/**
 * Servlet implementation class Q3UpdateWaterMeterType
 */

public class Q3UpdateWaterMeterType extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Q3UpdateWaterMeterType() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//Logging
		LoggingPath.setLpath(getServletContext().getRealPath("/logs/logs.txt"));
		
		String meterSerial = request.getParameter("hiddenmeterSerial");
		String placeID = request.getParameter("hiddenPlaceID");
		String oldMeterTypeName = request.getParameter("hiddenOldMeterType");
		int oldMeterTypeID = Integer.parseInt(request.getParameter("hiddenOldMeterTypeID"));
		//fix 01
		int woId = Integer.parseInt(request.getParameter("hiddenwoID"));
		
		Map newMeterTypes = (Map) getServletContext().getAttribute("meterTypes");
		int newMeterTypeID = Integer.parseInt(request.getParameter("meters"));
		String newMeterTypeName = (String) newMeterTypes.get(new Integer(newMeterTypeID));
		
		//System.out.println(newMeterTypeName);

		try {
			DBConnection dbconn = new DBConnection();	
			boolean updateWaterMeter = dbconn.updateWaterDevice(oldMeterTypeID, newMeterTypeID, meterSerial, placeID);		
			request.setAttribute("updateWaterMeter", new Boolean(updateWaterMeter));
			//check result
			if(updateWaterMeter==true)
			{
				boolean updateWaterAsset = dbconn.updateWaterAsset(dbconn.getInstallPointFromInstallPlace(placeID), newMeterTypeName, oldMeterTypeName, meterSerial);
				boolean updateWaterAsset2 = dbconn.updateWaterAsset2(dbconn.getInstallPointFromInstallPlace(placeID), newMeterTypeName.substring(0,newMeterTypeName.indexOf(".")), meterSerial);
				boolean updateWaterMeterWO = dbconn.updateWaterWODevice(newMeterTypeName, woId);
				boolean updateWaterMeterTypename = dbconn.updateInstallPointWaterTypename(newMeterTypeName, meterSerial);
				dbconn.closeConnection();				
				RequestDispatcher dispatcher = request.getRequestDispatcher("Q3changewmtyperesult.jsp");
				dispatcher.forward(request, response);
			} else {
				// error page here
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
