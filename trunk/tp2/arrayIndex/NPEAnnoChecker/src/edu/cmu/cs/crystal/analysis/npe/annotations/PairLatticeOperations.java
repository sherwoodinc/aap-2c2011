package edu.cmu.cs.crystal.analysis.npe.annotations;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.xml.validation.Validator;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ForStatement;
import org.eclipse.jdt.core.dom.WhileStatement;

import edu.cmu.cs.crystal.flow.IAbstractLatticeOperations;
import edu.cmu.cs.crystal.flow.ILatticeOperations;
import edu.cmu.cs.crystal.simple.TupleLatticeElement;
import edu.cmu.cs.crystal.simple.TupleLatticeOperations;
import edu.cmu.cs.crystal.tac.model.Variable;

/*
 * Extensión de TupleLatticeElement para guardar un par de maps extra con info necesaria. 
 */
public class PairLatticeOperations implements IAbstractLatticeOperations<PairLatticeElement, ASTNode>, ILatticeOperations<PairLatticeElement> {
	
	TupleLatticeOperations<Variable, Interval> ops = new TupleLatticeOperations<Variable, Interval>(new ArrayBoundsLatticeOperations(), Interval.empty());
	Map<ASTNode, Integer> iterCounts = new HashMap<ASTNode, Integer>();
	
	public boolean atLeastAsPrecise(PairLatticeElement arg0,
			PairLatticeElement arg1, ASTNode arg2) {
		boolean result = ops.atLeastAsPrecise(arg0.values, arg1.values, arg2);
		return result;
	}

	public PairLatticeElement bottom() {
		// TODO Auto-generated method stub
		PairLatticeElement p = new PairLatticeElement();
		p.values = ops.getDefault();
		p.arrayLenghts = new HashMap<Variable, Set<Variable>>();		
		return p;
	}

	public PairLatticeElement copy(PairLatticeElement arg0) {
		// TODO Auto-generated method stub
		PairLatticeElement p = new PairLatticeElement();
		p.values = ops.copy(arg0.values);
		p.arrayLenghts = new HashMap<Variable, Set<Variable>>();
		p.arrayLenghts.putAll(arg0.arrayLenghts);
		return p;
	}

	public PairLatticeElement join(PairLatticeElement arg0,
			PairLatticeElement arg1, ASTNode arg2) {
		// TODO Auto-generated method stub
		PairLatticeElement p = new PairLatticeElement();
		p.arrayLenghts = new HashMap<Variable, Set<Variable>>();
		p.arrayLenghts.putAll(arg0.arrayLenghts);
		p.arrayLenghts.putAll(arg1.arrayLenghts);
		p.values = ops.join(arg0.values, arg1.values, arg2);
		
		// Acá comprobamos si hacer widening o no.
		if (arg2 instanceof ForStatement || arg2 instanceof WhileStatement) {
			if (!iterCounts.containsKey(arg2))
				iterCounts.put(arg2, 1);
			else
				iterCounts.put(arg2, iterCounts.get(arg2)+1);
			
			if (iterCounts.get(arg2) > 1000)
				widen(p.values,arg0.values,arg1.values);
		}
		return p;
	}
	
	private void widen(TupleLatticeElement<Variable, Interval> valuesToWiden,
			TupleLatticeElement<Variable, Interval> valuesIzq,
			TupleLatticeElement<Variable, Interval> valuesDer) {
		for (Variable v: valuesIzq.getKeySet()) {
			Interval i1, i2;
			i1 = valuesIzq.get(v);
			i2 = valuesDer.get(v);
			if (!i1.equals(i2))
			{
				valuesToWiden.put(v, i1.widen(i2));				
			}
		}
	}

	public PairLatticeElement getDefault() {
		return bottom();		
	}
	

}
