package com.arcanum.statistics;

import java.util.Hashtable;
import java.util.Vector;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public interface StatisticalValueSet {
	String getName();
	Statistics getStatistics();
	
	boolean contains(String valueName);
	
	StatisticalValue set(StatisticalValueType t, double v);
	void set(String name, StatisticalValue value);
	
	default StatisticalValue set(String name, boolean value)
	{
		return set(getStatistics().getType(name),value?1:0);
	}
	default StatisticalValue set(String name, double value)
	{
		return set(getStatistics().getType(name),value);
	}
	default StatisticalValue set(String name, long value)
	{
		return set(getStatistics().getType(name),(double)value);
	}
	default StatisticalValue set(String name)
	{
		return set(getStatistics().getType(name),0);
	}
	void merge(StatisticalValueSet otherSet);
	
	
	
	default StatisticalValue compute(String valueName)
	{
		StatisticalValueType t = getStatistics().getType(valueName);
		if( t == null)
			return null;
		return t.compute(this);
	}
	
	StatisticalValue get(String valueName);
	default StatisticalValue get(StatisticalValueType t)
	{
		return get(t.getName());
	}
	
	default double getValue(String valueName)
	{
		compute(valueName);
		return get(valueName).getValue();
	}
	default long getValueLong(String valueName)
	{
		compute(valueName);
		return get(valueName).getValueLong();
	}
	default boolean getValueBoolean(String valueName)
	{
		compute(valueName);
		return get(valueName).getValueBoolean();
	}
	default double getPercent(String valueName)
	{
		compute(valueName);
		return get(valueName).getPercent();
	}
	
	default void inc(String valueName)
	{
		get(valueName).inc();
	}
	default void dec(String valueName)
	{
		get(valueName).dec();
	}
	default void add(String valueName,double d)
	{
		get(valueName).add(d);
	}
	default void add(String valueName,long l)
	{
		get(valueName).add(l);
	}
	default void or(String valueName,boolean b)
	{
		get(valueName).or(b);
	}
	default void sub(String valueName,double d)
	{
		get(valueName).sub(d);
	}
	default void sub(String valueName,long l)
	{
		get(valueName).sub(l);
	}
	
	default void multiply(String valueName,double d)
	{
		get(valueName).multiply(d);

	}
	default void multiply(String valueName,long l)
	{
		get(valueName).multiply(l);
	}
	default void divide(String valueName,double d)
	{
		get(valueName).divide(d);
	}
	default void divide(String valueName,long l)
	{
		get(valueName).divide(l);
	}
	default void and(String valueName,boolean b)
	{
		get(valueName).and(b);
	}
	
	default String toString(String valueName)
	{
		compute(valueName);
		return get(valueName).toString();
	}

	void remove(StatisticalValueType statisticalValueType);

	void addSubset(StatisticalValueSet vs);
	Hashtable<String,StatisticalValueSet> getSubsets();

	Hashtable<String,StatisticalValue> getValues();
	
	default String getElementName()
	{
		return XMLUtility.getXmlName(getName());
	}
	default void addValues(Element e){
		// get respective element
		Element parent=null;
		NodeList valueSets = e.getChildNodes();
		
		for(int i=0;i<valueSets.getLength();i++){
			Node n = valueSets.item(i);
			if( n.getNodeType() != Node.ELEMENT_NODE)
				continue;
			if( n.getNodeName().equals(getElementName())){
				parent = (Element)n;
				break;
			}
		}
		if( parent == null){
			Document d = e.getOwnerDocument();
			parent = d.createElement("valueSet");
			parent.setAttribute("name",getElementName());
		}
		Hashtable<String,StatisticalValue> h = getValues();
		for(StatisticalValue sv: h.values()){
			String k = sv.getAttributeName();
			String v = sv.toString();
			parent.setAttribute(k,v);
		}
	}
	
}
