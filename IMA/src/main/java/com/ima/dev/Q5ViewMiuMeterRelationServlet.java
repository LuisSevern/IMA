package main.java.com.ima.dev;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import main.java.com.ima.dev.service.DBConnection;

/**
 * Servlet implementation class Q5ViewMiuMeterRelationServlet
 */

public class Q5ViewMiuMeterRelationServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger =  LogManager.getLogger(Q5ViewMiuMeterRelationServlet.class);       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Q5ViewMiuMeterRelationServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		logger.info("Chagen Miu Port. Init ->");
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
			logger.error("Change Miu Port. Exception", e);
		}
		logger.info("Chagen Miu Port. End <-");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
