package edu.cmu.cs.crystal.analysis.npe.annotations;

import edu.cmu.cs.crystal.tac.model.BinaryOperator;


/**
 *
 */
public class ArrayBoundsLatticeElement {
		
	public enum LimitValue { SOME_VALUE, NEG_INFINITY, POS_INFINITY }
		
	public int min, max;
	public LimitValue lmin, lmax;
	
	public static ArrayBoundsLatticeElement bottom()	{
		ArrayBoundsLatticeElement ret = new ArrayBoundsLatticeElement(Integer.MAX_VALUE,Integer.MIN_VALUE);
		ret.lmin = LimitValue.POS_INFINITY;
		ret.lmax = LimitValue.NEG_INFINITY;
		return ret;		
	}

	public static ArrayBoundsLatticeElement top()	{
		ArrayBoundsLatticeElement ret = new ArrayBoundsLatticeElement(Integer.MIN_VALUE,Integer.MAX_VALUE);
		ret.lmin = LimitValue.NEG_INFINITY;
		ret.lmax = LimitValue.POS_INFINITY;
		return ret;		
	}

	// Both must be non-bottom
	private boolean rightIncludes(ArrayBoundsLatticeElement other) {
		if (lmax == LimitValue.POS_INFINITY)
			return true;

		if (other.lmax == LimitValue.POS_INFINITY)
			return lmax == LimitValue.POS_INFINITY;

		return other.max <= max;		
	}

	// Both must be non-bottom
	private boolean leftIncludes(ArrayBoundsLatticeElement other) {
		if (lmax == LimitValue.NEG_INFINITY)
			return true;

		if (other.lmin == LimitValue.NEG_INFINITY)
			return lmin == LimitValue.NEG_INFINITY;

		return other.min >= min;
	}

	public ArrayBoundsLatticeElement(int Min, int Max) {
		min = Min;
		max = Max;
		lmin = lmax = LimitValue.SOME_VALUE;
	}
	
	public boolean isBottom() {
		return lmin == LimitValue.POS_INFINITY && lmax == LimitValue.NEG_INFINITY; 
	}

	public boolean isTop() {
		return lmin == LimitValue.NEG_INFINITY && lmax == LimitValue.POS_INFINITY; 
	}

	public boolean contains(ArrayBoundsLatticeElement other) {
		
		if (isBottom())
			return other.isBottom();
		
		if (other.isBottom())
			return true;			
		
		return leftIncludes(other) && rightIncludes(other);
	}
	
	// Junta dos intervalos en uno que contenga a ambos
	public ArrayBoundsLatticeElement merge(ArrayBoundsLatticeElement other) {
		if (isBottom() || other.isTop())
			return other.clone();
		
		if (other.isBottom() || isTop())
			return clone();
			
		ArrayBoundsLatticeElement ret = new ArrayBoundsLatticeElement(Math.min(min, other.min), Math.max(max, other.max));
		ret.lmin = lmin == LimitValue.NEG_INFINITY || other.lmin == LimitValue.NEG_INFINITY ? LimitValue.NEG_INFINITY : LimitValue.SOME_VALUE;
		ret.lmax = lmax == LimitValue.POS_INFINITY || other.lmax == LimitValue.POS_INFINITY ? LimitValue.POS_INFINITY : LimitValue.SOME_VALUE;
		
		return ret;		
	}
	
	// Suma dos intervalos
	public ArrayBoundsLatticeElement add(ArrayBoundsLatticeElement other) {
		if (isBottom() || other.isTop())
			return other.clone();
		
		if (other.isBottom() || isTop())
			return clone();
				
		int minValue = min == Integer.MIN_VALUE || other.min == Integer.MIN_VALUE ? Integer.MIN_VALUE : min + other.min;
		int maxValue = max == Integer.MAX_VALUE || other.max == Integer.MAX_VALUE ? Integer.MAX_VALUE : max + other.max;
			
		ArrayBoundsLatticeElement ret = new ArrayBoundsLatticeElement(minValue, maxValue);
		ret.lmin = lmin == LimitValue.NEG_INFINITY || other.lmin == LimitValue.NEG_INFINITY ? LimitValue.NEG_INFINITY : LimitValue.SOME_VALUE;
		ret.lmax = lmax == LimitValue.POS_INFINITY || other.lmax == LimitValue.POS_INFINITY ? LimitValue.POS_INFINITY : LimitValue.SOME_VALUE;
		
		return ret;
	}

