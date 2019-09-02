package com.stonewashedpc.cocktailmaker.ingredients;

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
public class IngredientBoundary implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final String JDBC_DRIVER = "org.h2.Driver";

	private static final String DB_URL = "jdbc:h2:~/test";
	
	
	public List<Ingredient> findIngredients() {
		List<Ingredient> list = null;
		try {
			// Register JDBC database driver
			Class.forName(JDBC_DRIVER);
			// Open connection
			Connection conn = DriverManager.getConnection(DB_URL, "sa", "");
			// Create statement
			Statement stmt = conn.createStatement();
			//Execute and convert to ArrayList
			list = ingredientResultToList(stmt.executeQuery("select ingredients.*, 0 as amount from ingredients"));
			// Close connection
			conn.close();
		} catch (SQLException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}
	
	public List<Ingredient> findIngredientsByRecipe(int recipe_id) {
		List<Ingredient> list = null;
		try {
			// Register JDBC database driver
			Class.forName(JDBC_DRIVER);
			// Open connection
			Connection conn = DriverManager.getConnection(DB_URL, "sa", "");
			// Create statement
			Statement stmt = conn.createStatement();
			//Execute and convert to ArrayList
			list = ingredientResultToList(stmt.executeQuery("select ing.id, ing.name, rel_recipe_ingredient.AMOUNT from (select * from ingredients where id IN (select ingredient_id from rel_recipe_ingredient where recipe_id = " + recipe_id + ")) AS ing INNER JOIN REL_RECIPE_INGREDIENT ON ing.id = rel_recipe_ingredient.ingredient_id AND rel_recipe_ingredient.RECIPE_ID = " + recipe_id));
			// Close connection
			conn.close();
		} catch (SQLException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}
	
	public List<Amount> findIngredientAmounts(int recipe_id) {
		List<Amount> list = null;
		try {
			// Register JDBC database driver
			Class.forName(JDBC_DRIVER);
			// Open connection
			Connection conn = DriverManager.getConnection(DB_URL, "sa", "");
			// Create statement
			Statement stmt = conn.createStatement();
			//Execute and convert to ArrayList
			list = ingredientAmountsResultToList(stmt.executeQuery("select * from rel_recipe_ingredient where recipe_id = "+ recipe_id));
			// Close connection
			conn.close();
		} catch (SQLException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}
	
	private List<Amount> ingredientAmountsResultToList(ResultSet rs) {
		List<Amount> amounts = new ArrayList<Amount>();
		try {
			while (rs.next()) {
				Amount amount = new Amount();
				amount.setAmount(rs.getString("amount"));
				amount.setId(rs.getInt("recipe_id"));
				amounts.add(amount);
			}
			rs.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return amounts;
	}
	
	private List<Ingredient> ingredientResultToList(ResultSet rs) {
		List<Ingredient> ingredients = new ArrayList<Ingredient>();
		try {
			while (rs.next()) {
				Ingredient ingredient = new Ingredient();
				ingredient.setName(rs.getString("name"));
				ingredient.setId(rs.getInt("id"));
				ingredient.setAmount(rs.getInt("amount"));
				ingredients.add(ingredient);
			}
			rs.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ingredients;
	}
}
