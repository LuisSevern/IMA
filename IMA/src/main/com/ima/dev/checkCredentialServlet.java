package main.com.ima.dev;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import main.com.ima.dev.service.getUsers;

/**
 * Servlet implementation class checkCredentialServlet
 */

public class checkCredentialServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger =  LogManager.getLogger( main.com.ima.dev.checkCredentialServlet.class);

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public checkCredentialServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		logger.traceEntry("->");
		logger.debug("Hola d");
		logger.warn("Hola w");
		logger.info("Hola i");
		logger.error("Hola e");
		HttpSession session = request.getSession(true);
		session.setAttribute("username", request.getParameter("username"));
		String password = request.getParameter("password");
		RequestDispatcher dispatcher = null;

		if ((session.getAttribute("username") != null && ((String) session.getAttribute("username")).length() > 0)
				&& (password != null && !password.equals(""))) {
			// send LVC details to request
			/*getUsers users = new getUsers();
			boolean result = users.checkConnection(users.searchCredentials(session.getAttribute("username").toString()),
					password);
			*/
			boolean result = true;
			if (result) {
				dispatcher = request.getRequestDispatcher("Q2changeinstallpointsingle.jsp");
				dispatcher.forward(request, response);
			} else {
				request.setAttribute("errormsg", "Login Failed!");
				dispatcher = request.getRequestDispatcher("Login.jsp");
				dispatcher.forward(request, response);
			}
		} else {
			request.setAttribute("errormsg", "Login Failed!");
			dispatcher = request.getRequestDispatcher("Login.jsp");
			dispatcher.forward(request, response);
		}
	}

}
