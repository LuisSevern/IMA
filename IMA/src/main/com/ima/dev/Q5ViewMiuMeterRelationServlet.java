package main.com.ima.dev;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import main.com.ima.dev.dto.LoggingPath;
import main.com.ima.dev.service.DBConnection;

/**
 * Servlet implementation class Q5ViewMiuMeterRelationServlet
 */

public class Q5ViewMiuMeterRelationServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Q5ViewMiuMeterRelationServlet() {
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
		String miuSerial = request.getParameter("rfid");
		try{		
				DBConnection dbconn = new DBConnection();
				ArrayList miuInfo = dbconn.getRFDetails(miuSerial);
				dbconn.closeConnection();
				if(miuInfo.size() > 0)
				{
					request.setAttribute("miuInfo", miuInfo);
					RequestDispatcher dispatcher = request.getRequestDispatcher("Q5changemiuportverify.jsp");
					dispatcher.forward(request, response);
				}else {
					request.setAttribute("errormsg",
							"Error Message: Could not retrieve information for MIU "
									+ miuSerial + ".");
					RequestDispatcher dispatcher = request
							.getRequestDispatcher("Q5changemiuport.jsp");
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
