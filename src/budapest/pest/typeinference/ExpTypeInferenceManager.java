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

public class ExpTypeInferenceManager extends ExpVisitor<ExpTypeJudgment, PestTypedContext> {

	public ExpTypeJudgment visit(ArithTopExp n, PestTypedContext arg) 
	{
		ExpTypeJudgment leftJudgment = n.left.accept(this, arg);
		if(!leftJudgment.isValid) return leftJudgment;
		
		ExpTypeJudgment rightJudgment = n.right.accept(this, arg);
		if(!rightJudgment.isValid) return rightJudgment;
		
		if(n.op != ArithTopExp.Operator.ADD && 
		  (leftJudgment.type.equals(PestTypes.String()) || rightJudgment.type.equals(PestTypes.String())))
		{
			return new ExpTypeJudgment(false,
					n.op.toString() + " operation is not defined for type string");
		}
		
		PestType unifier = mgu.execute(leftJudgment.type, rightJudgment.type);
		if(unifier == null)
		{
			String leftAsString = leftJudgment.expression.accept(new ExpPrinter(), null);
			String rightAsString = rightJudgment.expression.accept(new ExpPrinter(), null);
			
			return new ExpTypeJudgment(false,
					leftAsString + " (" + leftJudgment.type.getTypeName() + ") " + 
					" has a different type than " +
					rightAsString + " (" + rightJudgment.type.getTypeName()  + ")");
		}
		
		PestTypedContext context = new PestTypedContext(leftJudgment.context);
		PestTypedContextUnionResult unionResult = context.union(rightJudgment.context);
		if(unionResult.succeeded)
		{
			return new ExpTypeJudgment(false,
					"Typing error in expression: " + 
				    n.accept(new ExpPrinter(), null) + ". " + 
					unionResult.message);
		}
		context.replaceType(leftJudgment.type, unifier);
		context.replaceType(rightJudgment.type, unifier);
		
		return new ExpTypeJudgment(unifier,
				context, n, true, "");
	}
	
	public ExpTypeJudgment visit(LiteralIntExp n, PestTypedContext arg) 
	{
		return new ExpTypeJudgment(new PestIntType(), new PestTypedContext(), n);
	}
	
	public ExpTypeJudgment visit(LiteralStringExp n, PestTypedContext arg) 
	{
		return new ExpTypeJudgment(new PestStringType(), new PestTypedContext(), n);
	}
	
	public ExpTypeJudgment visit(VarExp n, PestTypedContext arg) 
	{
		PestTypedContext context = new PestTypedContext();
		if(!arg.isTyped(n.name))
		{
			context.add(n.name, new PestTypingConstant());
		}
		return new ExpTypeJudgment(new PestTypingConstant(), context, n);
	}

	public ExpTypeJudgment visit(NegTopExp n, PestTypedContext arg) 
	{
		ExpTypeJudgment subExpJudgment = n.subExp.accept(this, arg);
		if(!subExpJudgment.isValid) return subExpJudgment;
		
		PestType subExpType = subExpJudgment.type;
		if(subExpType.equals(PestTypes.Int()))
		{
			return new ExpTypeJudgment(PestTypes.Int(),
					new PestTypedContext(subExpJudgment.context),
					n);
		}
		
		if(subExpType instanceof PestTypingConstant)
		{
			PestTypedContext context = new PestTypedContext(subExpJudgment.context);
			context.replaceType(subExpType, PestTypes.Int());
			return new ExpTypeJudgment(PestTypes.Int(),
					context,
					n);
		}
		
		return new ExpTypeJudgment(false,
				"Cannot apply neg operation to expression: " + subExpJudgment.expression.accept(new ExpPrinter(), null));
	}
	
