package edu.cmu.cs.crystal.analysis.npe.annotations;

import edu.cmu.cs.crystal.tac.model.BinaryOperator;


/**
 *
 */
public class Interval {
		
	public enum Limit { VAL, NINF, PINF }
		
	private int min, max;
	private Limit lmin, lmax;

	/// Constructores
	
	public static Interval empty()	{
		Interval ret = new Interval(Integer.MAX_VALUE,Integer.MIN_VALUE);
		ret.lmin = Limit.PINF;
		ret.lmax = Limit.NINF;
		return ret;		
	}

	public static Interval all()	{
		Interval ret = new Interval(Integer.MIN_VALUE,Integer.MAX_VALUE);
		ret.lmin = Limit.NINF;
		ret.lmax = Limit.PINF;
		return ret;		
	}
	
	public static Interval upTo(int Max)	{
		Interval ret = new Interval(Integer.MIN_VALUE,Max);
		ret.lmin = Limit.NINF;
		return ret;		
	}

	public static Interval from(int Min)	{
		Interval ret = new Interval(Min,Integer.MAX_VALUE);
		ret.lmax = Limit.PINF;
		return ret;		
	}

	public Interval(int Min, int Max) {
		if (Min <= Max) {
			min = Min;
			max = Max;
		}
		else {
			min = Max;
			max = Min;			
		}
		lmin = lmax = Limit.VAL;
	}

	public Interval clone() {
		Interval ret = new Interval(min,max);
		ret.lmin = lmin;
		ret.lmax = lmax;
		return ret;
	}
	
	public void assign(Interval other)
	{
		min = other.min;
		max = other.max;
		lmin = other.lmin;
		lmax = other.lmax;
	}

	/// Observadores
	
	public boolean isEmpty() {
		return lmin == Limit.PINF && lmax == Limit.NINF; 
	}

	public boolean isAll() {
		return lmin == Limit.NINF && lmax == Limit.PINF; 
	}

	public boolean contains(Interval other) {
		
		if (isEmpty())
			return other.isEmpty();
		
		if (other.isEmpty())
			return true;			
		
		return leftIncludes(other) && rightIncludes(other);
	}
	
	public boolean overlaps(Interval other) {
		
		if (isEmpty())
			return false;
		
		if (other.isEmpty())
			return true;			
		
		return leftIncludes(other) || rightIncludes(other) || other.rightIncludes(this) || other.leftIncludes(this);
	}
	
	public boolean equals(Interval other) {
		return this.contains(other) && other.contains(this);	
	}

	public String toString(){
		if (isEmpty())
			return "[]";
		
		return "[ "+
		(lmin == Limit.VAL? min : "-INF") + " , " + 
		(lmax == Limit.VAL? max : "INF" ) + " ]";
	}

	/// Operaciones algebraicas y de conjuntos
	
	// Diferencia de intervalos (A - A interseccion B)
	// Si el resultado son dos intervalos, devuelvo uno de los dos
	public Interval difference(Interval other)
	{
		if (other.contains(this))
			return empty();
	
		Interval intsec = intersect(other);
		if (intsec.isEmpty())
			return clone();
		
		if (!intsec.get_LT().isEmpty())
			return intsec.get_LT();

		if (!intsec.get_GT().isEmpty())
			return intsec.get_GT();
		
		return empty();
	}	

	// Intersección de intervalos
	public Interval intersect(Interval other)
	{
		if (isEmpty() || other.isEmpty() || !overlaps(other))
			return empty();
				
		Interval ret = new Interval(Math.max(min, other.min), Math.min(max, other.max));
		ret.lmin = lmin == Limit.NINF && other.lmin == Limit.NINF ? Limit.NINF : Limit.VAL;
		ret.lmax = lmax == Limit.PINF && other.lmax == Limit.PINF ? Limit.PINF : Limit.VAL;
		
		return ret;
	}	
	
	// Union de intervalos
	public Interval merge(Interval other) {
		if (isEmpty() || other.isAll())
			return other.clone();
		
		if (other.isEmpty() || isAll())
			return clone();
			
		Interval ret = new Interval(Math.min(min, other.min), Math.max(max, other.max));
		ret.lmin = lmin == Limit.NINF || other.lmin == Limit.NINF ? Limit.NINF : Limit.VAL;
		ret.lmax = lmax == Limit.PINF || other.lmax == Limit.PINF ? Limit.PINF : Limit.VAL;
		
		return ret;		
	}
	
	// Suma dos intervalos
	public Interval add(Interval other) {
		if (isEmpty() || other.isAll())
			return other.clone();
		
		if (other.isEmpty() || isAll())
			return clone();
				
		int minValue = min == Integer.MIN_VALUE || other.min == Integer.MIN_VALUE ? Integer.MIN_VALUE : min + other.min;
		int maxValue = max == Integer.MAX_VALUE || other.max == Integer.MAX_VALUE ? Integer.MAX_VALUE : max + other.max;
			
		Interval ret = new Interval(minValue, maxValue);
		ret.lmin = lmin == Limit.NINF || other.lmin == Limit.NINF ? Limit.NINF : Limit.VAL;
		ret.lmax = lmax == Limit.PINF || other.lmax == Limit.PINF ? Limit.PINF : Limit.VAL;
		
		return ret;
	}

	// Resta dos intervalos
	public Interval substract(Interval other) {
		if (isEmpty() || isAll() || other.isEmpty())
			return clone();
		
		if (other.isAll())
			return other.clone();
		
		int minValue = min == Integer.MIN_VALUE || other.max == Integer.MAX_VALUE ? Integer.MIN_VALUE : min - other.max;
		int maxValue = max == Integer.MAX_VALUE || other.min == Integer.MIN_VALUE ? Integer.MAX_VALUE : max - other.min;
						
		Interval ret = new Interval(minValue, maxValue);
		ret.lmin = lmin == Limit.NINF || other.lmax == Limit.PINF ? Limit.NINF : Limit.VAL;
		ret.lmax = lmax == Limit.PINF || other.lmin == Limit.NINF ? Limit.PINF : Limit.VAL;
		
		return ret;
	}

