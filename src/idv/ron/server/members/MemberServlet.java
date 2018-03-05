package idv.ron.server.members;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

@SuppressWarnings("serial")
public class MemberServlet extends HttpServlet {
	private final static String CONTENT_TYPE = "text/html; charset=UTF-8";

	// the request method for Android is set to be Post
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		Gson gson = new Gson();
		BufferedReader br = request.getReader();
		StringBuilder jsonIn = new StringBuilder();
		String line = null;
		while ((line = br.readLine()) != null) {
			jsonIn.append(line);
		}
		JsonObject jsonObject = gson.fromJson(jsonIn.toString(), JsonObject.class);
		MemberDao memberDao = new MemberDaoMySqlImpl();
		String action = jsonObject.get("action").getAsString();
		System.out.println("action: " + action);
		if (action.equals("memberExist")) {
			String user_cellphone = jsonObject.get("user_cellphone").getAsString();
			String password = jsonObject.get("password").getAsString();
			System.out.println("userId: " + user_cellphone + "; password: " + password);
			writeText(response, String.valueOf(memberDao.memberExist(user_cellphone, password)));
		} else if (action.equals("memberIdExist")) {
			String user_cellphone = jsonObject.get("user_cellphone").getAsString();
			writeText(response, String.valueOf(memberDao.memberIdExist(user_cellphone)));
		} else if (action.equals("findById")) {
			String user_cellphone = jsonObject.get("user_cellphone").getAsString();
			Member member = memberDao.findById(user_cellphone);
			writeText(response, gson.toJson(member));
		} else if (action.equals("insert")) {
			Member member = gson.fromJson(jsonObject.get("member").getAsString(), Member.class);
			writeText(response, String.valueOf(memberDao.insert(member)));
		} else if (action.equals("update")) {
			Member member = gson.fromJson(jsonObject.get("member").getAsString(), Member.class);
			writeText(response, String.valueOf(memberDao.update(member)));
		}
	}

	// just return order information (general page's request method is Get)
	public void doGet(HttpServletRequest rq, HttpServletResponse rp) throws ServletException, IOException {
		doPost(rq, rp);
	}

	private void writeText(HttpServletResponse response, String outText) throws IOException {
		response.setContentType(CONTENT_TYPE);
		PrintWriter out = response.getWriter();
		System.out.println("outText: " + outText);
		out.print(outText);
	}
}