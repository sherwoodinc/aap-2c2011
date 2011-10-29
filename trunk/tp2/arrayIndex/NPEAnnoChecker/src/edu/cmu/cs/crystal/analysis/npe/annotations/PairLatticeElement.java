package edu.cmu.cs.crystal.analysis.npe.annotations;

import java.util.Map;
import java.util.Set;

import edu.cmu.cs.crystal.simple.TupleLatticeElement;
import edu.cmu.cs.crystal.tac.model.Variable;

public class PairLatticeElement {
	TupleLatticeElement<Variable, ArrayBoundsLatticeElement> values;
	Map<Variable, Set<Variable> > arrayLenghts;
}
