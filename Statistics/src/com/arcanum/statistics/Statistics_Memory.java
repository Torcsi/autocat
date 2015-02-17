package com.arcanum.statistics;

import java.io.PrintStream;
import java.util.Hashtable;
import java.util.Vector;

public class Statistics_Memory implements Statistics{

	String name;
	public Statistics_Memory(String name) {
		this.name = name;
	}
	@Override
	public String getName() {
		return name;
	}
	@Override
	public void write(PrintStream w) throws Exception {
		// TODO Auto-generated method stub
		
	}
	Hashtable<String,StatisticalValueType> valueTypes = new Hashtable<String,StatisticalValueType>(); 
	@Override
	public void defineType(StatisticalValueType valueType) {
		valueTypes.put(valueType.name, valueType);
		
	}
	@Override
	public StatisticalValueType getType(String name) {
		if( valueTypes.containsKey(name))
			return valueTypes.get(name);
		return null;
	}
	
	Hashtable<String,StatisticalValueSet> valueSets = new Hashtable<String,StatisticalValueSet>();
	
	@Override
	public StatisticalValueSet openSet(String name) {
		StatisticalValueSet valueSet = new StatisticalValueSet_Memory(this,name);
		valueSets.put(name, valueSet);
		return valueSet;
	}
	@Override
	public StatisticalValueSet openSet(StatisticalValueSet parentSet,
			String name) {
		StatisticalValueSet valueSet = new StatisticalValueSet_Memory(this,name);
		parentSet.addSubset(valueSet);
		return valueSet;
	}
	@Override
	public void closeSet(StatisticalValueSet valueSet) {
		
		
	}
	
	@Override
	public Hashtable<String,StatisticalValueType> getValueTypes()
	{
		return valueTypes;
	}
	
	Hashtable<String,StatisticalValueTypes> valueTypesSets=new Hashtable<String,StatisticalValueTypes>();
	@Override
	public void addValueTypes(StatisticalValueTypes valueTypes) {
		valueTypesSets.put(valueTypes.getName(), valueTypes);
	}

}
