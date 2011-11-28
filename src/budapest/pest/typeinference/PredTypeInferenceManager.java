package budapest.pest.typeinference;

import budapest.pest.ast.pred.BinaryPred;
import budapest.pest.ast.pred.BooleanLiteralPred;
import budapest.pest.ast.pred.NotPred;
import budapest.pest.ast.pred.QuantifiedPred;
import budapest.pest.ast.pred.RelationPred;
import budapest.pest.ast.pred.trm.IntegerLiteralTrm;
import budapest.pest.ast.pred.trm.StringSizeTrm;
import budapest.pest.ast.pred.trm.Trm;
import budapest.pest.ast.pred.trm.VarTrm;
import budapest.pest.ast.visitor.PredVisitor;
import budapest.pest.dump.pest.ExpPrinter;
import budapest.pest.dump.pest.PredPrinter;

public class PredTypeInferenceManager extends PredVisitor<PredTypeJudgment, PestTypedContext> {
			
	public PredTypeJudgment visit(BinaryPred n, PestTypedContext context)
	{
		PredTypeJudgment leftJudgment = n.left.accept(this, context);
		if(!leftJudgment.isValid) return leftJudgment;
		
		PredTypeJudgment rightJudgment = n.right.accept(this, context);
		if(!rightJudgment.isValid) return rightJudgment;
		
		if(leftJudgment.type.equals(PestTypes.Bool()) && rightJudgment.type.equals(PestTypes.Bool()))
		{
			PestTypedContext newContext = new PestTypedContext(leftJudgment.context);
			PestTypedContextUnionResult unionResult = context.union(rightJudgment.context);
			if(unionResult.succeeded)
			{
				return new PredTypeJudgment(PestTypes.Bool(),
						newContext, n, true, "");
			}
			else
			{
				return new PredTypeJudgment(false,
						"Typing error in pred: " + 
					    n.accept(new PredPrinter(), null) + ". " + 
						unionResult.message);
			}
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
	
	public PredTypeJudgment visit(QuantifiedPred n, PestTypedContext context)
	{
		return null;
	}
	
	public PredTypeJudgment visit(RelationPred n, PestTypedContext context)
	{
		Trm left = n.left;
		if(!(left instanceof IntegerLiteralTrm)) {
			
			if(left instanceof StringSizeTrm) {
			
				String name = ((StringSizeTrm) left).string;
				if(!context.isTyped(name)) {
					context.add(name, PestTypes.String());
				}
			}else if(left instanceof VarTrm){
				
				String name = ((VarTrm) left).name;
				if(!context.isTyped(name)) {
					context.add(name, PestTypes.Int());
				}
			}
		}
		
		return new PredTypeJudgment(PestTypes.Bool(), context, n);
	}
}
