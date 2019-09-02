package com.stonewashedpc.cocktailmaker.pumps;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;

@Stateless
public class PumpBoundary implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final String JDBC_DRIVER = "org.h2.Driver";

	private static final String DB_URL = "jdbc:h2:~/test";
	
	
	public List<Pump> findPumps() {
		List<Pump> list = null;
		try {
			// Register JDBC database driver
			Class.forName(JDBC_DRIVER);
			// Open connection
			Connection conn = DriverManager.getConnection(DB_URL, "sa", "");
			// Create statement
			Statement stmt = conn.createStatement();
			//Execute and convert to ArrayList
			list = pumpResultToList(stmt.executeQuery("select p.*, ingredients.name from pump_config as p inner join INGREDIENTS on p.ingredient_id = ingredients.id"));
			// Close connection
			conn.close();
		} catch (SQLException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}
	
	private List<Pump> pumpResultToList(ResultSet rs) {
		List<Pump> pumps = new ArrayList<Pump>();
		try {
			while (rs.next()) {
				Pump pump = new Pump();
				pump.setGPIO(rs.getInt("gpio"));
				pump.setIngredientId(rs.getInt("ingredient_id"));
				pump.setPumpSpeed(rs.getInt("pump_speed"));
				pump.setIngredientName(rs.getString("name"));
				pumps.add(pump);
			}
			rs.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return pumps;
	}
	
	public void deletePump(int gpio) {
		try {
			// Register JDBC database driver
			Class.forName(JDBC_DRIVER);
			// Open connection
			Connection conn = DriverManager.getConnection(DB_URL, "sa", "");
			// Create statement
			Statement stmt = conn.createStatement();
			//Execute and convert to ArrayList
			stmt.execute("delete from pump_config where gpio = " + gpio);
			// Close connection
			conn.close();
		} catch (SQLException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void addPump(int gpio, int ingredient_id, int pump_speed) {
		try {
			// Register JDBC database driver
			Class.forName(JDBC_DRIVER);
			// Open connection
			Connection conn = DriverManager.getConnection(DB_URL, "sa", "");
			// Create statement
			Statement stmt = conn.createStatement();
			//Execute and convert to ArrayList
			stmt.execute(String.format("insert into pump_config values(%d, %d, %d)", gpio, ingredient_id, pump_speed));
			// Close connection
			conn.close();
		} catch (SQLException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
