package main.java.com.ima.dev;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import main.java.com.ima.dev.dto.LoggingPath;
import main.java.com.ima.dev.dto.Q6MWCustomerInfo;
import main.java.com.ima.dev.service.DBConnection;

/**
 * Servlet implementation class Q8ChangeInstallPointSingleServlet
 */
public class Q8ChangeInstallPointSingleServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Q8ChangeInstallPointSingleServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		// Logging test
		LoggingPath.setLpath(getServletContext().getRealPath("/logs/logs.txt"));
		// initializing dispatcher
		RequestDispatcher dispatcher = null;

		// First Request
		int WoRefPrefix = Integer.parseInt(request.getParameter("usedWoRefPrefix"));
		long WoRefNumber = 0;
		String ignoreSuffix = request.getParameter("noSuffix");
		boolean addSuffix = true;
		if (ignoreSuffix != null && ignoreSuffix.equalsIgnoreCase("on")) {
			addSuffix = false;
		}
		try {
			if (WoRefPrefix == 1) {
				if (addSuffix) {
					WoRefNumber = Long.parseLong(request.getParameter("usedWoRefNumber") + "01");
				} else {
					WoRefNumber = Long.parseLong(request.getParameter("usedWoRefNumber"));
				}
			} else if (WoRefPrefix == 3) {
				if (addSuffix) {
					WoRefNumber = Long
							.parseLong(request.getParameter("usedWoRefNumber") + "0" + request.getParameter("suffix"));
				} else {
					WoRefNumber = Long.parseLong(request.getParameter("usedWoRefNumber"));
				}
			}
		} catch (NumberFormatException e) {
			request.setAttribute("errormsg", "Error Message: The format of the Work Order "
					+ request.getParameter("usedWoRefNumber") + " is wrong.");
			dispatcher = request.getRequestDispatcher("Q8changeinstallpointsinglewater.jsp");
		}

		// Second Request
		int WoRefPrefix2 = Integer.parseInt(request.getParameter("actualWoRefPrefix"));
		long WoRefNumber2 = 0;
		String ignoreSuffix2 = request.getParameter("noSuffix2");
		boolean addSuffix2 = true;
		if (ignoreSuffix2 != null && ignoreSuffix2.equalsIgnoreCase("on")) {
			addSuffix2 = false;
		}
		try {
			if (WoRefPrefix2 == 1) {
				if (addSuffix2) {
					WoRefNumber2 = Long.parseLong(request.getParameter("actualWoRefNumber") + "01");
				} else {
					WoRefNumber2 = Long.parseLong(request.getParameter("actualWoRefNumber"));
				}
			} else if (WoRefPrefix2 == 3) {
				if (addSuffix2) {
					WoRefNumber2 = Long.parseLong(
							request.getParameter("actualWoRefNumber") + "0" + request.getParameter("suffix2"));
				} else {
					WoRefNumber2 = Long.parseLong(request.getParameter("actualWoRefNumber"));
				}
			}
		} catch (NumberFormatException e) {
			request.setAttribute("errormsg", "Error Message: The format of the Work Order "
					+ request.getParameter("actualWoRefNumber") + " is wrong.");
			dispatcher = request.getRequestDispatcher("Q8changeinstallpointsinglewater.jsp");
		}

		try {
			DBConnection dbconn = new DBConnection();
			// first result
			Q6MWCustomerInfo customerInfo = dbconn.returnWaterCustomerInfo(WoRefPrefix, WoRefNumber);
			request.setAttribute("customerInfo", customerInfo);
			// second result
			Q6MWCustomerInfo customerInfo2 = dbconn.returnWaterCustomerInfoUnexecuted(WoRefPrefix2, WoRefNumber2);
			request.setAttribute("customerInfo2", customerInfo2);
			dbconn.closeConnection();
			if (customerInfo.getWorkOrderID() != null && customerInfo2.getWorkOrderID() != null
					&& !customerInfo.getWorkOrderID().contains(customerInfo2.getWorkOrderID())) {
				dispatcher = request.getRequestDispatcher("Q8changeinstallpointsinglewaterverify.jsp");
			} else if (customerInfo.getWorkOrderID() == null && customerInfo2.getWorkOrderID() == null) {
				request.setAttribute("errormsg", "Error Message: Could not retrieve information for workorder "
						+ WoRefNumber + " and workorder " + WoRefNumber2 + ".");
				dispatcher = request.getRequestDispatcher("Q8changeinstallpointsinglewater.jsp");
			} else if (customerInfo.getWorkOrderID() == null) {
				request.setAttribute("errormsg",
						"Error Message: Could not retrieve information for workorder " + WoRefNumber + ".");
				dispatcher = request.getRequestDispatcher("Q8changeinstallpointsinglewater.jsp");
			} else if (customerInfo2.getWorkOrderID() == null) {
				request.setAttribute("errormsg",
						"Error Message: Could not retrieve information for workorder " + WoRefNumber2 + ".");
				dispatcher = request.getRequestDispatcher("Q8changeinstallpointsinglewater.jsp");
			} else if (customerInfo.getWorkOrderID().contains(customerInfo2.getWorkOrderID())) {
				request.setAttribute("errormsg", "Error Message: The same workorder was entered twice.");
				dispatcher = request.getRequestDispatcher("Q6changeinstallpointwater.jsp");
			}
			// SONARQUBE
			if (dispatcher != null) {
				dispatcher.forward(request, response);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
