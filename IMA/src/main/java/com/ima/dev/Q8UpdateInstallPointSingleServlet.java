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
 * Servlet implementation class Q8UpdateInstallPointSingleServlet
 */
public class Q8UpdateInstallPointSingleServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final int WORK_ORDER_STATUS_CANCELLED = 6;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Q8UpdateInstallPointSingleServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub		
		//Logging test
		LoggingPath.setLpath(getServletContext().getRealPath("/logs/logs.txt"));
				
				// First Request
		String meterSN = request.getParameter("hiddenMeterSN");
		String usedWO = request.getParameter("hiddenUsedSN");
		int usedInstallPoint = Integer.parseInt(request.getParameter("hiddenUsedIP"));
		String correctWO = request.getParameter("hiddenCorrectSN");
		int correctInstallPoint = Integer.parseInt(request.getParameter("hiddenInstallPoint"));
		int deviceType = Integer.parseInt(request.getParameter("hiddenDeviceType"));
		String newPlaceID = request.getParameter("hiddenPlaceID");
				
				try {
					DBConnection dbconn = new DBConnection();	
					boolean updateIP = dbconn.updateInstallPoint(meterSN, correctInstallPoint);		
					//check result
					if(updateIP==true)
					{
						//change workorder status - update BAM
						boolean updateWOStatus = dbconn.updateWOStatus(WoPrefix(correctWO.substring(0,3)), Integer.parseInt(correctWO.substring(3,correctWO.length()).trim()));
						boolean updatePOD = dbconn.updatePOD(usedInstallPoint, usedWO);
						dbconn.updateGenericWOStatus(WoPrefix(usedWO.substring(0, 3)), Integer.parseInt(usedWO.substring(3).trim()), WORK_ORDER_STATUS_CANCELLED);						
						//update PCM Device
//						dbconn.updateWaterDeviceSerial_1IP(meterSN, deviceType, newPlaceID);
//						dbconn.updateWaterDeviceSerial_2PRE(meterSN, deviceType, WoPrefix(correctWO.substring(0,3)));
//						dbconn.updateWaterDeviceSerial_3NUM(meterSN, deviceType, Integer.parseInt(correctWO.substring(3,correctWO.length()).trim()));
						dbconn.closeConnection();
						//send to request dispatcher
						RequestDispatcher dispatcher = request.getRequestDispatcher("Q8changeinstallpointsinglewaterresult.jsp");
						dispatcher.forward(request, response);
					} else {
						// error page here
					}
					dbconn.closeConnection();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					System.out.println(e.getMessage());
				}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	public int WoPrefix(String prefix)
	{
		if(prefix.substring(0,3).contains("EXT"))
		{
			return 1;
		}else if(prefix.substring(0,3).contains("PDA"))
		{
			return 2;
		}else if(prefix.substring(0,3).contains("DUP"))
		{
			return 3;
		} else return 0;
	}
}
