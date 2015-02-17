package com.arcanum.statistics;

import java.util.Hashtable;

public class StatisticalValueTypes extends Hashtable<String,StatisticalValueType> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -290418949150289152L;
	String name;
	public StatisticalValueTypes(String name) 
	{
		this.name = name;
	}
	public String getName()
	{
		return name;
	}
	
}
