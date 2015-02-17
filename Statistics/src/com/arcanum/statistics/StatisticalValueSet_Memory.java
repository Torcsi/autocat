package com.arcanum.statistics;

import java.util.Hashtable;
import java.util.Vector;

public class StatisticalValueSet_Memory implements StatisticalValueSet {

	Statistics_Memory statistics;
	String name;
	public StatisticalValueSet_Memory(Statistics_Memory statistics,
			String name) {
		this.statistics = statistics;
		this.name = name;
	}

	@Override
	public Statistics getStatistics() {
		return statistics;
	}
	@Override
	public String getName(){
		return name;
	}

	Hashtable<String,StatisticalValue> values = new Hashtable<String,StatisticalValue>(); 
	@Override
	public boolean contains(String valueName) {
		
		return values.containsKey(valueName);
	}

	@Override
	public StatisticalValue set(StatisticalValueType t, double d) {
		StatisticalValue v;
		if( values.containsKey(t.getName())){
			v = values.get(t.getName());
		}else{
			v = new StatisticalValue(t,d);
			values.put(t.getName(), v);
		}
		return v;
	}

	@Override
	public StatisticalValue get(String valueName) {
		if( !values.containsKey(valueName))
			return null;
		return values.get(valueName);
	}

	@Override
	public void remove(StatisticalValueType statisticalValueType) {
		String valueName = statisticalValueType.getName();
		if( values.containsKey(valueName))
			values.remove(valueName);

	}

	Hashtable<String,StatisticalValueSet> subsets = null;
	@Override
	public void addSubset(StatisticalValueSet vs) {
		if( subsets == null){
			subsets = new Hashtable<String,StatisticalValueSet>();
		}
		subsets.put(vs.getName(), vs);

	}

	@Override
	public Hashtable<String,StatisticalValueSet> getSubsets() {
		
		return subsets;
	}

	@Override
	public void merge(StatisticalValueSet otherSet) {
		for(StatisticalValueType t:getStatistics().getValueTypes().values()){
			if(t.operands == null)
				continue;
			boolean foundOperand = false;
			for(StatisticalValueType ot:t.operands){
				if(otherSet.contains(ot.getName())){
					foundOperand = true;
				}
			}
			if( !foundOperand )
				continue;
			StatisticalValue value = this.get(t);
			if(value == null){
				value = t.create();
				this.set(t.getName(),value);
			}
			t.compute(value,otherSet);
		}
	}

	@Override
	public void set(String name, StatisticalValue value) {
		values.put(name, value);
		
	}

	@Override
	public Hashtable<String, StatisticalValue> getValues() {
		// TODO Auto-generated method stub
		return null;
	}

}
