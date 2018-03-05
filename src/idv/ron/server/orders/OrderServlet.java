package idv.ron.server.orders;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

@SuppressWarnings("serial")
public class OrderServlet extends HttpServlet {
	private final static String CONTENT_TYPE = "text/html; charset=UTF-8";

	// the request method for Android is set to be Post
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		Gson gson = new Gson();
		BufferedReader br = request.getReader();
		StringBuilder jsonIn = new StringBuilder();
		String line = null;
		while ((line = br.readLine()) != null) {
			jsonIn.append(line);
		}
		JsonObject jsonObject = gson.fromJson(jsonIn.toString(),
				JsonObject.class);
		OrderDao orderDAO = new OrderDaoMySqlImpl();
		String action = jsonObject.get("action").getAsString();
		System.out.println("action: " + action);
		String userId = jsonObject.get("userId").getAsString();

		if (action.equals("orderInsert")) {
			String cartJson = jsonObject.get("cart").getAsString();
			Type listType = new TypeToken<List<OrderProduct>>() {
			}.getType();
			List<OrderProduct> cart = gson.fromJson(cartJson, listType);
			int orderId = orderDAO.insert(userId, cart);
			Order order = null;
			if (orderId != -1) {
				order = orderDAO.findById(orderId);
			}
			writeText(response, gson.toJson(order));
		} else if (action.equals("getAll")) {
			String start = jsonObject.get("start").getAsString();
			String end = jsonObject.get("end").getAsString();
			List<Order> orders = orderDAO.getAll(userId, start, end);
			writeText(response, gson.toJson(orders));
		}
	}

	// just return order information (default page's request method is Get)
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		writeText(response, "orders");
	}

	private void writeText(HttpServletResponse response, String outText)
			throws IOException {
		response.setContentType(CONTENT_TYPE);
		PrintWriter out = response.getWriter();
		// System.out.println("outText: " + outText);
		out.print(outText);
	}
}