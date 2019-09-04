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
	@Column(name = "INGREDIENTID")
	private int ingredientId;

	public int getIngredientId() {
		return ingredientId;
	}

	public void setIngredientId(int ingredientId) {
		this.ingredientId = ingredientId;
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
