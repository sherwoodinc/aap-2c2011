package edu.cmu.cs.crystal.analysis.npe.annotations;

import java.util.ArrayList;


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
		if (isBottom())
			return other.clone();
		
		if (other.isBottom())
			return clone();
		
		ArrayBoundsLatticeElement ret = new ArrayBoundsLatticeElement(Math.min(min, other.min), Math.max(max,other.max));
		ret.lmin = lmin == LimitValue.NEG_INFINITY || other.lmin == LimitValue.NEG_INFINITY? LimitValue.NEG_INFINITY : LimitValue.SOME_VALUE;
		ret.lmax = lmax == LimitValue.POS_INFINITY || other.lmax == LimitValue.POS_INFINITY? LimitValue.POS_INFINITY : other.lmax;
			
		return ret;
	}

	// Junta dos intervalos en uno que contenga a ambos
	public ArrayBoundsLatticeElement add(ArrayBoundsLatticeElement other) {
		if (isBottom())
			return other.clone();
		
		if (other.isBottom())
			return clone();
		
		ArrayBoundsLatticeElement ret = new ArrayBoundsLatticeElement(Math.min(min, other.min), Math.max(max,other.max));
		ret.lmin = lmin == LimitValue.NEG_INFINITY || other.lmin == LimitValue.NEG_INFINITY? LimitValue.NEG_INFINITY : LimitValue.SOME_VALUE;
		ret.lmax = lmax == LimitValue.POS_INFINITY || other.lmax == LimitValue.POS_INFINITY? LimitValue.POS_INFINITY : other.lmax;
			
		return ret;
	}

	// Junta dos intervalos en uno que contenga a ambos
	public ArrayBoundsLatticeElement substract(ArrayBoundsLatticeElement other) {
		if (isBottom())
			return other.clone();
		
		if (other.isBottom())
			return clone();
		
		ArrayBoundsLatticeElement ret = new ArrayBoundsLatticeElement(Math.min(min, other.min), Math.max(max,other.max));
		ret.lmin = lmin == LimitValue.NEG_INFINITY || other.lmin == LimitValue.NEG_INFINITY? LimitValue.NEG_INFINITY : LimitValue.SOME_VALUE;
		ret.lmax = lmax == LimitValue.POS_INFINITY || other.lmax == LimitValue.POS_INFINITY? LimitValue.POS_INFINITY : other.lmax;
			
		return ret;
	}

	// Junta dos intervalos en uno que contenga a ambos
	public ArrayBoundsLatticeElement multiply(ArrayBoundsLatticeElement other) {
		if (isBottom())
			return other.clone();
		
		if (other.isBottom())
			return clone();
		
		ArrayBoundsLatticeElement ret = new ArrayBoundsLatticeElement(Math.min(min, other.min), Math.max(max,other.max));
		ret.lmin = lmin == LimitValue.NEG_INFINITY || other.lmin == LimitValue.NEG_INFINITY? LimitValue.NEG_INFINITY : LimitValue.SOME_VALUE;
		ret.lmax = lmax == LimitValue.POS_INFINITY || other.lmax == LimitValue.POS_INFINITY? LimitValue.POS_INFINITY : other.lmax;
			
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
};

