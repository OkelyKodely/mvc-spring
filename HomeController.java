package com.journaldev.spring.controller;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.sql.*;

import com.journaldev.spring.model.User;

@Controller
public class HomeController {

	private String theDate = null;

private String hostName = "ec2-54-163-240-54.compute-1.amazonaws.com";
private String dbName = "d89l9begjikklj";
private String userName = "isscllglmxgeln";
private String passWord = "334f696049572d4bc9c3b6b78c3410301e24dd3b5fd2b96dc15bf4c1c6fed113";

private Connection conn = null;

	private void getConnect() {
try {
    Class.forName("org.postgresql.Driver");
    String url = "jdbc:postgresql://" + hostName + "/" + dbName + "?user=" + userName + "&password=" + passWord + "&ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory";
    conn = DriverManager.getConnection(url);
} catch(Exception e) {}
	}

	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Locale locale, Model model) {
System.out.println("Home Page Requested, locale = " + locale);
Date date = new Date();
DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);

String formattedDate = dateFormat.format(date);

		model.addAttribute("serverTime", formattedDate);

String sql = "";

getConnect();

Statement st = null;
try {
	sql = "insert into jspblog (title, datetime, content) values ('"+formattedDate+"', current_timestamp, 'test by jerry springer')";
	st = conn.createStatement();
	st.execute(sql);
} catch(Exception e) {}

		try {
			conn.close();
		} catch(Exception e) {}

		return "home";
	}

	@RequestMapping(value = "/user", method = RequestMethod.POST)
	public String user(@Validated User user, Model model) {
		System.out.println("User Page Requested");
		model.addAttribute("userName", user.getUserName());
		model.addAttribute("userName2", "abc");
		getConnect();
		this.theDate = "";
		String sql = "SELECT datetime FROM jspblog";
		try {
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(sql);
			while(rs.next()) {
				this.theDate = this.theDate + rs.getString("datetime") + System.getProperty("line.separator");
			}
			conn.close();
		} catch(Exception e) {}
		model.addAttribute("dbServerTime", this.theDate);
		return "user";
	}
}