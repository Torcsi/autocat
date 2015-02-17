package com.arcanum.statistics;

import java.io.PrintStream;
import java.io.Writer;
import java.util.Enumeration;
import java.util.Hashtable;

public class StatisticsFactory {
	
	static Hashtable<String,Statistics> statistics=new Hashtable<String,Statistics>();
	enum Type{
		MEMORY,
		PERSISTENT
	};
	static Statistics create(String name,Type type){
		Statistics s;
		s = new Statistics_Memory(name);
		statistics.put(name,s);
		return s;
	}
	static Statistics get(String name)
	{
		if( statistics.containsKey(name))
			return statistics.get(name);
		return null;
	}
	static void write(PrintStream w) throws Exception
	{
		Enumeration<String> en = statistics.keys();
		while(en.hasMoreElements()){
			String k = en.nextElement();
			Statistics s = statistics.get(k);
			s.write(w);
		}
	}
}
