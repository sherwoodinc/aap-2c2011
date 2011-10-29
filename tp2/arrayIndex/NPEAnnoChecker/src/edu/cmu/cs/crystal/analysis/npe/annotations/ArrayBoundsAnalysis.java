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
import edu.cmu.cs.crystal.tac.TACFlowAnalysis;
import edu.cmu.cs.crystal.tac.model.Variable;

/**
 * A simple flow analysis. This analysis is almost identical to @link{edu.cmu.cs.crystal.analysis.npe.simpleflow.SimpleNPEAnalysis},
 * the only difference is it uses a transfer function that is aware of annotations, and now the visitor will check that
 * parameters to method calls are safe and it will check assignments into fields and parameters which may be non-null.
 * 
 * @author ciera
 */
public class ArrayBoundsAnalysis extends AbstractCrystalMethodAnalysis {
	public static final String NON_NULL_ANNO = "edu.cmu.cs.crystal.annos.NonNull";
	
	TACFlowAnalysis<PairLatticeElement> flowAnalysis;

	@Override
	public String getName() {
		return "Array Bounds Checker";
	}

	@Override
	public void analyzeMethod(MethodDeclaration d) {
		ArrayBoundsTransferFunction tf = new ArrayBoundsTransferFunction();
		flowAnalysis = new TACFlowAnalysis<PairLatticeElement>(tf, getInput());
		
		d.accept(new ArrayFlowVisitor());
	}

	/**
	 * The visitor for the analysis.
	 * @author ciera
	 */
	public class ArrayFlowVisitor extends ASTVisitor {
		
		private void checkIndex(PairLatticeElement tuple, Expression array, Expression index){
			Variable varArray = flowAnalysis.getVariable(array);
			Variable varIndex = flowAnalysis.getVariable(index);
			ArrayBoundsLatticeElement elementIndex = tuple.values.get(varIndex);
			
			System.out.println(elementIndex.toString() + " in "+ varArray.toString());

			if (tuple.arrayLenghts.containsKey(varArray))
			{
				if (tuple.arrayLenghts.get(varArray).isEmpty())
				{
					getReporter().reportUserProblem("Array index '"+ index +"' may be out of bound in "+ array, index, getName(), SEVERITY.WARNING);
					return;
				}
				for (Variable v: tuple.arrayLenghts.get(varArray))
				{				
					
					if( ! tuple.values.get(v).contains(elementIndex) ) 
						getReporter().reportUserProblem("Array index '"+ index +"' may be out of bound in "+ array, index, getName(), SEVERITY.WARNING);
				}
			}
			else
				if( ! tuple.values.get(varArray).contains(elementIndex) ) 
					getReporter().reportUserProblem("Array index '"+ index +"' may be out of bound in "+ array, index, getName(), SEVERITY.WARNING);
				
		}

		@Override
		public void endVisit(ArrayAccess node) {
			PairLatticeElement beforeTuple = flowAnalysis.getResultsBefore(node);
			
			checkIndex(beforeTuple, node.getArray(), node.getIndex());
		}

		@Override
		public void endVisit(QualifiedName node) {
			//Due to an ambiguity within the parser, a qualified name may actually be a FieldAccess.
			//To check for this, see what the binding is.
			if (node.resolveBinding() instanceof IVariableBinding) {
				//now we know it's field access.
				PairLatticeElement beforeTuple = flowAnalysis.getResultsBefore(node);				
			}
		}
	}
}