	// Resta dos intervalos
	public ArrayBoundsLatticeElement substract(ArrayBoundsLatticeElement other) {
		if (isBottom() || isTop() || other.isBottom())
			return clone();
		
		if (other.isTop())
			return other.clone();
		
		int minValue = min == Integer.MIN_VALUE || other.max == Integer.MAX_VALUE ? Integer.MIN_VALUE : min - other.max;
		int maxValue = max == Integer.MAX_VALUE || other.min == Integer.MIN_VALUE ? Integer.MAX_VALUE : max - other.min;
						
		ArrayBoundsLatticeElement ret = new ArrayBoundsLatticeElement(minValue, maxValue);
		ret.lmin = lmin == LimitValue.NEG_INFINITY || other.lmax == LimitValue.POS_INFINITY ? LimitValue.NEG_INFINITY : LimitValue.SOME_VALUE;
		ret.lmax = lmax == LimitValue.POS_INFINITY || other.lmin == LimitValue.NEG_INFINITY ? LimitValue.POS_INFINITY : LimitValue.SOME_VALUE;
		
		return ret;
	}

	// Multiplica dos intervalos
	public ArrayBoundsLatticeElement multiply(ArrayBoundsLatticeElement other) {
		if (isBottom() || other.isTop())
			return other.clone();
		
		if (other.isBottom() || isTop())
			return clone();
		
		int aux1 = min == Integer.MIN_VALUE || other.min == Integer.MIN_VALUE ? Integer.MIN_VALUE : min * other.min;
		int aux2 = min == Integer.MIN_VALUE || other.max == Integer.MAX_VALUE ? Integer.MIN_VALUE : min * other.max;
		int aux3 = max == Integer.MAX_VALUE || other.min == Integer.MIN_VALUE ? Integer.MAX_VALUE : max * other.min;
		int aux4 = max == Integer.MAX_VALUE || other.max == Integer.MAX_VALUE ? Integer.MAX_VALUE : max * other.max;
		
		int minValue = Math.min(aux1, Math.min(aux2, Math.min(aux3, aux4)));
		int maxValue = Math.max(aux1, Math.max(aux2, Math.max(aux3, aux4)));
		
		ArrayBoundsLatticeElement ret = new ArrayBoundsLatticeElement(minValue, maxValue);
		ret.lmin = minValue == Integer.MIN_VALUE ? LimitValue.NEG_INFINITY : LimitValue.SOME_VALUE;
		ret.lmax = maxValue == Integer.MAX_VALUE ? LimitValue.POS_INFINITY : LimitValue.SOME_VALUE;
		
		return ret;
	}

	public boolean equals(ArrayBoundsLatticeElement other) {
		return this.contains(other) && other.contains(this);	
	}
	
	public ArrayBoundsLatticeElement clone() {
		ArrayBoundsLatticeElement ret = new ArrayBoundsLatticeElement(min,max);
		ret.lmin = lmin;
		ret.lmax = lmax;
		
		return ret;
	}
	
	public String toString(){
		if (isBottom())
			return "[]";
		
		return "[ "+
		(lmin == LimitValue.SOME_VALUE? min : "-INF") + " , " + 
		(lmax == LimitValue.SOME_VALUE? max : "INF" ) + " ]";
	}
	
	public ArrayBoundsLatticeElement getInterval(BinaryOperator op, ArrayBoundsLatticeElement than)
	{		
		if (than.isBottom() && op == BinaryOperator.REL_EQ)
			return top();
			
		ArrayBoundsLatticeElement ret = than.clone();
		switch(op)
		{
			case REL_EQ:				
				break;
				
			case REL_GEQ:
				ret.lmax = LimitValue.POS_INFINITY;
				break;

			case REL_LEQ:
				ret.lmin = LimitValue.NEG_INFINITY;
				break;

			case REL_GT:
				if (than.lmin == LimitValue.SOME_VALUE)
					ret.min += 1;
				ret.lmax = LimitValue.POS_INFINITY;
				break;

			case REL_LT:
				if (than.lmin == LimitValue.SOME_VALUE)
					ret.max -= 1;
				ret.lmin = LimitValue.NEG_INFINITY;
				break;
		}
		return ret;
	}
	
};

