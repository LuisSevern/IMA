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
 * Servlet implementation class Q7UpdateMIUMeterRelationServlet
 */
public class Q7UpdateMIUMeterRelationServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Q7UpdateMIUMeterRelationServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//Logging test
		LoggingPath.setLpath(getServletContext().getRealPath("/logs/logs.txt"));
		
		int devrelid = Integer.parseInt(request.getParameter("devrelid"));
		int target = Integer.parseInt(request.getParameter("target"));
		int devrelid2 = Integer.parseInt(request.getParameter("devrelid2"));
		int target2 = Integer.parseInt(request.getParameter("target2"));
		
		try {
				DBConnection dbconn = new DBConnection();
				//temporary
				boolean temp = dbconn.updateMIUMeterTarget(0, devrelid2);
				if(temp)
				{
					boolean updateDevRel = dbconn.updateMIUMeterTarget(target2, devrelid);
					if(updateDevRel)
					{
						//request.setAttribute("updatePort", new Boolean(updatePort));
						dbconn.updateMIUMeterTarget(target, devrelid2);
						dbconn.closeConnection();
						RequestDispatcher dispatcher = request.getRequestDispatcher("Q7swapwmmiumoduleresult.jsp");
						dispatcher.forward(request, response);
					}
				}
				dbconn.closeConnection();
		}catch(Exception e)
		{
			//exception here
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
