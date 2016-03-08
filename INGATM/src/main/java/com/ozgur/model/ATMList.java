package com.ozgur.model;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ATMList {
	
	private ArrayList<ATM> atms;

	public ArrayList<ATM> getAtms() {
		return atms;
	}

	public void setAtms(ArrayList<ATM> atms) {
		this.atms = atms;
	}
	

}
