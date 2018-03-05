package idv.ron.server.members;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

/**
 * Servlet implementation class Sendemail
 */
@WebServlet("/Sendemail")
public class Sendemail extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private final static String CONTENT_TYPE = "text/html; charset=UTF-8";
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}
	protected void doPost(HttpServletRequest rq, HttpServletResponse rp) throws ServletException, IOException {
		Gson gson=new Gson();
		BufferedReader br=rq.getReader();
		StringBuffer jsonIn=new StringBuffer();
		String line=null;
		while((line=br.readLine())!=null) {
			jsonIn.append(line);
		}
		System.out.println("input: "+jsonIn);
		JsonObject jsonObject=gson.fromJson(jsonIn.toString(), JsonObject.class);
		boolean ismailsend=false;
		
		//取得傳過來json中的email以及姓名
		String user_email=jsonObject.get("user_email").getAsString();
		String user_name =jsonObject.get("user_name").getAsString();
		
		//產生亂數
		int z;
		StringBuilder sb = new StringBuilder();
		int i;
		for (i = 0; i < 8; i++) {
			z = (int) ((Math.random() * 7) % 3);
			if (z == 1) { // 放數字
				sb.append((int) ((Math.random() * 9) + 1));
			} else if (z == 2) { // 放大寫英文
				sb.append((char) (((Math.random() * 26) + 65)));
			} else {// 放小寫英文
				sb.append((char) ((Math.random() * 26) + 97));
			}
		}
		//亂數碼叫做sbs
		String sbs = sb.toString();
		
		//寄信
		String host = "smtp.gmail.com";
		int port = 587;
		final String user = "iw5420@gmail.com";// your email
		final String password = "qaz*****";// your password

		Properties props = new Properties();
		props.put("mail.smtp.host", host);
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.port", port);
		Session session2 = Session.getInstance(props, new Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(user, password);
			}
		});
		try {
			Message message = new MimeMessage(session2);
			message.setFrom(new InternetAddress("iw5420@gmail.com"));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(user_email));
			message.setSubject("測試寄信.");
			message.setText("Dear " + user_name + ", 您的驗證碼為" + sbs);

			Transport transport = session2.getTransport("smtp");
			transport.connect(host, port, user, password);
			Transport.send(message);
			System.out.println("寄送email結束.");				
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
		Cookie cookieVerifynumber=null;
		cookieVerifynumber = new Cookie("verifynumber", sbs);
		cookieVerifynumber.setMaxAge(30 * 60 * 60);
		cookieVerifynumber.setPath(rq.getContextPath());
		rp.addCookie(cookieVerifynumber);
		
		ismailsend=true;
		jsonObject = new JsonObject();
		jsonObject.addProperty("ismailsend", ismailsend);	
		rp.setContentType(CONTENT_TYPE);
		PrintWriter out = rp.getWriter();
		out.println(jsonObject.toString());
	}

}
