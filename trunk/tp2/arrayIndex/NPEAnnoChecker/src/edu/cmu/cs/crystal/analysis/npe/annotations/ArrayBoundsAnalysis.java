/**
 * Copyright (c) 2006-2009 Marwan Abi-Antoun, Jonathan Aldrich, Nels E. Beckman,    
 * Kevin Bierhoff, David Dickey, Ciera Jaspan, Thomas LaToza, Gabriel Zenarosa, and others.
 *
 * This file is part of Crystal.
 *
 * Crystal is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Crystal is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Crystal.  If not, see <http://www.gnu.org/licenses/>.
 */
package edu.cmu.cs.crystal.analysis.npe.annotations;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.ArrayAccess;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.QualifiedName;

import edu.cmu.cs.crystal.AbstractCrystalMethodAnalysis;
import edu.cmu.cs.crystal.IAnalysisReporter.SEVERITY;
import edu.cmu.cs.crystal.analysis.npe.annotations.Interval.Limit;
import edu.cmu.cs.crystal.tac.TACFlowAnalysis;
import edu.cmu.cs.crystal.tac.model.Variable;

/**
 * Chequeo de accesos a array.
 */
public class ArrayBoundsAnalysis extends AbstractCrystalMethodAnalysis {

	TACFlowAnalysis<PairLatticeElement> flowAnalysis;

	@Override
	public String getName() {
		return "AAP Array Checker";
	}

	@Override
	public void analyzeMethod(MethodDeclaration d) {
		ArrayBoundsTransferFunction tf = new ArrayBoundsTransferFunction();
		flowAnalysis = new TACFlowAnalysis<PairLatticeElement>(tf, getInput());

		d.accept(new ArrayFlowVisitor());
	}

	/**
	 * Visitor para el análisis, sólo se preocupa por instrucciones ArrayAccess.
	 */
	public class ArrayFlowVisitor extends ASTVisitor {

		private void checkIndex(PairLatticeElement tuple, Expression array,
				Expression index) {
			Variable varArray = flowAnalysis.getVariable(array);
			Variable varIndex = flowAnalysis.getVariable(index);
			Interval elementIndex = tuple.values.get(varIndex);
			Interval negs = Interval.upTo(-1);

			System.out.println(elementIndex.toString() + " in "
					+ varArray.toString());

			// Primero comprobamos los negativos y luego contra las longitudes posibles del array.
			if (!checkAndReport(array, index, elementIndex, negs)) {
				if (tuple.arrayLenghts.containsKey(varArray)) {
					if (tuple.arrayLenghts.get(varArray).isEmpty()) {
						reportWarning(array, index);
						return;
					}
					for (Variable v : tuple.arrayLenghts.get(varArray)) {
						Interval arr = tuple.values.get(v);
						checkAndReport(array, index, elementIndex, arr);
					}
				} else {
					Interval arr = tuple.values.get(varArray);
					checkAndReport(array, index, elementIndex, arr);
				}
			}

		}

		private boolean checkAndReport(Expression array, Expression index,
				Interval elementIndex, Interval arr) {
			// Acá se implementa la lógica de decidir entre warning y error en un acceso a array.
			if (arr.contains(elementIndex))
			{
				reportError(array, index);
				return true;
			}
			else if (arr.overlaps(elementIndex))
			{
				reportWarning(array, index);
				return true;
			}
			return false;
		}

		private void reportError(Expression array, Expression index) {
			getReporter().reportUserProblem(
					"Array index '" + index
							+ "' is out of bounds in " + array,
					index, getName(), SEVERITY.ERROR);
		}

		private void reportWarning(Expression array, Expression index) {
			getReporter().reportUserProblem(
					"Array index '" + index
							+ "' may be out of bounds in " + array,
					index, getName(), SEVERITY.WARNING);
		}

		@Override
		public void endVisit(ArrayAccess node) {
			PairLatticeElement beforeTuple = flowAnalysis
					.getResultsBefore(node);

			checkIndex(beforeTuple, node.getArray(), node.getIndex());
		}
	}
}
