package edu.cmu.cs.crystal.analysis.npe.annotations;

import edu.cmu.cs.crystal.simple.SimpleLatticeOperations;

/**
 * Operaciones del reticulado entre intervalos.
 * El grueso de las operaciones entre intervalos está implementado en la clase Interval.
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
