package edu.cmu.cs.crystal.analysis.npe.annotations;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.xml.validation.Validator;

import org.eclipse.jdt.core.dom.ASTNode;

import edu.cmu.cs.crystal.flow.IAbstractLatticeOperations;
import edu.cmu.cs.crystal.flow.ILatticeOperations;
import edu.cmu.cs.crystal.simple.TupleLatticeOperations;
import edu.cmu.cs.crystal.tac.model.Variable;

public class PairLatticeOperations implements IAbstractLatticeOperations<PairLatticeElement, ASTNode>, ILatticeOperations<PairLatticeElement> {
	
	TupleLatticeOperations<Variable, ArrayBoundsLatticeElement> ops = new TupleLatticeOperations<Variable, ArrayBoundsLatticeElement>(new ArrayBoundsLatticeOperations(), ArrayBoundsLatticeElement.bottom());
	
	public boolean atLeastAsPrecise(PairLatticeElement arg0,
			PairLatticeElement arg1, ASTNode arg2) {
		return ops.atLeastAsPrecise(arg0.values, arg1.values, arg2);		
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
		p.values = ops.join(arg0.values, arg1.values, arg2);
		p.arrayLenghts = new HashMap<Variable, Set<Variable>>();
		p.arrayLenghts.putAll(arg0.arrayLenghts);
		p.arrayLenghts.putAll(arg1.arrayLenghts);
		return p;
	}
	
	public PairLatticeElement getDefault() {
		return bottom();		
	}
	

}
