package main.java.com.ima.dev;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import main.java.com.ima.dev.dto.LoggingPath;
import main.java.com.ima.dev.dto.Q5MiuMeterRelation;
import main.java.com.ima.dev.service.DBConnection;

/**
 * Servlet implementation class Q5UpdateMiuMeterRelationServlet
 */

public class Q5UpdateMiuMeterRelationServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LogManager.getLogger(Q5UpdateMiuMeterRelationServlet.class);

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Q5UpdateMiuMeterRelationServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		logger.info("Change miu port. Update. Init ->");
		String[] chkvalue = request.getParameterValues("chk");
		// testing
		int rsid;
		int woid;

		try {
			DBConnection dbconn1 = new DBConnection();
			String rfSerial = dbconn1.getRfSerial(Integer.parseInt(chkvalue[0].substring(0, chkvalue[0].indexOf("~"))));
			ArrayList devicesConnected = dbconn1.getRFDetails(rfSerial);
			dbconn1.closeConnection();

			RequestDispatcher dispatcher;
			if (!areRepeatedPorts(request, chkvalue, devicesConnected)) {
				for (int i = 0; i < chkvalue.length; i++) {
					// get relationship id & woid
					rsid = Integer.parseInt(chkvalue[i].substring(0, chkvalue[i].indexOf("~")));
					woid = Integer.parseInt(chkvalue[i].substring(chkvalue[i].indexOf("~") + 1));
					String parameter = request.getParameter(chkvalue[i]);
					if (parameter != null) {
						DBConnection dbconn = new DBConnection();
						boolean updatePort = dbconn.updateRFMeterPort(parameter, rsid);
						boolean updatePortWO = dbconn.updateRFMeterPortArgs(parameter, woid);
						dbconn.closeConnection();
						request.setAttribute("updatePort", Boolean.valueOf(updatePort));
						request.setAttribute("updatePortWO", Boolean.valueOf(updatePortWO));
					} else {
						request.setAttribute("updatePort", Boolean.valueOf(false));
						request.setAttribute("updatePortWO", Boolean.valueOf(false));
					}
				}

				dispatcher = request.getRequestDispatcher("Q5changemiuportresult.jsp");
			} else {
				request.setAttribute("miuInfo", devicesConnected);
				request.setAttribute("errormsg", "Error Message: Could not change port information. Repeated ports.");
				dispatcher = request.getRequestDispatcher("Q5changemiuportverify.jsp");
			}

			dispatcher.forward(request, response);

		} catch (Exception e) {
			logger.error("Change miu port. Update: Exception", e);
		}
		logger.info("Change miu port. Update. End <-");
	}

	private boolean areRepeatedPorts(HttpServletRequest request, String[] chkvalue, ArrayList devicesConnected) {
		boolean repeated = false;
		int rsid;

		Map relationIdPortMap = new HashMap();
		for (int i = 0; i < devicesConnected.size(); i++) {
			relationIdPortMap.put(Integer.valueOf(((Q5MiuMeterRelation) devicesConnected.get(i)).getId()),
					Integer.valueOf(((Q5MiuMeterRelation) devicesConnected.get(i)).getPort()));
		}
		for (int i = 0; i < chkvalue.length; i++) {
			rsid = Integer.parseInt(chkvalue[i].substring(0, chkvalue[i].indexOf("~")));
			String port = request.getParameter(chkvalue[i]);
			relationIdPortMap.put(Integer.valueOf(rsid), Integer.valueOf(port));
		}
		Iterator iterator = relationIdPortMap.keySet().iterator();
		Set values = new HashSet();
		while (iterator.hasNext() && !repeated) {
			Integer next = (Integer) iterator.next();
			Integer puerto = (Integer) relationIdPortMap.get(next);
			if (values.contains(puerto)) {
				repeated = true;
			} else {
				values.add(puerto);
			}
		}
		return repeated;
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
