package main.java.com.ima.dev;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import main.java.com.ima.dev.dto.LoggingPath;
import main.java.com.ima.dev.dto.Q7SwapModule;
import main.java.com.ima.dev.service.DBConnection;

/**
 * Servlet implementation class Q7WaterDetailsByWorkOrder
 */
public class Q7WaterDetailsByWorkOrderServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Q7WaterDetailsByWorkOrderServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// Logging test
		LoggingPath.setLpath(getServletContext().getRealPath("/logs/logs.txt"));
		RequestDispatcher dispatcher = null;

		// First Request
		int WoRefPrefix = Integer.parseInt(request.getParameter("woRefPrefix"));
		String ignoreSuffix = request.getParameter("noSuffix");
		boolean addSuffix = true;
		if (ignoreSuffix != null && ignoreSuffix.equalsIgnoreCase("on")) {
			addSuffix = false;
		}

		long WoRefNumber = 0;
		try {
			if (WoRefPrefix == 1) {
				if (addSuffix) {
					WoRefNumber = Long.parseLong(request.getParameter("woRefNumber") + "01");
				} else {
					WoRefNumber = Long.parseLong(request.getParameter("woRefNumber"));
				}

			} else if (WoRefPrefix == 3) {
				if (addSuffix) {
					WoRefNumber = Long
							.parseLong(request.getParameter("woRefNumber") + "0" + request.getParameter("suffix"));
				} else {
					WoRefNumber = Long.parseLong(request.getParameter("woRefNumber"));
				}
			}
		} catch (NumberFormatException e) {
			request.setAttribute("errormsg", "Error Message: The format of the Work Order "
					+ request.getParameter("woRefNumber") + " is wrong.");
			dispatcher = request.getRequestDispatcher("Q7swapwmmiumodule.jsp");
		}
		// String MeterSN = request.getParameter("MeterSN");

		// Second Request
		int WoRefPrefix2 = Integer.parseInt(request.getParameter("woRefPrefix2"));
		String ignoreSuffix2 = request.getParameter("noSuffix2");
		boolean addSuffix2 = true;
		if (ignoreSuffix2 != null && ignoreSuffix2.equalsIgnoreCase("on")) {
			addSuffix2 = false;
		}

		long WoRefNumber2 = 0;
		try {
			if (WoRefPrefix2 == 1) {
				if (addSuffix2) {
					WoRefNumber2 = Long.parseLong(request.getParameter("woRefNumber2") + "01");
				} else {
					WoRefNumber2 = Long.parseLong(request.getParameter("woRefNumber2"));
				}
			} else if (WoRefPrefix2 == 3) {
				if (addSuffix2) {
					WoRefNumber2 = Long
							.parseLong(request.getParameter("woRefNumber2") + "0" + request.getParameter("suffix2"));
				} else {
					WoRefNumber2 = Long.parseLong(request.getParameter("woRefNumber2"));
				}
			}
		} catch (NumberFormatException e) {
			request.setAttribute("errormsg", "Error Message: The format of the Work Order "
					+ request.getParameter("woRefPrefix2") + " is wrong.");
			dispatcher = request.getRequestDispatcher("Q7swapwmmiumodule.jsp");
		}
		// String MeterSN2 = request.getParameter("MeterSN2");

		try {
			DBConnection dbconn = new DBConnection();
			// first result
			Q7SwapModule woInfo = dbconn.returnWaterMeterModule(WoRefPrefix, WoRefNumber);
			request.setAttribute("woInfo", woInfo);
			// second result
			Q7SwapModule woInfo2 = dbconn.returnWaterMeterModule(WoRefPrefix2, WoRefNumber2);
			dbconn.closeConnection();
			request.setAttribute("woInfo2", woInfo2);
			// initialize dispatcher
			if (woInfo.getWorkorder() != null && woInfo2.getWorkorder() != null
					&& !woInfo.getWorkorder().contains(woInfo2.getWorkorder())) {
				dispatcher = request.getRequestDispatcher("Q7swapwmmiumoduleverify.jsp");
			} else if (woInfo.getWorkorder() == null && woInfo2.getWorkorder() == null) {
				request.setAttribute("errormsg", "Error Message: Could not retrieve information for workorder "
						+ WoRefNumber + " and workorder " + WoRefNumber2 + ".");
				dispatcher = request.getRequestDispatcher("Q7swapwmmiumodule.jsp");
			} else if (woInfo.getWorkorder() == null) {
				request.setAttribute("errormsg",
						"Error Message: Could not retrieve information for workorder " + WoRefNumber + ".");
				dispatcher = request.getRequestDispatcher("Q7swapwmmiumodule.jsp");
			} else if (woInfo2.getWorkorder() == null) {
				request.setAttribute("errormsg",
						"Error Message: Could not retrieve information for workorder " + WoRefNumber2 + ".");
				dispatcher = request.getRequestDispatcher("Q7swapwmmiumodule.jsp");
			} else if (woInfo.getWorkorder().contains(woInfo2.getWorkorder())) {
				request.setAttribute("errormsg", "Error Message: The same workorder was entered twice.");
				dispatcher = request.getRequestDispatcher("Q7swapwmmiumodule.jsp");
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
