package budapest.pest.typeinference;

import budapest.pest.ast.exp.ArithTopExp;
import budapest.pest.ast.exp.BinBoolExp;
import budapest.pest.ast.exp.LiteralIntExp;
import budapest.pest.ast.exp.LiteralStringExp;
import budapest.pest.ast.exp.NegTopExp;
import budapest.pest.ast.exp.NotBoolExp;
import budapest.pest.ast.exp.RelBoolExp;
import budapest.pest.ast.exp.SizeTopExp;
import budapest.pest.ast.exp.VarExp;
import budapest.pest.ast.visitor.ExpVisitor;
import budapest.pest.dump.pest.ExpPrinter;

public class ExpTypeInferenceManager extends ExpVisitor<PestTypeJudgment, PestTypedContext> {

	public PestTypeJudgment visit(ArithTopExp n, PestTypedContext arg) 
	{
		PestTypeJudgment leftJudgment = n.left.accept(this, arg);
		if(!leftJudgment.isValid) return leftJudgment;
		
		PestTypeJudgment rightJudgment = n.right.accept(this, arg);
		if(!rightJudgment.isValid) return rightJudgment;
		
		if(n.op != ArithTopExp.Operator.ADD && 
		  (leftJudgment.type.equals(PestTypes.String()) || rightJudgment.type.equals(PestTypes.String())))
		{
			if(n.op != ArithTopExp.Operator.ADD)
			{
				return new PestTypeJudgment(false,
						n.op.toString() + " operation is not defined for type string");
			}
		}
		
		PestType unifier = mgu.execute(leftJudgment.type, rightJudgment.type);
		if(unifier == null)
		{
			String leftAsString = leftJudgment.expression.accept(new ExpPrinter(), null);
			String rightAsString = rightJudgment.expression.accept(new ExpPrinter(), null);
			
			return new PestTypeJudgment(false,
					leftAsString + " (" + leftJudgment.type.getTypeName() + ") " + 
					" has a different type than " +
					rightAsString + " (" + rightJudgment.type.getTypeName()  + ")");
		}
		
		PestTypedContext context = new PestTypedContext(leftJudgment.context);
		PestTypedContextUnionResult unionResult = context.union(rightJudgment.context);
		if(unionResult.succeeded)
		{
			return new PestTypeJudgment(unifier,
					context, n, true, "");
		}
		else
		{
			return new PestTypeJudgment(false,
					"Typing error in expression: " + 
				    n.accept(new ExpPrinter(), null) + ". " + 
					unionResult.message);
		}
	}
	
	public PestTypeJudgment visit(LiteralIntExp n, PestTypedContext arg) 
	{
		return new PestTypeJudgment(new PestIntType(), new PestTypedContext(), n);
	}
	
	public PestTypeJudgment visit(LiteralStringExp n, PestTypedContext arg) 
	{
		return new PestTypeJudgment(new PestStringType(), new PestTypedContext(), n);
	}
	
	public PestTypeJudgment visit(VarExp n, PestTypedContext arg) 
	{
		PestTypedContext context = new PestTypedContext();
		context.add(n.name, new PestTypingConstant());
		return new PestTypeJudgment(new PestTypingConstant(), context, n);
	}

	public PestTypeJudgment visit(NegTopExp n, PestTypedContext arg) 
	{
		PestTypeJudgment subExpJudgment = n.subExp.accept(this, arg);
		if(!subExpJudgment.isValid) return subExpJudgment;
		
		PestType subExpType = subExpJudgment.type;
		if(subExpType.equals(PestTypes.Int()))
		{
			return new PestTypeJudgment(PestTypes.Int(),
					new PestTypedContext(subExpJudgment.context),
					n);
		}
		
		if(subExpType instanceof PestTypingConstant)
		{
			PestTypedContext context = new PestTypedContext(subExpJudgment.context);
			context.replaceType(subExpType, PestTypes.Int());
			return new PestTypeJudgment(PestTypes.Int(),
					context,
					n);
		}
		
		return new PestTypeJudgment(false,
				"Cannot apply neg operation to expression: " + subExpJudgment.expression.accept(new ExpPrinter(), null));
	}
	
