package com.acl.dronedispatcher.domain.enums;

public enum StateEnum {

	IDLE("IDLE"),
	LOADING("LOADING"),
	LOADED("LOADED"),
	DELIVERING("DELIVERING"),
	DELIVERED("DELIVERED"),
	RETURNING("RETURNING");

	private final String state;

	StateEnum(String state) {
		this.state = state;
	}

	public String getState() {
		return this.state;
	}
}