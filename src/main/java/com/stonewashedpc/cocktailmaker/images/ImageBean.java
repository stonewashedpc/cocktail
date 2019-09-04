package com.stonewashedpc.cocktailmaker.images;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;

@Named
@ApplicationScoped
public class ImageBean {
	
	@Inject
	private CocktailImageBoundary cocktailImageBoundary;
	
	public byte[] getImageBytes(int recipe_id) {
		return cocktailImageBoundary.findCocktailImage(recipe_id);
	}
}
