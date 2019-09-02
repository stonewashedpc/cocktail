package com.stonewashedpc.cocktailmaker;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.stonewashedpc.cocktailmaker.recipies.Recipe;
import com.stonewashedpc.cocktailmaker.recipies.RecipeBoundary;

@Named
@ViewScoped
public class CocktailMachine implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private RecipeBoundary recipeBoundary;
	
	private List<Recipe> recipes;	
	
	private int count;
	
	@PostConstruct
	private void init() {
		this.recipes = recipeBoundary.findRecipes();
	}
	
	public int getCount() {
		return count;
	}
	
	public void addAction() {
		count++;
	}
		
	public List<Recipe> getRecipes() {
		return recipes;
	}
}
