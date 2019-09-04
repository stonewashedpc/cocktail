package com.stonewashedpc.cocktailmaker.images;

import java.sql.Blob;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "IMAGES")
public class CocktailImage {
	
	@Id
	@Column(name = "RECIPEID")
	private int recipeId;

	public int getRecipeId() {
		return recipeId;
	}

	public void setRecipeId(int recipeId) {
		this.recipeId = recipeId;
	}
	
	@Id
	@Column(name = "IMAGEBLOB")
	private Blob imageBlob;

	public Blob getImageBlob() {
		return imageBlob;
	}

	public void setImageBlob(Blob imageBlob) {
		this.imageBlob = imageBlob;
	}
}
