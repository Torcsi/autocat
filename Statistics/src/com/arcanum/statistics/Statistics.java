package com.arcanum.statistics;

import java.io.PrintStream;
import java.util.Hashtable;
import java.util.Vector;

public interface Statistics{
	String getName();
	
	void write(PrintStream w) throws Exception;
	
	void addValueTypes(StatisticalValueTypes valueSet);
	
	
	void defineType(StatisticalValueType valueType);
	StatisticalValueType getType(String name);
	Hashtable<String,StatisticalValueType> getValueTypes();

	StatisticalValueSet openSet(String name);
	StatisticalValueSet openSet(StatisticalValueSet parentSet, String name);
	void closeSet(StatisticalValueSet valueSet);
	
	
}
