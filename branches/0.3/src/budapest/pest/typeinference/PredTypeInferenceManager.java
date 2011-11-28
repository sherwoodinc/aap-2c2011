package budapest.pest.typeinference;

import budapest.pest.ast.pred.BinaryPred;
import budapest.pest.ast.pred.BooleanLiteralPred;
import budapest.pest.ast.pred.NotPred;
import budapest.pest.ast.pred.QuantifiedPred;
import budapest.pest.ast.pred.RelationPred;
import budapest.pest.ast.visitor.PredVisitor;
import budapest.pest.dump.pest.PredPrinter;
import budapest.pest.dump.pest.TrmPrinter;

public class PredTypeInferenceManager extends PredVisitor<PredTypeJudgment, PestTypedContext> {
			
	public PredTypeJudgment visit(BinaryPred n, PestTypedContext context)
	{
		PredTypeJudgment leftJudgment = n.left.accept(this, context);
		if(!leftJudgment.isValid) return leftJudgment;
		
		PredTypeJudgment rightJudgment = n.right.accept(this, context);
		if(!rightJudgment.isValid) return rightJudgment;
		
		if(leftJudgment.type.equals(PestTypes.Bool()) && rightJudgment.type.equals(PestTypes.Bool()))
		{
			PestTypedContext leftAndRightContext = new PestTypedContext(leftJudgment.context);
			PestTypedContextUnionResult leftAndRightResult = leftAndRightContext.union(rightJudgment.context);
			if(!leftAndRightResult.succeeded)
			{
				return new PredTypeJudgment(false,
						"Typing " +
						"error in pred: " + 
					    n.accept(new PredPrinter(), null) + ". " + 
					    leftAndRightResult.message);
				
			}
						
			return new PredTypeJudgment(PestTypes.Bool(),
					leftAndRightContext, n, true, "");
		}
		
		return new PredTypeJudgment(false,
				"Typing error in pred: " + 
			    n.accept(new PredPrinter(), null) + ". " + 
				"Left is " + leftJudgment.type.getTypeName() +
				" and right is " + rightJudgment.type.getTypeName());
	}
	
	public PredTypeJudgment visit(BooleanLiteralPred n, PestTypedContext context)
	{
		return new PredTypeJudgment(PestTypes.Bool(), context, n);
	}
	
	public PredTypeJudgment visit(NotPred n, PestTypedContext context)
	{
		PredTypeJudgment subPredJudgment = n.subPred.accept(this, context);
		if(!subPredJudgment.isValid) return subPredJudgment;
		
		PestType subPredType = subPredJudgment.type;
		if(subPredType.equals(PestTypes.Bool()))
		{
			return new PredTypeJudgment(PestTypes.Bool(),
					new PestTypedContext(subPredJudgment.context),
					n);
		}
		
		if(subPredType instanceof PestTypingConstant)
		{
			PestTypedContext newContext = new PestTypedContext(subPredJudgment.context);
			context.replaceType(subPredType, PestTypes.Bool());
			return new PredTypeJudgment(PestTypes.Int(),
					newContext,
					n);
		}
		
		return new PredTypeJudgment(false,
				"Cannot apply not to pred: " + subPredJudgment.pred.accept(new PredPrinter(), null));
	}
			
	public PredTypeJudgment visit(RelationPred n, PestTypedContext context)
	{
		TrmTypeJudgment leftJudgment = n.left.accept(new TrmTypeInferenceManager(), context);
		if(!leftJudgment.isValid)
		{
			return new PredTypeJudgment(false, leftJudgment.toString());
		}
		
		TrmTypeJudgment rightJudgment = n.right.accept(new TrmTypeInferenceManager(), context);
		if(!rightJudgment.isValid)
		{
			return new PredTypeJudgment(false, rightJudgment.toString());
		}
		
		PestType unifier = mgu.execute(leftJudgment.type, rightJudgment.type);
		if(unifier == null)
		{
			return new PredTypeJudgment(false, 
					"Cannot apply "+ n.op.toString()  + " to the terms " + n.left.accept(new TrmPrinter(), null) + 
					" ("+ leftJudgment.type.getTypeName() +") and " + n.right.accept(new TrmPrinter(), null) + 
					" (" + rightJudgment.type.getTypeName() + ")");
		}
		
		PestTypedContext leftAndRightContext = new PestTypedContext(leftJudgment.context);
		PestTypedContextUnionResult leftAndRightResult = leftAndRightContext.union(rightJudgment.context);
		if(!leftAndRightResult.succeeded)
		{
			return new PredTypeJudgment(false,
					"Typing error in pred: " + 
				    n.accept(new PredPrinter(), null) + ". " + 
				    leftAndRightResult.message);
		}
		leftAndRightContext.replaceType(leftJudgment.type, unifier);
		leftAndRightContext.replaceType(rightJudgment.type, unifier);
						
		return new PredTypeJudgment(PestTypes.Bool(),
				leftAndRightContext, n, true, "");
	}
	
	public PredTypeJudgment visit(QuantifiedPred n, PestTypedContext context)
	{
		return null;
	}
}
