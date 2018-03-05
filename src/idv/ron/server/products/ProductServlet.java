package idv.ron.server.products;

import idv.ron.server.main.ImageUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

@SuppressWarnings("serial")
@WebServlet("/ProductServlet")
public class ProductServlet extends HttpServlet {
	private final static String CONTENT_TYPE = "text/html; charset=UTF-8";

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
		ProductDao productDao = new ProductDaoMySqlImpl();
		String action = jsonObject.get("action").getAsString();
		System.out.println("action: " + action);

		if (action.equals("getAll")) {
			List<Product> products = productDao.getAll();
			writeText(response, gson.toJson(products));
		} else if (action.equals("getImage")) {
			OutputStream os = response.getOutputStream();
			int id = jsonObject.get("id").getAsInt();
			int imageSize = jsonObject.get("imageSize").getAsInt();
			byte[] image = productDao.getImage(id);
			if (image != null) {
				image = ImageUtil.reduceImage(image, imageSize);
				response.setContentType("image/jpeg");
				response.setContentLength(image.length);
			}
			os.write(image);
		} else {
			writeText(response, "");
		}
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		ProductDao productDao = new ProductDaoMySqlImpl();
		List<Product> products = productDao.getAll();
		writeText(response, new Gson().toJson(products));
	}

	private void writeText(HttpServletResponse response, String outText)
			throws IOException {
		response.setContentType(CONTENT_TYPE);
		PrintWriter out = response.getWriter();
		// System.out.println("outText: " + outText);
		out.print(outText);
	}
}