	public PestTypeJudgment visit(RelBoolExp n, PestTypedContext arg) 
	{
		PestTypeJudgment leftJudgment = n.left.accept(this, arg);
		if(!leftJudgment.isValid) return leftJudgment;
		
		PestTypeJudgment rightJudgment = n.right.accept(this, arg);
		if(!rightJudgment.isValid) return rightJudgment;
		
		if(leftJudgment.type.equals(PestTypes.Int()) && rightJudgment.type.equals(PestTypes.Int()))
		{
			PestTypedContext context = new PestTypedContext(leftJudgment.context);
			PestTypedContextUnionResult unionResult = context.union(rightJudgment.context);
			if(unionResult.succeeded)
			{
				return new PestTypeJudgment(PestTypes.Bool(),
						context, n, true, "");
			}
			else
			{
				return new PestTypeJudgment(false,
						"Typing error in expression: " + 
					    n.accept(new ExpPrinter(), null) + ". " + 
						unionResult.message);
			}
		}
		
		return new PestTypeJudgment(false,
				"Typing error in expression: " + 
			    n.accept(new ExpPrinter(), null) + ". " + 
				"Left is " + leftJudgment.type.getTypeName() +
				" and right is " + rightJudgment.type.getTypeName());
	}
	
	public PestTypeJudgment visit(BinBoolExp n, PestTypedContext arg) 
	{
		PestTypeJudgment leftJudgment = n.left.accept(this, arg);
		if(!leftJudgment.isValid) return leftJudgment;
		
		PestTypeJudgment rightJudgment = n.right.accept(this, arg);
		if(!rightJudgment.isValid) return rightJudgment;
		
		if(leftJudgment.type.equals(PestTypes.Bool()) && rightJudgment.type.equals(PestTypes.Bool()))
		{
			PestTypedContext context = new PestTypedContext(leftJudgment.context);
			PestTypedContextUnionResult unionResult = context.union(rightJudgment.context);
			if(unionResult.succeeded)
			{
				return new PestTypeJudgment(PestTypes.Bool(),
						context, n, true, "");
			}
			else
			{
				return new PestTypeJudgment(false,
						"Typing error in expression: " + 
					    n.accept(new ExpPrinter(), null) + ". " + 
						unionResult.message);
			}
		}
		
		return new PestTypeJudgment(false,
				"Typing error in expression: " + 
			    n.accept(new ExpPrinter(), null) + ". " + 
				"Left is " + leftJudgment.type.getTypeName() +
				" and right is " + rightJudgment.type.getTypeName());
	}
	
	public PestTypeJudgment visit(NotBoolExp n, PestTypedContext arg) 
	{
		PestTypeJudgment subExpJudgment = n.subExp.accept(this, arg);
		if(!subExpJudgment.isValid) return subExpJudgment;
		
		PestType subExpType = subExpJudgment.type;
		if(subExpType.equals(PestTypes.Bool()))
		{
			return new PestTypeJudgment(PestTypes.Bool(),
					new PestTypedContext(subExpJudgment.context),
					n);
		}
				
		return new PestTypeJudgment(false,
				"Cannot apply not operation to expression: " + subExpJudgment.expression.accept(new ExpPrinter(), null));
	}
	
	public PestTypeJudgment visit(SizeTopExp n, PestTypedContext arg) 
	{
		PestTypeJudgment subExpJudgment = n.subExp.accept(this, arg);
		if(!subExpJudgment.isValid) return subExpJudgment;
		
		PestType subExpType = subExpJudgment.type;
		if(subExpType.equals(PestTypes.String()))
		{
			return new PestTypeJudgment(PestTypes.Int(),
					new PestTypedContext(subExpJudgment.context),
					n);
		}
		
		if(subExpType instanceof PestTypingConstant)
		{
			PestTypedContext context = new PestTypedContext(subExpJudgment.context);
			context.replaceType(subExpType, PestTypes.String());
			return new PestTypeJudgment(PestTypes.Int(),
					context,
					n);
		}
		
		return new PestTypeJudgment(false,
				"Cannot apply size operation to expression: " + subExpJudgment.expression.accept(new ExpPrinter(), null));
	}
	
}
