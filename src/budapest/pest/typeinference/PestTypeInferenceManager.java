package budapest.pest.typeinference;

import budapest.pest.ast.params.FormalParam;
import budapest.pest.ast.proc.Procedure;
import budapest.pest.ast.proc.Program;
import budapest.pest.ast.stmt.AssignStmt;
import budapest.pest.ast.stmt.BlockStmt;
import budapest.pest.ast.stmt.IfStmt;
import budapest.pest.ast.stmt.LocalDefStmt;
import budapest.pest.ast.stmt.LoopStmt;
import budapest.pest.ast.stmt.SeqStmt;
import budapest.pest.ast.stmt.SkipStmt;
import budapest.pest.ast.visitor.PestVisitor;
import budapest.pest.dump.pest.ExpPrinter;

public class PestTypeInferenceManager extends PestVisitor<PestTypeInferenceResult, PestTypedContext> {

	public PestTypeInferenceResult infere(Program n)
	{
		for(Procedure proc : n.procs)
		{
			PestTypedContext context = new PestTypedContext();
			for(FormalParam param : proc.params)
			{
				context.add(param.name, type)
			}
			
			PestTypeInferenceResult result = proc.accept(this, new PestTypedContext());
			if(!result.succeeded)
			{
				return result;
			}
		}
		return new PestTypeInferenceResult(new PestTypedContext(), true, "Program typed OK!");
	}
	
	public PestTypeInferenceResult visit(Procedure n, PestTypedContext context)
	{
		return n.stmt.accept(this, context);
	}
	
	public PestTypeInferenceResult visit(BlockStmt n, PestTypedContext context)
	{
		return n.stmt.accept(this, context);
	}
	
	public PestTypeInferenceResult visit(AssignStmt n, PestTypedContext context)
	{
		PestTypeJudgment rightJudgment = n.right.accept(new ExpTypeInferenceManager(), context);
		if(!rightJudgment.isValid)
		{
			return new PestTypeInferenceResult(null,
					false, 
					rightJudgment.status);
		}
		
		if(context.isTyped(n.left.name))
		{
			PestType varType = context.getTypeOf(n.left.name);
			PestType unifier = mgu.execute(varType, rightJudgment.type);
			if(unifier == null)
			{
				return new PestTypeInferenceResult(null, 
						false,
						n.left.name + "(" + varType.getTypeName() + ") has a different type than " +
						n.right.accept(new ExpPrinter(), null));
			}
		}
		
		context.setType(n.left.name, rightJudgment.type);
		return new PestTypeInferenceResult(new PestTypedContext(context), true, "AssignStmt typed OK!");
	}
	
	public PestTypeInferenceResult visit(LocalDefStmt n, PestTypedContext context)
	{
		PestTypeJudgment rightJudgment = n.right.accept(new ExpTypeInferenceManager(), context);
		if(!rightJudgment.isValid)
		{
			return new PestTypeInferenceResult(null,
					false, 
					rightJudgment.status);
		}
						
		context.setType(n.left.name, rightJudgment.type);
		return new PestTypeInferenceResult(new PestTypedContext(context), true, "LocalDefStmt typed OK!");
	}
			
	public PestTypeInferenceResult visit(SeqStmt n, PestTypedContext context)
	{
		PestTypeInferenceResult r1 = n.s1.accept(this, context);
		if(!r1.succeeded) return r1;
		return n.s1.accept(this, r1.context);
	}
	
	public PestTypeInferenceResult visit(IfStmt n, PestTypedContext context)
	{
		PestTypeJudgment conditionJudgment = n.condition.accept(new ExpTypeInferenceManager(), context);
		if(!conditionJudgment.isValid)
		{
			return new PestTypeInferenceResult(null,
					false, 
					conditionJudgment.status);
		}
		
		PestTypeInferenceResult resThen = n.thenS.accept(this, context);
		if(!resThen.succeeded) return resThen;
		
		return n.elseS.accept(this, context);
	}
	
	public PestTypeInferenceResult visit(LoopStmt n, PestTypedContext context)
	{
		PestTypeJudgment conditionJudgment = n.condition.accept(new ExpTypeInferenceManager(), context);
		if(!conditionJudgment.isValid)
		{
			return new PestTypeInferenceResult(null,
					false, 
					conditionJudgment.status);
		}
			
		return n.body.accept(this, context);
	}
			
	public PestTypeInferenceResult visit(SkipStmt n, PestTypedContext context)
	{
		return new PestTypeInferenceResult(context, true, "SkipStmt typed OK!");
	}
	
}
