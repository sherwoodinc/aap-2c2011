package edu.cmu.cs.crystal.analysis.npe.annotations;

import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;

import edu.cmu.cs.crystal.annotations.AnnotationDatabase;
import edu.cmu.cs.crystal.annotations.AnnotationSummary;
import edu.cmu.cs.crystal.flow.ILatticeOperations;
import edu.cmu.cs.crystal.simple.AbstractingTransferFunction;
import edu.cmu.cs.crystal.simple.TupleLatticeElement;
import edu.cmu.cs.crystal.simple.TupleLatticeOperations;
import edu.cmu.cs.crystal.tac.model.ArrayInitInstruction;
import edu.cmu.cs.crystal.tac.model.BinaryOperation;
import edu.cmu.cs.crystal.tac.model.BinaryOperator;
import edu.cmu.cs.crystal.tac.model.CopyInstruction;
import edu.cmu.cs.crystal.tac.model.LoadFieldInstruction;
import edu.cmu.cs.crystal.tac.model.LoadLiteralInstruction;
import edu.cmu.cs.crystal.tac.model.MethodCallInstruction;
import edu.cmu.cs.crystal.tac.model.NewArrayInstruction;
import edu.cmu.cs.crystal.tac.model.Variable;

public class NPEAnnotatedTransferFunction extends AbstractingTransferFunction<TupleLatticeElement<Variable, ArrayBoundsLatticeElement>> {
	/**
	 * The operations for this lattice. We want to have a tuple lattice from variables to null lattice elements, so we
	 * give it an instance of NullLatticeOperations. We also want the default value to be maybe null.
	 */
	TupleLatticeOperations<Variable, ArrayBoundsLatticeElement> ops =
		new TupleLatticeOperations<Variable, ArrayBoundsLatticeElement>(new ArrayBoundsLatticeOperations(), ArrayBoundsLatticeElement.bottom());
	private AnnotationDatabase annoDB;
	
	public NPEAnnotatedTransferFunction(AnnotationDatabase annoDB) {
		this.annoDB = annoDB;
	}	

	/**
	 * The operations will create a default lattice which will map all variables to maybe null (since that was our default).
	 * 
	 * Of course, "this" should never be null.
	 */
	public TupleLatticeElement<Variable, ArrayBoundsLatticeElement> createEntryValue(
			MethodDeclaration method) {
		TupleLatticeElement<Variable, ArrayBoundsLatticeElement> def = ops.getDefault();
		def.put(getAnalysisContext().getThisVariable(), ArrayBoundsLatticeElement.top());
		
		AnnotationSummary summary = annoDB.getSummaryForMethod(method.resolveBinding());
		
		for (int ndx = 0; ndx < method.parameters().size(); ndx++) {
			SingleVariableDeclaration decl = (SingleVariableDeclaration) method.parameters().get(ndx);
			Variable paramVar = getAnalysisContext().getSourceVariable(decl.resolveBinding());
			
			if (paramVar.resolveType().isArray()) {
				def.put(paramVar, ArrayBoundsLatticeElement.bottom());
			} else {
				def.put(paramVar, ArrayBoundsLatticeElement.top());
			}
		}
		
		return def;
	}

	/**
	 * Just return our lattice ops.
	 */
	public ILatticeOperations<TupleLatticeElement<Variable, ArrayBoundsLatticeElement>> getLatticeOperations() {
		return ops;
	}

	@Override
	public TupleLatticeElement<Variable, ArrayBoundsLatticeElement> transfer(
			LoadFieldInstruction instr,
			TupleLatticeElement<Variable, ArrayBoundsLatticeElement> value) {
	
		if (instr.getFieldName().equals("length")) {
			System.out.println("llama a length "+instr.getAccessedObjectOperand());
		}
		
		return value;
	}
	
	@Override
	public TupleLatticeElement<Variable, ArrayBoundsLatticeElement> transfer(
			BinaryOperation instr,
			TupleLatticeElement<Variable, ArrayBoundsLatticeElement> value) {
		System.out.println("BinOp: "+instr.getOperator()+" "+instr.getOperand1()+" "+instr.getOperand2());
		ArrayBoundsLatticeElement res;
		ArrayBoundsLatticeElement izq = value.get(instr.getOperand1());
		ArrayBoundsLatticeElement der = value.get(instr.getOperand2());
		switch(instr.getOperator())	
		{
		case ARIT_ADD:
			res = izq.add(der);
			break;
			
		case ARIT_SUBTRACT:
			res = izq.substract(der);
			break;
			
		case ARIT_MULTIPLY:
			res = izq.multiply(der);
			break;

		case REL_EQ:
			res = der.clone();
			break;

		case REL_GT:
			// TODO
			res = der.clone();
			break;

		default:
			res = izq.clone();		
		}
		value.put(instr.getTarget(), res);
		return value;
	}
		
	@Override
	public TupleLatticeElement<Variable, ArrayBoundsLatticeElement> transfer(
			ArrayInitInstruction instr,
			TupleLatticeElement<Variable, ArrayBoundsLatticeElement> value) {
		
		if (instr.getInitOperands().size() > 0)
			value.put(instr.getTarget(), new ArrayBoundsLatticeElement(0, instr.getInitOperands().size()-1) );
		else
			value.put(instr.getTarget(), ArrayBoundsLatticeElement.bottom() );
		return value;
	}

	@Override
	public TupleLatticeElement<Variable, ArrayBoundsLatticeElement> transfer(
			CopyInstruction instr,
			TupleLatticeElement<Variable, ArrayBoundsLatticeElement> value) {
		value.put(instr.getTarget(), value.get(instr.getOperand()));
		return value;
	}

	@Override
	public TupleLatticeElement<Variable, ArrayBoundsLatticeElement> transfer(
			LoadLiteralInstruction instr,
			TupleLatticeElement<Variable, ArrayBoundsLatticeElement> value) {
		 
		if (instr.isNumber() && instr.getLiteral() instanceof java.lang.String) {
			int index = Integer.parseInt((String)instr.getLiteral());
			value.put(instr.getTarget(), new ArrayBoundsLatticeElement(index,index));
		}
		else
			value.put(instr.getTarget(), ArrayBoundsLatticeElement.top());
		return value;
	}

	@Override
	public TupleLatticeElement<Variable, ArrayBoundsLatticeElement> transfer(
			NewArrayInstruction instr,
			TupleLatticeElement<Variable, ArrayBoundsLatticeElement> value) {
		
		if (instr.isInitialized()) {
			int dim = instr.getDimensions();
			value.put(instr.getTarget(), new ArrayBoundsLatticeElement(0, dim-1));
		} else {
			value.put(instr.getTarget(), ArrayBoundsLatticeElement.bottom());
		}
		
		return value;
	}
}
