package com.ozgur.model;

import com.fasterxml.jackson.annotation.JsonRootName;


@JsonRootName(value = "geoLocation")
public class GeoLocation {
	private String lng;

	private String lat;

	public String getLng() {
		return lng;
	}

	public void setLng(String lng) {
		this.lng = lng;
	}

	public String getLat() {
		return lat;
	}

	public void setLat(String lat) {
		this.lat = lat;
	}
}