	public ExpTypeJudgment visit(RelBoolExp n, PestTypedContext arg) 
	{
		ExpTypeJudgment leftJudgment = n.left.accept(this, arg);
		if(!leftJudgment.isValid) return leftJudgment;
		
		ExpTypeJudgment rightJudgment = n.right.accept(this, arg);
		if(!rightJudgment.isValid) return rightJudgment;
		
		if(leftJudgment.type.equals(PestTypes.Int()) && rightJudgment.type.equals(PestTypes.Int()))
		{
			PestTypedContext context = new PestTypedContext(leftJudgment.context);
			PestTypedContextUnionResult unionResult = context.union(rightJudgment.context);
			if(unionResult.succeeded)
			{
				return new ExpTypeJudgment(PestTypes.Bool(),
						context, n, true, "");
			}
			else
			{
				return new ExpTypeJudgment(false,
						"Typing error in expression: " + 
					    n.accept(new ExpPrinter(), null) + ". " + 
						unionResult.message);
			}
		}
		
		return new ExpTypeJudgment(false,
				"Typing error in expression: " + 
			    n.accept(new ExpPrinter(), null) + ". " + 
				"Left is " + leftJudgment.type.getTypeName() +
				" and right is " + rightJudgment.type.getTypeName());
	}
	
	public ExpTypeJudgment visit(BinBoolExp n, PestTypedContext arg) 
	{
		ExpTypeJudgment leftJudgment = n.left.accept(this, arg);
		if(!leftJudgment.isValid) return leftJudgment;
		
		ExpTypeJudgment rightJudgment = n.right.accept(this, arg);
		if(!rightJudgment.isValid) return rightJudgment;
		
		if(leftJudgment.type.equals(PestTypes.Bool()) && rightJudgment.type.equals(PestTypes.Bool()))
		{
			PestTypedContext context = new PestTypedContext(leftJudgment.context);
			PestTypedContextUnionResult unionResult = context.union(rightJudgment.context);
			if(unionResult.succeeded)
			{
				return new ExpTypeJudgment(PestTypes.Bool(),
						context, n, true, "");
			}
			else
			{
				return new ExpTypeJudgment(false,
						"Typing error in expression: " + 
					    n.accept(new ExpPrinter(), null) + ". " + 
						unionResult.message);
			}
		}
		
		return new ExpTypeJudgment(false,
				"Typing error in expression: " + 
			    n.accept(new ExpPrinter(), null) + ". " + 
				"Left is " + leftJudgment.type.getTypeName() +
				" and right is " + rightJudgment.type.getTypeName());
	}
	
	public ExpTypeJudgment visit(NotBoolExp n, PestTypedContext arg) 
	{
		ExpTypeJudgment subExpJudgment = n.subExp.accept(this, arg);
		if(!subExpJudgment.isValid) return subExpJudgment;
		
		PestType subExpType = subExpJudgment.type;
		if(subExpType.equals(PestTypes.Bool()))
		{
			return new ExpTypeJudgment(PestTypes.Bool(),
					new PestTypedContext(subExpJudgment.context),
					n);
		}
				
		return new ExpTypeJudgment(false,
				"Cannot apply not operation to expression: " + subExpJudgment.expression.accept(new ExpPrinter(), null));
	}
	
	public ExpTypeJudgment visit(SizeTopExp n, PestTypedContext arg) 
	{
		ExpTypeJudgment subExpJudgment = n.subExp.accept(this, arg);
		if(!subExpJudgment.isValid) return subExpJudgment;
		
		PestType subExpType = subExpJudgment.type;
		if(subExpType.equals(PestTypes.String()))
		{
			return new ExpTypeJudgment(PestTypes.Int(),
					new PestTypedContext(subExpJudgment.context),
					n);
		}
		
		if(subExpType instanceof PestTypingConstant)
		{
			PestTypedContext context = new PestTypedContext(subExpJudgment.context);
			context.replaceType(subExpType, PestTypes.String());
			return new ExpTypeJudgment(PestTypes.Int(),
					context,
					n);
		}
		
		return new ExpTypeJudgment(false,
				"Cannot apply size operation to expression: " + subExpJudgment.expression.accept(new ExpPrinter(), null));
	}
	
}
