package com.stonewashedpc.cocktailmaker.recipies;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table( name="RECIPES" )
public class Recipe {
	
	@Id
	@Column( name="ID" )
	private int id;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	@Id
	@Column( name="NAME" )
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Recipe() {

	}

	public Recipe(String name) {

		this.name = name;
	}
}