	// Multiplica dos intervalos
	public Interval multiply(Interval other) {
		if (isEmpty() || other.isAll())
			return other.clone();
		
		if (other.isEmpty() || isAll())
			return clone();
		
		int aux1 = min == Integer.MIN_VALUE || other.min == Integer.MIN_VALUE ? Integer.MIN_VALUE : min * other.min;
		int aux2 = min == Integer.MIN_VALUE || other.max == Integer.MAX_VALUE ? Integer.MIN_VALUE : min * other.max;
		int aux3 = max == Integer.MAX_VALUE || other.min == Integer.MIN_VALUE ? Integer.MAX_VALUE : max * other.min;
		int aux4 = max == Integer.MAX_VALUE || other.max == Integer.MAX_VALUE ? Integer.MAX_VALUE : max * other.max;
		
		int minValue = Math.min(aux1, Math.min(aux2, Math.min(aux3, aux4)));
		int maxValue = Math.max(aux1, Math.max(aux2, Math.max(aux3, aux4)));
		
		Interval ret = new Interval(minValue, maxValue);
		ret.lmin = minValue == Integer.MIN_VALUE ? Limit.NINF : Limit.VAL;
		ret.lmax = maxValue == Integer.MAX_VALUE ? Limit.PINF : Limit.VAL;
		
		return ret;
	}
		
	public Interval negate() {
		if (isAll() || isEmpty())
			return clone();
		
		Interval ret = new Interval(-max, -min);
		if (lmin == Limit.NINF) ret.lmax = Limit.PINF;
		if (lmax == Limit.PINF) ret.lmin = Limit.NINF;
		
		return ret;
	}

	public static Interval[] getIntervals(BinaryOperator op, Interval op0, Interval op1)
	{	
		// out 0-1 son los intervalos resultantes cuando la comparación es true
		// out 2-3 son los intervalos resultantes cuando la comparación es false
		Interval[] out = new Interval[4];
		out[0] = op0.clone();
		out[1] = op1.clone();
		out[2] = op0.clone();
		out[3] = op1.clone();
		getIntervalsAux(op, op0, op1, out[0], out[1]);
		getIntervalsAux(negate(op), op0, op1, out[2], out[3]);
		return out;
	}
	
	private static BinaryOperator negate(BinaryOperator op) {
		switch(op)
		{
		case REL_NEQ:
			return BinaryOperator.REL_EQ;
			
		case REL_EQ:
			return BinaryOperator.REL_NEQ;
			
		case REL_GEQ:
			return BinaryOperator.REL_LT;

		case REL_LEQ:
			return BinaryOperator.REL_GT;

		case REL_GT:
			return BinaryOperator.REL_LEQ;

		case REL_LT:
			return BinaryOperator.REL_GEQ;
		}
		return op;
	}

	private static void getIntervalsAux(BinaryOperator op, Interval op0, Interval op1, Interval out0, Interval out1)	{
		switch(op)
		{
			case REL_NEQ:
				out0.assign(op0.difference(op1));
				out1.assign(op1.difference(op0));
				break;
				
			case REL_EQ:
				// iguales
				out0.assign(op0.intersect(op1));
				out1.assign(out0);
				break;
				
			case REL_GEQ:
				out0.assign(op0.intersect(op1.merge(op1.get_GT())));
				out1.assign(op1.intersect(op0.get_LT()));
				break;

			case REL_LEQ:
				out0.assign(op0.intersect(op1.merge(op1.get_LT())));
				out1.assign(op1.intersect(op0.get_GT()));
				break;

			case REL_GT:
				out0.assign(op0.intersect(op1.get_GT()));
				out1.assign(op1.intersect(op0.merge(op0.get_LT())));
				break;

			case REL_LT:
				out0.assign(op0.intersect(op1.get_LT()));
				out1.assign(op1.intersect(op0.merge(op0.get_GT())));
				break;
		}
		
	}

	// Both must be non-bottom
	private boolean rightIncludes(Interval other) {
		if (isAll())
			return true;

		if (lmax == Limit.PINF)
			return other.lmax == Limit.PINF || other.max >= min;
		
		if (other.lmax == Limit.PINF)
			return false;
			
		// Both are defined intervals
		return other.max <= max && (lmin == Limit.NINF || (other.max >= min));		
	}

	// Both must be non-bottom
	private boolean leftIncludes(Interval other) {
		if (isAll())
			return true;

		if (lmin == Limit.NINF)
			return other.lmin == Limit.NINF || other.min <= max;
		
		if (other.lmin == Limit.NINF)
			return false;
			
		// Both are defined intervals
		return other.min >= min && (lmax == Limit.PINF || other.min <= max);	}

	private Interval get_LT() {
		if (isEmpty())
			return all();
		
		if (isAll() || lmin == Limit.NINF)
			return empty();
	
		return upTo(min-1);		
	}

	private Interval get_GT() {
		if (isEmpty())
			return all();
		
		if (isAll() || lmax == Limit.PINF)
			return empty();
	
		return from(max+1);		
	}

	public Interval widen(Interval i2) {
		Interval res = clone();
		if (min < i2.min && max < i2.max)
			res.lmax = Limit.PINF;
		else if (min > i2.min && max > i2.max)
			res.lmin = Limit.NINF;
		
		return res;
	}

};

