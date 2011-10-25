package edu.cmu.cs.crystal.analysis.npe.annotations;

import edu.cmu.cs.crystal.simple.SimpleLatticeOperations;

/**
 * The lattice operations for a null lattice. As NullLatticeElement is an enum,
 * we can directly compare references and do not need to clone anything.
 * 
 * @author ciera
 *
 */
public class ArrayBoundsLatticeOperations extends SimpleLatticeOperations<ArrayBoundsLatticeElement> {

	@Override
	public boolean atLeastAsPrecise(ArrayBoundsLatticeElement left,
			ArrayBoundsLatticeElement right) {
		return left.contains(right);
	}

	@Override
	public ArrayBoundsLatticeElement bottom() {
		return ArrayBoundsLatticeElement.bottom();
	}

	@Override
	public ArrayBoundsLatticeElement copy(ArrayBoundsLatticeElement original) {
		return original.clone();
	}

	@Override
	public ArrayBoundsLatticeElement join(ArrayBoundsLatticeElement left,
			ArrayBoundsLatticeElement right) {
			return left.merge(right);
	}

}
