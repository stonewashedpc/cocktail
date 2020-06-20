package com.stonewashedpc.cocktailmaker.images;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.stonewashedpc.cocktailmaker.Main;

@Named
@ApplicationScoped
public class ImageBean {
	
	private static final Logger LOG = Logger.getLogger(Main.class.getSimpleName());
	
	@Inject
	private CocktailImageBoundary cocktailImageBoundary;
	
	public byte[] getImageBytes(int recipe_id) {
		LOG.log(Level.INFO, "Image id: " + recipe_id);
		return cocktailImageBoundary.findCocktailImage(recipe_id);
	}
}
