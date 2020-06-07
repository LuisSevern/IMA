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
 * Servlet implementation class Q6UpdateInstallPointWaterServlet
 */

public class Q6UpdateInstallPointWaterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Q6UpdateInstallPointWaterServlet() {
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
		//Asset ID
		int id1 = Integer.parseInt(request.getParameter("id1"));
		int id2 = Integer.parseInt(request.getParameter("id2"));
		//Workorder ID
		String wo1 = request.getParameter("wo1");
		String wo2 = request.getParameter("wo2");
		//DataType
		int dt1 = Integer.parseInt(request.getParameter("dt1"));
		int dt2 = Integer.parseInt(request.getParameter("dt2"));
		//PlaceID
		String pl1 = request.getParameter("pl1");
		String pl2 = request.getParameter("pl2");
		//Meter Serial
		String ms1 = request.getParameter("ms1");
		String ms2 = request.getParameter("ms2");

		try {		
			DBConnection dbconn = new DBConnection();
			String result = dbconn.changeInstallPoint(id1, id2, ms1, wo1, ms2, wo2);
			request.setAttribute("result", result);
			if(result.equals("true"))
			{
				// Retrieving the updated install points
				int installPoint1 = dbconn.findInstallPointFromAssetId(id1);
				int installPoint2 = dbconn.findInstallPointFromAssetId(id2);
				dbconn.updateInstallPointAndWaterMeterPartnership(installPoint1, ms1);
				dbconn.updateInstallPointAndWaterMeterPartnership(installPoint2, ms2);			
				dbconn.updateIntallPointInArguments (installPoint1, wo1.substring(3).trim());
				dbconn.updateIntallPointInArguments (installPoint2, wo2.substring(3).trim());
				
				//Temporary Change Value
				//dbconn.updateWaterDeviceSerial("TMLVAL", pl2, WoPrefix(wo2.substring(0,3)), Integer.parseInt(wo2.substring(3).trim()));
				//Change Meter & Type for WO1
//				dbconn.updateWaterDeviceSerial_1IP(ms1, dt1, pl2);
//				dbconn.updateWaterDeviceSerial_2PRE(ms1, dt1, WoPrefix(wo2.substring(0,3)));
//				dbconn.updateWaterDeviceSerial_3NUM(ms1, dt1, Integer.parseInt(wo2.substring(3).trim()));
				//dbconn.updateWaterDeviceSerial(ms2, pl1, WoPrefix(wo1.substring(0,3)), Integer.parseInt(wo1.substring(3).trim()));
				//dbconn.updateWaterDeviceType(dt2, pl1, WoPrefix(wo1.substring(0,3)), Integer.parseInt(wo1.substring(3).trim()));
				//Change Meter & Type for WO2
//				dbconn.updateWaterDeviceSerial_1IP(ms2, dt2, pl1);
//				dbconn.updateWaterDeviceSerial_2PRE(ms2, dt2, WoPrefix(wo1.substring(0,3)));
//				dbconn.updateWaterDeviceSerial_3NUM(ms2, dt2, Integer.parseInt(wo1.substring(3).trim()));
				//dbconn.updateWaterDeviceSerial(ms1, pl2, WoPrefix(wo2.substring(0,3)), Integer.parseInt(wo2.substring(3).trim()));
				//dbconn.updateWaterDeviceType(dt1, pl2, WoPrefix(wo2.substring(0,3)), Integer.parseInt(wo2.substring(3).trim()));
				dbconn.closeConnection();
				//Request Dispatcher
				RequestDispatcher dispatcher = request.getRequestDispatcher("Q6changeinstallpointwaterresult.jsp");
				dispatcher.forward(request, response);
			}else{
				dbconn.closeConnection();
				RequestDispatcher dispatcher = request.getRequestDispatcher("error.jsp");
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
