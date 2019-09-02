package com.stonewashedpc.cocktailmaker.pumps;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "PUMPS")
public class Pump {

	@Id
	@Column(name = "ID")
	private int id;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Id
	@Column(name = "GPIO")
	private int gpio;

	public int getGPIO() {
		return gpio;
	}

	public void setGPIO(int gpio) {
		this.gpio = gpio;
	}

	@Id
	@Column(name = "INGREDIENT_ID")
	private int ingredient_id;

	public int getIngredientId() {
		return ingredient_id;
	}

	public void setIngredientId(int ingredient_id) {
		this.ingredient_id = ingredient_id;
	}
	
	@Id
	@Column(name = "INGREDIENTNAME")
	private String ingredientName;

	public String getIngredientName() {
		return ingredientName;
	}

	public void setIngredientName(String ingredientName) {
		this.ingredientName = ingredientName;
	}

	@Id
	@Column(name = "PUMPSPEED")
	private int pumpSpeed;

	public int getPumpSpeed() {
		return pumpSpeed;
	}

	public void setPumpSpeed(int i) {
		this.pumpSpeed = i;
	}
}
