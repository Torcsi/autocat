package com.arcanum.statistics;

import java.util.Hashtable;
import java.util.Vector;

public class StatisticalValueType {
	

	enum Aggregator {
		MIN, MAX, AVERAGE, SUM, COUNT
	};

	enum Operator {
		FIRST, ADD, SUB, MUL, DIV, AND, OR, NOT
	};

	enum ComputationType {
		STORED, AGGREGATED, OPERATION
	};

	StatisticalValue.Type type;
	String name;

	Aggregator aggregator;
	Operator operator;
	ComputationType computationType;

	long duration;

	StatisticalValueType operand1 = null;
	StatisticalValueType[] operands = null;
	static final long PERSISTENT = -1;
	static final long DEFAULT_DURATION = 60 * 1000; // 1 minute

	public StatisticalValueType(String name, StatisticalValue.Type type) {
		this.name = name;
		this.computationType = ComputationType.STORED;
		this.type = type;
		this.duration = DEFAULT_DURATION;
	}

	public StatisticalValueType(String name, StatisticalValue.Type type, long duration) {
		this.name = name;
		this.computationType = ComputationType.STORED;
		this.type = type;
		this.duration = duration;
	}

	public StatisticalValueType(String name, StatisticalValue.Type type, long duration,
			Aggregator aggregator, StatisticalValueType operand,StatisticalValueType... operands) {
		this.name = name;
		this.computationType = ComputationType.AGGREGATED;
		this.aggregator = aggregator;
		this.type = type;
		this.duration = duration;
		this.operand1 = operand;
		this.operands = operands;
	}

	public StatisticalValueType(String name, StatisticalValue.Type type, long duration,
			StatisticalValueType operand1, Operator operationType,
			StatisticalValueType... operands) {
		this.name = name;
		this.computationType = ComputationType.OPERATION;
		this.operator = operationType;
		this.type = type;
		this.duration = duration;
		this.operand1 = operand1;
		this.operands = operands;
	}

	String getName() {
		return name;
	}

	StatisticalValue create()
	{
		return new StatisticalValue(name,this);
	}
	StatisticalValue compute(StatisticalValueSet vs) {
		switch (computationType) {
		case OPERATION:
			return computeOperation(vs);
		case AGGREGATED:
			return computeAggregation(vs);
		default:
			return vs.get(name);
		}
	}
	void compute(StatisticalValue v, StatisticalValueSet vs)
	{
		switch (computationType) {
		case OPERATION:
			computeOperation(v,vs);
		case AGGREGATED:
			computeAggregation(1,v.getValue(),vs);
		default:
			v.set(vs.getValue(v.getName()));
		}
	}
	
	StatisticalValue computeOperation(StatisticalValueSet vs) {
		StatisticalValue v = vs.get(operand1.name);
		return computeOperation(v,vs);
	}
	
	
	StatisticalValue computeOperation(StatisticalValue v,StatisticalValueSet vs) {
		double value;
		boolean bAny;
		value = v == null ? 0 : v.getValue();
		bAny = v == null ? false : true;
		switch (operator) {
		case FIRST:
			if( bAny ){
				v = vs.set(this,value);
			}else{
				for (StatisticalValueType t : operands) {
					v = vs.get(t.name);
					if (v != null) {
						bAny = true;
						v = vs.set(this,v.getValue());
						break;
					}
				}
			}
			return bAny ? v : vs.get(this);
		case ADD:
			for (StatisticalValueType t : operands) {
				v = vs.get(t.name);
				if (v != null) {
					bAny = true;
					value += v.getValue();
				}
			}
			if (!bAny) {
				vs.remove(this);
				v = null;
			} else {
				v = vs.set(this, value);
			}
			break;
		case SUB:
			for (StatisticalValueType t : operands) {
				v = vs.get(t.name);
				if (v != null) {
					bAny = true;
					value -= v.getValue();
				}
			}
			if (!bAny) {
				vs.remove(this);
				v = null;
			} else {
				v = vs.set(this, value);
			}
			break;

		case MUL:
			if (bAny) {
				for (StatisticalValueType t : operands) {
					v = vs.get(t.name);
					if (v == null) {
						bAny = false;
						break;
					}
					value *= v.getValue();
				}
			}
			if (!bAny) {
				vs.remove(this);
				v = null;
			} else {
				v = vs.set(this, value);
			}
			break;
		case DIV:
			if (bAny) {
				for (StatisticalValueType t : operands) {
					v = vs.get(t.name);
					if (v == null) {
						bAny = false;
						break;
					}
					value /= v.getValue();
				}
			}
			if (!bAny) {
				vs.remove(this);
				v = null;
			} else {
				v = vs.set(this, value);
			}
			break;
		case AND:
			if (bAny) {
				if (value == 0) {
					bAny = false;
				} else {
					for (StatisticalValueType t : operands) {
						v = vs.get(t.name);
						if (v == null || v.getValue() == 0) {
							bAny = false;
							break;
						}
					}
				}
			}
			if (!bAny) {
				vs.remove(this);
				v = null;
			} else {
				v = vs.set(this, value);
			}
			break;
		case OR:
			if (!bAny) {
				if (value != 0) {
					bAny = true;
				} else {
					for (StatisticalValueType t : operands) {
						v = vs.get(t.name);
						if (v != null && v.getValue() != 0) {
							bAny = true;
							break;
						}
					}
				}
			}
			if (!bAny) {
				vs.remove(this);
				v = null;
			} else {
				v = vs.set(this, 1);
			}
			break;
		case NOT:
			if (bAny) {
				if (value == 0) {
					bAny = false;
				} else {
					for (StatisticalValueType t : operands) {
						v = vs.get(t.name);
						if (v != null && v.getValue() != 0) {
							bAny = false;
							break;
						}
					}
				}
				if (!bAny) {
					vs.remove(this);
					v = null;
				} else {
					v = vs.set(this, 1);
				}
				break;
			}
		}
		return v;

	}
	StatisticalValue computeAggregation(StatisticalValueSet vs) {
		return computeAggregation(0,0.0,vs);
	}
	StatisticalValue computeAggregation(int count, double value, StatisticalValueSet vs) {
		Hashtable<String,StatisticalValueSet> subsets = vs.getSubsets();
		for(StatisticalValueSet subset:subsets.values()){
			StatisticalValue v = subset.get(operand1.name);
			if( v != null){
				if( count == 0 ){
					value = v.getValue();
				}else{
					value = aggregateValue(value,v.getValue());
				}
				count++;
			}
			for(StatisticalValueType t:operands){
				v = subset.get(t.name);
				if( v != null){
					if( count == 0 ){
						value = v.getValue();
					}else{
						value = aggregateValue(value,v.getValue());
					}
					count++;
				}
			}
		}
		if( count == 0){
			vs.remove(this);
			return null;
		}
		switch(aggregator){
		case COUNT:
			value = count;
			break;
		case AVERAGE:
			value /= count;
			break;
		case MIN:
		case MAX:
		case SUM:
			break;
		}
		return vs.set(this, value);
	}
	double aggregateValue(double v1, double v2)
	{
		switch(aggregator){
		case MIN:
			return v1<v2?v1:v2;
		case MAX:
			return v1>v2?v1:v2;
		default:
			return v1+v2;
		}
	}
}
