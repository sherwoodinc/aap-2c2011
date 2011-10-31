package edu.cmu.cs.crystal.analysis.npe.annotations;

import edu.cmu.cs.crystal.simple.SimpleLatticeOperations;

/**
 * The lattice operations for a null lattice. As NullLatticeElement is an enum,
 * we can directly compare references and do not need to clone anything.
 * 
 * @author ciera
 *
 */
public class ArrayBoundsLatticeOperations extends SimpleLatticeOperations<Interval> {

	@Override
	public boolean atLeastAsPrecise(Interval left,
			Interval right) {
		return right.contains(left);
	}

	@Override
	public Interval bottom() {
		return Interval.empty();
	}

	@Override
	public Interval copy(Interval original) {
		return original.clone();
	}

	@Override
	public Interval join(Interval left,
			Interval right) {
			return left.merge(right);
	}

}
