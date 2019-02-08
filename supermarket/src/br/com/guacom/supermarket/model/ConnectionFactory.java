package br.com.guacom.supermarket.model;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionFactory {
	private static Properties props;
	
	public static Connection getConnection() {
		try {
			props = new Properties();
			props.load(new FileInputStream("config.properties"));
			return DriverManager.getConnection(props.getProperty("config_con"), 
					props.getProperty("user"), props.getProperty("pass"));
		} catch (SQLException | IOException e) {
			e.printStackTrace();
		}
		throw new IllegalArgumentException();
	}
}
