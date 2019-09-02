package com.stonewashedpc.cocktailmaker.recipies;

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
public class RecipeBoundary implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final String JDBC_DRIVER = "org.h2.Driver";

	private static final String DB_URL = "jdbc:h2:~/test";
	
	
	public List<Recipe> findRecipes() {
		List<Recipe> list = null;
		try {
			// Register JDBC database driver
			Class.forName(JDBC_DRIVER);
			// Open connection
			Connection conn = DriverManager.getConnection(DB_URL, "sa", "");
			// Create statement
			Statement stmt = conn.createStatement();
			//Execute and convert to ArrayList
			list = recipeResultToList(stmt.executeQuery("select * from recipes"));
			// Close connection
			conn.close();
		} catch (SQLException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}
	
	// SQL for finding only possible recipes: 
	// select DISTINCT r.name from recipes r where not exists (select 1 from rel_recipe_ingredient i where r.id=i.recipe_id and i.ingredient_id not in (select ingredient_id from PUMP_CONFIG))
	public List<Recipe> findPossibleRecipes() {
		List<Recipe> list = null;
		try {
			// Register JDBC database driver
			Class.forName(JDBC_DRIVER);
			// Open connection
			Connection conn = DriverManager.getConnection(DB_URL, "sa", "");
			// Create statement
			Statement stmt = conn.createStatement();
			//Execute and convert to ArrayList
			list = recipeResultToList(stmt.executeQuery("select DISTINCT r.* from recipes r where not exists (select 1 from rel_recipe_ingredient i where r.id=i.recipe_id and i.ingredient_id not in (select ingredient_id from PUMP_CONFIG))"));
			// Close connection
			conn.close();
		} catch (SQLException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}
	
	private List<Recipe> recipeResultToList(ResultSet rs) {
		List<Recipe> recipes = new ArrayList<Recipe>();
		try {
			while (rs.next()) {
				Recipe recipe = new Recipe();
				recipe.setName(rs.getString("name"));
				recipe.setId(rs.getInt("id"));
				recipes.add(recipe);
			}
			rs.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return recipes;
	}
}
