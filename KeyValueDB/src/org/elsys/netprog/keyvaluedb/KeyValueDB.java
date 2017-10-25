package org.elsys.netprog.keyvaluedb;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/KeyValueDB")
public class KeyValueDB extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private HashMap<String, String> db;

	public KeyValueDB() {
		db = new HashMap<String, String>();
		db.put("item1", "value1");
		db.put("item2", "value2");
		db.put("item3", "value3");
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		for (String key : db.keySet()) {
			response.getOutputStream().println(key + " : " + db.get(key));
		}
	}
	
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String key = request.getParameter("key");
		String value = request.getParameter("value");
		db.put(key, value);
	}
	
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String key = request.getParameter("key");
		db.remove(key);
	}
}
