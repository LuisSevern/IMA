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
 * Servlet implementation class ChangeInstallPointServlet
 */

public class Q1ChangeInstallPointServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//System.out.println("ChangeInstallPointServlet");
		//Logging test
		LoggingPath.setLpath(getServletContext().getRealPath("/logs/logs.txt"));
		System.out.println(LoggingPath.getLpath());
		
		int id1 = Integer.parseInt(request.getParameter("id1"));
		int id2 = Integer.parseInt(request.getParameter("id2"));
		String meterSerialNumber1 = request.getParameter("meterSeralNumber1");
		String meterSerialNumber2 = request.getParameter("meterSeralNumber2");
		String workOrderId1 = request.getParameter("workOrderId1");
		String workOrderId2 = request.getParameter("workOrderId2");

		try {		
			DBConnection dbconn = new DBConnection();
			
			String result = dbconn.changeInstallPoint(id1, id2,
							meterSerialNumber1, workOrderId1,
							meterSerialNumber2, workOrderId2); 
			// Retrieving the updated install points
			int installPoint1 = dbconn.findInstallPointFromAssetId(id1);
			int installPoint2 = dbconn.findInstallPointFromAssetId(id2);
			dbconn.updateInstallPointAndElectricalMeterPartnership(installPoint1, meterSerialNumber1);
			dbconn.updateInstallPointAndElectricalMeterPartnership(installPoint2, meterSerialNumber2);			
			dbconn.updateIntallPointInArguments (installPoint1, workOrderId1.substring(3).trim());
			dbconn.updateIntallPointInArguments (installPoint2, workOrderId2.substring(3).trim());
			dbconn.closeConnection();
			request.setAttribute("result", result);
			if(result.equals("true"))
			{
				RequestDispatcher dispatcher = request.getRequestDispatcher("Q1changeinstallpointresult.jsp");
				dispatcher.forward(request, response);
			}else{
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

}
