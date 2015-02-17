package com.arcanum.statistics;

import java.text.SimpleDateFormat;
import java.util.Date;

public class StatisticalValue {
	enum Type {
		BOOLEAN, LONG, DOUBLE, PERCENT, FLAG, DATETIME, DURATION, TIMESTAMP, TIME;
	}
	String name;
	double value;
	StatisticalValueType valueType;
	
	String xmlName=null;
	String getAttributeName()
	{
		if( xmlName == null)
			xmlName = XMLUtility.getXmlName(name);
		return xmlName;
	}
	StatisticalValue(String name, StatisticalValueType type,double initialValue)
	{
		this.name = name;
		this.value = initialValue;
		this.valueType = type;
	}
	StatisticalValue(StatisticalValueType type,double initialValue)
	{
		this.name = type.name;
		this.value = initialValue;
		this.valueType = type;
	}
	StatisticalValue(String name, StatisticalValueType type)
	{
		this.name = name;
		this.valueType = type;
		if( valueType.type == Type.TIMESTAMP || valueType.type == Type.DURATION || valueType.type==Type.TIME || valueType.type==Type.DATETIME)
			value = System.currentTimeMillis();
	}
	
	String getName()
	{
		return name;
	}
	public double getValue()
	{
		return value;
	}
	public long getValueLong()
	{
		return (long)value;
	}
	public boolean getValueBoolean()
	{
		return value != 0;
	}
	public double getPercent()
	{
		return value*100.0;
	}
	public void set()
	{
		if( valueType.type == Type.TIMESTAMP || valueType.type == Type.TIME ){
			value = System.currentTimeMillis();
		}else if(valueType.type == Type.BOOLEAN){
			value = 1;
		}
	}
	public void set(double d)
	{
		value = d;
	}
	public void set(long l)
	{
		value = l;
	}
	public void set(boolean b)
	{
		value = b?1:0;
	}
	public void inc()
	{
		value++;
	}
	public void dec()
	{
		value--;
	}
	public void add(double d)
	{
		value+=d;
	}
	public void add(long l)
	{
		value += l;
	}
	public void or(boolean b)
	{
		if( value == 0)
			value = b?1:0;
	}
	public void sub(double d)
	{
		value -=d;
	}
	public void sub(long l)
	{
		value -= l;
	}
	
	public void multiply(double d)
	{
		value *= d;
	}
	public void multiply(long l)
	{
		value *= l;
	}
	void divide(double d)
	{
		if( d!= 0)
			value /= d;
	}
	void divide(long l)
	{
		if( l!= 0)
			value /= l;
	}
	void and(boolean b)
	{
		if(value!=0){
			value=b?value:0;
		}
	}
	public int hashCode()
	{
		return name.hashCode();
	}
	public String toString()
	{
		return name + (valueType.type!=Type.FLAG?"=" + valueToString():"");
	}
	
	private final static double EPS=0.000001d;
	public String valueToString()
	{
		switch(valueType.type){
		case BOOLEAN:
			return (value==0?"false":"true");
		case LONG:
			return String.format("%d", Math.round(value));
		case DOUBLE:
			double diff = Math.abs(value-Math.round(value));
			if( diff >= EPS){
				String.format("$s", value);
			}
			return String.format("$d", value);
		case PERCENT:
			return String.format("%2.2f", value*100.0);
		case FLAG:
			return "+";
		case TIMESTAMP:
		case TIME:
			return timeToString((long)value);
		case DATETIME:
			return datetimeToString((long)value);
		case DURATION:
			return durationToString(System.currentTimeMillis() - (long)value);
			
		}
		return Double.toString(value);
	}
	String timeToString(long t)
	{
		SimpleDateFormat sd  = new SimpleDateFormat("hh:mm:ss");
		return sd.format(new Date(t));
	}
	String datetimeToString(long t)
	{
		SimpleDateFormat sd  = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		return sd.format(new Date(t));
	}
	String durationToString(long t)
	{
		SimpleDateFormat sd  = new SimpleDateFormat("hh:mm:ss:SSS");
		return sd.format(new Date(t));
	}
	
}
