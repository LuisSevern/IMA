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
 * Servlet implementation class Q3UpdateMiuModuleTypeServlet
 */

public class Q3UpdateMiuModuleTypeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Q3UpdateMiuModuleTypeServlet() {
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
		
		String miuSerial = request.getParameter("hiddenrfSerial");
		String oldMiuTypeName = request.getParameter("hiddenoldrfName");
		int oldMiuTypeID = Integer.parseInt(request.getParameter("hiddenrfTypeID"));
		//fix 01
		long woId = Long.parseLong(request.getParameter("hiddenwoID"));
		String workOrderID = request.getParameter("hiddenworkorderid");
		int devrelid = Integer.parseInt(request.getParameter("hiddendevrelid"));
		
		Map newMIUTypes = (Map) getServletContext().getAttribute("miuTypes");
		int newMIUTypeID = Integer.parseInt(request.getParameter("mius"));
		String newMIUTypeName = (String) newMIUTypes.get(new Integer(newMIUTypeID));
		
		String meterSerialNumber = (String) request.getParameter("hiddenMeterSerialNumber");
		int meterTypeId  = Integer.parseInt((String)request.getParameter("hiddenMeterTypeId"));
		String[] vPortSelected = {"", "", "", ""};
		if (request.getParameter("portName_1") != null){
			vPortSelected[0] = request.getParameter("portName_1");
		}
		if (request.getParameter("portName_2") != null){
			vPortSelected[1] = request.getParameter("portName_2");
		}
		if (request.getParameter("portName_3") != null){
			vPortSelected[2] = request.getParameter("portName_3");
		}
		if (request.getParameter("portName_4") != null){
			vPortSelected[3] = request.getParameter("portName_4");
		}
		System.err.println("\nPerforming the change type of the MIU operation with the next data: " + "\nmiuSerial: " +  miuSerial 
				+ "\noldMiuTypeName: " + oldMiuTypeName + "\noldMiuTypeID: " + oldMiuTypeName + "\nwoId: " + woId + "\nworkOrderID: " + workOrderID
				+ "\ndevrelid: " + devrelid + "\nnewMIUTypes: " + newMIUTypes + "\nnewMIUTypeID" + newMIUTypeID + "\nnewMIUTypeName" + newMIUTypeName 
				+ "\nmeterSerialNumber: " + meterSerialNumber + "\nmeterTypeId: " + meterTypeId + "\n");
		
		try {
			int chosenPort=0;
			boolean updateWaterMiu = true, moduleNew = false;
			DBConnection dbconn = new DBConnection();
			if (newMIUTypeID==16){
				boolean exit = false;
				for (chosenPort=0; chosenPort<vPortSelected.length && !exit; chosenPort++){
					if (vPortSelected[chosenPort].equals("-1")){
						exit = true;
					}
				}
				if (!exit){
					chosenPort=0;
				}
			}

			int amountOfModules = dbconn.countExistingMeter(miuSerial);
			if (amountOfModules >= 2){
				// The module already existed in the system. 
				// In a later meter installation in the module the type of the module was provided incorrectly.
				// So there are more than one entry for the module in pcm.device.
				// The correct id of the module is retrieved
				long correctIdModule = dbconn.getDeviceId(miuSerial, newMIUTypeID);
				// The wrong id of the module is retrieved
				long wrongIdModule = dbconn.getDeviceId(miuSerial, oldMiuTypeID);
				if (correctIdModule != -1 && wrongIdModule != -1){
					// Update the relation between the correct module and the meters
					dbconn.updateModuleMeterRelation(correctIdModule, wrongIdModule);
				}
				// Delete the entry for the wrong couple Module SN/wrong module type
				updateWaterMiu = dbconn.deleteDevice(miuSerial, oldMiuTypeID);	
				dbconn.deleteWaterMIUAsset(miuSerial, oldMiuTypeName);
			}else{
				// The module did not exist in the system.
				updateWaterMiu = dbconn.updateWaterMIUDevice(oldMiuTypeID, newMIUTypeID, miuSerial);
				//update Water Asset
				boolean updateWaterAsset = dbconn.updateWaterMIUAsset(newMIUTypeName, oldMiuTypeName, miuSerial);				
				moduleNew = true;
			}
			request.setAttribute("updateWaterMiu", new Boolean (updateWaterMiu));
			//check result
			if(updateWaterMiu==true)
			{
				//check wo prefix for type of workorder
				if(WoPrefix(workOrderID)==2){
					boolean updateWaterMIUModulePDA = dbconn.updateWaterMIUWODevicePDA(newMIUTypeName, woId);
				}
				else{
					boolean updateWaterMIUModule = dbconn.updateWaterMIUWODevice(newMIUTypeName, woId); 
				}
				//if tx1 change port to 1
				if(newMIUTypeName.contains("Ondeo.Transmitter-Tx1") || moduleNew)
				{
					if (chosenPort != 1 && chosenPort != -1 && chosenPort != 0){
						boolean updatePort = dbconn.updateRFMeterPort(Integer.toString(chosenPort), devrelid);
						boolean updatePortWO = dbconn.updateRFMeterPortArgs(Integer.toString(chosenPort), woId);
					} else {
						boolean updatePort = dbconn.updateRFMeterPort("1", devrelid);
						boolean updatePortWO = dbconn.updateRFMeterPortArgs("1", woId);
					}
				} else {
					// Updating the port number of the meter in the TX4 module
					dbconn.updateRFMeterPort(Integer.toString(chosenPort), devrelid);
				}
				// Updating the data related with the relation
				// this method is not neccessary since the relationship between the incorrect module and the meter is reused
				// so the identifier of the relationship does not change. dbconn.updateWrongDataOfDevice();
				
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
