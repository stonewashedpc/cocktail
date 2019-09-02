package com.stonewashedpc.cocktailmaker.ingredients;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "AMOUNTS")
public class Amount {


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
	@Column(name = "AMOUNT")
	private String amount;

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}
	
	public Amount() {

	}

	public Amount(String amount) {

		this.amount = amount;
	}
}
