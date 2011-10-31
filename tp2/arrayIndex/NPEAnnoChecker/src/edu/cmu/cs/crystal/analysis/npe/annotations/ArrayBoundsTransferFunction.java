package edu.cmu.cs.crystal.analysis.npe.annotations;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;

import edu.cmu.cs.crystal.analysis.npe.annotations.Interval.Limit;
import edu.cmu.cs.crystal.cfg.eclipse.Label;
import edu.cmu.cs.crystal.flow.BooleanLabel;
import edu.cmu.cs.crystal.flow.ILabel;
import edu.cmu.cs.crystal.flow.ILatticeOperations;
import edu.cmu.cs.crystal.flow.IResult;
import edu.cmu.cs.crystal.flow.LabeledResult;
import edu.cmu.cs.crystal.flow.LabeledSingleResult;
import edu.cmu.cs.crystal.flow.SingleResult;
import edu.cmu.cs.crystal.tac.AbstractTACBranchSensitiveTransferFunction;
import edu.cmu.cs.crystal.tac.model.ArrayInitInstruction;
import edu.cmu.cs.crystal.tac.model.BinaryOperation;
import edu.cmu.cs.crystal.tac.model.CopyInstruction;
import edu.cmu.cs.crystal.tac.model.EnhancedForConditionInstruction;
import edu.cmu.cs.crystal.tac.model.LoadFieldInstruction;
import edu.cmu.cs.crystal.tac.model.LoadLiteralInstruction;
import edu.cmu.cs.crystal.tac.model.NewArrayInstruction;
import edu.cmu.cs.crystal.tac.model.UnaryOperation;
import edu.cmu.cs.crystal.tac.model.Variable;

