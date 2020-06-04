package main.com.ima.dev;

import java.io.IOException;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import main.com.ima.dev.dto.LoggingPath;
import main.com.ima.dev.service.DBConnection;

/**
 * Servlet implementation class Q4UpdateMiuModuleServlet
 */

public class Q4UpdateMiuModuleServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Q4UpdateMiuModuleServlet() {
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
				
				String previousMIUSerial = request.getParameter("hiddenOldRFSerial");
				String changedMIUSerial = request.getParameter("changedRFSerial");
				String miuType = request.getParameter("hiddenRFTypeName");
				int miuTypeID = Integer.parseInt(request.getParameter("hiddenRFTypeID"));
				long WOID = Long.parseLong(request.getParameter("hiddenWoID"));
				//fix02
				String workOrderID = request.getParameter("hiddenworkorderid");
				DBConnection dbconn = new DBConnection();
				try {
					 
					int miuExists = dbconn.countExistingMeter(changedMIUSerial);
					if ( miuExists!=0) { //CQ 4647.
						// error page here
						request.setAttribute("result", "1.Error updating miu.  Not Allowed - MIU Number already exists.  Perform a MMWO to change MIU.");
						dbconn.closeConnection();
						RequestDispatcher dispatcher = request.getRequestDispatcher("error.jsp");
						dispatcher.forward(request, response);
					} else {
						boolean updateWaterMiu = dbconn.updateWaterMIUSerialDevice(previousMIUSerial,changedMIUSerial, miuTypeID);	
						request.setAttribute("updateWaterMiu", new Boolean(updateWaterMiu));					
						//check result
						if(updateWaterMiu==true) {
							boolean updateWaterAsset = dbconn.updateWaterMIUSerialAsset(previousMIUSerial,changedMIUSerial,miuType);
							boolean updateWoDevMiu = dbconn.updateWoDevMIU(WOID, previousMIUSerial, changedMIUSerial);
							boolean updateWaterMiuArgs = dbconn.updateRFMeterArgs(changedMIUSerial, WOID);
							if(WoPrefix(workOrderID)==2)
							{
								boolean updateWaterMIUModulePDA = dbconn.updateWaterMIUSerialWODevicePDA(changedMIUSerial, WOID);
							}
							else
							{
								boolean updateWaterMIUModule = dbconn.updateWaterMIUSerialWODevice(changedMIUSerial, WOID);
							}
							dbconn.closeConnection();
							RequestDispatcher dispatcher = request.getRequestDispatcher("Q3changewmtyperesult.jsp");
							dispatcher.forward(request, response);
						} else {
							request.setAttribute("result", "2.Error updating miu.  Not Allowed - MIU Number already exists.  Perform a MMWO to change MIU.");
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
