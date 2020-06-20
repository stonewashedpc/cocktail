package com.stonewashedpc.cocktailmaker.images;

import java.io.Serializable;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.ejb.Stateless;

@Stateless
public class CocktailImageBoundary implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final String JDBC_DRIVER = "org.h2.Driver";

	private static final String DB_URL = "jdbc:h2:~/test";

	public byte[] findCocktailImage(int recipe_id) {
		byte[] byte_array = null;
		try {
			// Register JDBC database driver
			Class.forName(JDBC_DRIVER);
			// Open connection
			Connection conn = DriverManager.getConnection(DB_URL, "sa", "");
			// Create statement
			Statement stmt = conn.createStatement();
			// Execute and convert to ArrayList
			ResultSet rs = stmt.executeQuery("select * from cocktail_images where recipe_id = " + recipe_id);
			
			rs.first();
			
			Blob blob = rs.getBlob("blob");
			byte_array = blob.getBytes(1, (int) blob.length());
			
			blob.free();

			// Close connection
			conn.close();
		} catch (SQLException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return byte_array;
	}
}