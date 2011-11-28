package budapest.pest.typeinference;

import budapest.pest.ast.pred.trm.BinaryTrm;
import budapest.pest.ast.pred.trm.IntegerLiteralTrm;
import budapest.pest.ast.pred.trm.NegTrm;
import budapest.pest.ast.pred.trm.StringLiteralTrm;
import budapest.pest.ast.pred.trm.StringSizeTrm;
import budapest.pest.ast.pred.trm.VarTrm;
import budapest.pest.ast.visitor.TrmVisitor;
import budapest.pest.dump.pest.TrmPrinter;

public class TrmTypeInferenceManager extends TrmVisitor<TrmTypeJudgment, PestTypedContext> {

	public TrmTypeJudgment visit(BinaryTrm n, PestTypedContext context)
	{
		TrmTypeJudgment leftJudgment = n.left.accept(this, context);
		if(!leftJudgment.isValid) return leftJudgment;
		
		TrmTypeJudgment rightJudgment = n.right.accept(this, context);
		if(!rightJudgment.isValid) return rightJudgment;
		
		if(n.op != BinaryTrm.Operator.ADD && 
		  (leftJudgment.type.equals(PestTypes.String()) || rightJudgment.type.equals(PestTypes.String())))
		{
			if(n.op != BinaryTrm.Operator.ADD)
			{
				return new TrmTypeJudgment(false,
						n.op.toString() + " operation is not defined for type string");
			}
		}
		
		PestType unifier = mgu.execute(leftJudgment.type, rightJudgment.type);
		if(unifier == null)
		{
			String leftAsString = leftJudgment.term.accept(new TrmPrinter(), null);
			String rightAsString = rightJudgment.term.accept(new TrmPrinter(), null);
			
			return new TrmTypeJudgment(false,
					leftAsString + " (" + leftJudgment.type.getTypeName() + ") " + 
					" has a different type than " +
					rightAsString + " (" + rightJudgment.type.getTypeName()  + ")");
		}
		
		PestTypedContext newContext = new PestTypedContext(leftJudgment.context);
		PestTypedContextUnionResult unionResult = newContext.union(rightJudgment.context);
		if(!unionResult.succeeded)
		{
			return new TrmTypeJudgment(false,
					"Typing error in expression: " + 
				    n.accept(new TrmPrinter(), null) + ". " + 
					unionResult.message);
		}
		newContext.replaceType(leftJudgment.type, unifier);
		newContext.replaceType(rightJudgment.type, unifier);
		
		return new TrmTypeJudgment(unifier,
				newContext, n, true, "");
	}
	
	public TrmTypeJudgment visit(IntegerLiteralTrm n, PestTypedContext context)
	{
		return new TrmTypeJudgment(PestTypes.Int(), context, n);
	}
	
	public TrmTypeJudgment visit(NegTrm n, PestTypedContext context)
	{
		TrmTypeJudgment subTrmJudgment = n.subTrm.accept(this, context);
		if(!subTrmJudgment.isValid)
		{
			return subTrmJudgment;
		}
		
		PestType subTrmType = subTrmJudgment.type;
		PestType unifier = mgu.execute(subTrmType, PestTypes.Int());
		if(unifier == null)
		{
			return new TrmTypeJudgment(false, 
					"Cannot apply neg to the term " + n.accept(new TrmPrinter(), null) + 
					" as it's not an Int");
		}
		
		context.replaceType(subTrmType, unifier);
		return new TrmTypeJudgment(PestTypes.Int(), context, n);
	}
	
	public TrmTypeJudgment visit(StringLiteralTrm n, PestTypedContext context)
	{
		return new TrmTypeJudgment(PestTypes.String(), context, n);
	}
	
	public TrmTypeJudgment visit(StringSizeTrm n, PestTypedContext context)
	{
		if(context.isTyped(n.string))
		{
			PestType existingType = context.getTypeOf(n.string);
			PestType unifier = mgu.execute(existingType, PestTypes.String());
			if(unifier == null)
			{
				return new TrmTypeJudgment(false, 
						n.string + " must be a String in order to get its length");
			}
		}
		
		context.add(n.string, PestTypes.String());
		return new TrmTypeJudgment(PestTypes.Int(), context, n);
	}
	
	public TrmTypeJudgment visit(VarTrm n, PestTypedContext context)
	{
		PestType type = new PestTypingConstant();
		context.add(n.name, type);
		return new TrmTypeJudgment(type, context, n);
	}
}
