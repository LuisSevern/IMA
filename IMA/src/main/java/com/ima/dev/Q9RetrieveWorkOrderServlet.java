package main.java.com.ima.dev;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import main.java.com.ima.dev.dto.LoggingPath;
import main.java.com.ima.dev.dto.Q9DeviceInfo;
import main.java.com.ima.dev.service.DBConnection;

/**
 * Servlet implementation class Q9RetrieveWorkOrderServlet
 */
public class Q9RetrieveWorkOrderServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Q9RetrieveWorkOrderServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		LoggingPath.setLpath(getServletContext().getRealPath("/logs/logs.txt"));
		RequestDispatcher dispatcher = null;		
		String deviceSerialNumber = request.getParameter("deviceSerialNumber");
		
		try {
			DBConnection dbconn = new DBConnection();	
			Q9DeviceInfo device = dbconn.deviceWorkOrderInfo(deviceSerialNumber);
			request.setAttribute("device", device);
				
			dbconn.closeConnection();
			
			if(device.getSerial()!=null){
				dispatcher = request.getRequestDispatcher("Q9retrieveworkorderresult.jsp");
			}else{
				request.setAttribute("errormsg","Error Message: Could not retrieve information for device "+deviceSerialNumber+".");
				dispatcher = request.getRequestDispatcher("Q9retrieveworkorder.jsp");
			}
			
			
			dispatcher.forward(request, response);

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

}