public class ArrayBoundsTransferFunction extends
AbstractTACBranchSensitiveTransferFunction<PairLatticeElement> {
	/**
	 * The operations for this lattice. We want to have a tuple lattice from
	 * variables to null lattice elements, so we give it an instance of
	 * NullLatticeOperations. We also want the default value to be maybe null.
	 */
	PairLatticeOperations ops = new PairLatticeOperations();

	public ArrayBoundsTransferFunction() {
	}

	/**
	 * The operations will create a default lattice which will map all variables
	 * to maybe null (since that was our default).
	 * 
	 * Of course, "this" should never be null.
	 */
	public PairLatticeElement createEntryValue(MethodDeclaration method) {
		PairLatticeElement def = ops.getDefault();

		for (int ndx = 0; ndx < method.parameters().size(); ndx++) {
			SingleVariableDeclaration decl = (SingleVariableDeclaration) method
					.parameters().get(ndx);
			Variable paramVar = getAnalysisContext().getSourceVariable(
					decl.resolveBinding());

			if (paramVar.resolveType().isArray()) 				
				def.arrayLenghts.put(paramVar, new HashSet<Variable>());

			def.values.put(paramVar, Interval.all());
		}

		return def;
	}

	/**
	 * Just return our lattice ops.
	 */
	public ILatticeOperations<PairLatticeElement> getLatticeOperations() {
		return ops;
	}

	@Override
	public IResult<PairLatticeElement> transfer(LoadFieldInstruction instr,
			List<ILabel> labels, PairLatticeElement value) {

		cleanVariable(instr.getTarget(), value);

		if (instr.getFieldName().equals("length")) {
			System.out.println("llama a length "
					+ instr.getAccessedObjectOperand());
			value.arrayLenghts.get(instr.getSourceObject()).add(
					instr.getTarget());
		}
		value.values.put(instr.getTarget(),
				value.values.get(instr.getSourceObject()).clone());
		return LabeledSingleResult.createResult(value, labels);
	}

	@Override
	public IResult<PairLatticeElement> transfer(BinaryOperation instr,
			List<ILabel> labels, PairLatticeElement value) {
		System.out.println("BinOp: " + instr.getOperator() + " "
				+ instr.getOperand1() + " " + instr.getOperand2());
		Interval res = Interval.all();
		Interval izq = value.values.get(instr.getOperand1())
				.clone();
		Interval der = value.values.get(instr.getOperand2())
				.clone();
		Interval[] outs = null;
		switch (instr.getOperator()) {
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
		case REL_NEQ:
		case REL_LT:
		case REL_GT:
		case REL_GEQ:
		case REL_LEQ:
			izq = Interval.all();
			outs = Interval.getIntervals(instr.getOperator(), izq,der);
			break;
		}

		if (labels.size() > 1) {
			LabeledResult<PairLatticeElement> ret = LabeledResult.createResult(
					labels, value);
			PairLatticeElement valueTrue = ops.copy(value);
			PairLatticeElement valueFalse = ops.copy(value);
			valueTrue.values.put(instr.getOperand1(), outs[0]);
			//valueTrue.values.put(instr.getOperand2(), outs[1]);
			valueFalse.values.put(instr.getOperand1(), outs[2]);
			//valueFalse.values.put(instr.getOperand2(), outs[3]);
			ret.put(BooleanLabel.getBooleanLabel(true), valueTrue);
			ret.put(BooleanLabel.getBooleanLabel(false), valueFalse);
			return ret;
		} else {
			value.values.put(instr.getTarget(), res);
			return LabeledSingleResult.createResult(value, labels);
		}
	}

	public IResult<PairLatticeElement> transfer(UnaryOperation instr,
			List<ILabel> labels, PairLatticeElement value) {
		System.out.println("UnOp: " + instr.getOperator() + " "
				+ instr.getOperand());
		Interval op = value.values.get(instr.getOperand());
		switch (instr.getOperator())
		{
		case ARIT_MINUS:
			value.values.put(instr.getTarget(), op.negate());
			break;				
		}
		return LabeledSingleResult.createResult(value, labels);
	}
	
	@Override
	public IResult<PairLatticeElement> transfer(ArrayInitInstruction instr,
			List<ILabel> labels, PairLatticeElement value) {

		if (instr.getInitOperands().size() > 0)
			value.values.put(instr.getTarget(), new Interval(
					instr.getInitOperands().size(), instr.getInitOperands().size()));
		else
			value.values.put(instr.getTarget(),
					Interval.empty());
		return LabeledSingleResult.createResult(value, labels);
	}

	@Override
	public IResult<PairLatticeElement> transfer(CopyInstruction instr,
			List<ILabel> labels, PairLatticeElement value) {
		cleanVariable(instr.getTarget(), value);
		value.values.put(instr.getTarget(),
				value.values.get(instr.getOperand()));
		return LabeledSingleResult.createResult(value, labels);
	}

	private void cleanVariable(Variable target, PairLatticeElement value) {
		for (Map.Entry<Variable, Set<Variable>> entry : value.arrayLenghts
				.entrySet()) {
			entry.getValue().remove(target);
		}

	}

	@Override
	public IResult<PairLatticeElement> transfer(LoadLiteralInstruction instr,
			List<ILabel> labels, PairLatticeElement value) {

		if (instr.isNumber() && instr.getLiteral() instanceof java.lang.String) {
			int index = Integer.parseInt((String) instr.getLiteral());
			value.values.put(instr.getTarget(), new Interval(
					index, index));
		}
		cleanVariable(instr.getTarget(), value);
		return LabeledSingleResult.createResult(value, labels);
	}

	@Override
	public IResult<PairLatticeElement> transfer(NewArrayInstruction instr,
			List<ILabel> labels, PairLatticeElement value) {

		Variable dim = instr.getDimensionOperands().get(0);
		Interval adim = value.values.get(dim);
		value.values.put(instr.getTarget(), adim);
		if (!value.arrayLenghts.containsKey(instr.getTarget()))
			value.arrayLenghts.put(instr.getTarget(), new HashSet<Variable>());

		return LabeledSingleResult.createResult(value, labels);
	}

}
