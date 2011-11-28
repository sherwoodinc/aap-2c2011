package budapest.pest.typeinference;

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

	public PestProgramTypeInferenceResult infere(Program n)
	{
		PestProgramTypeInferenceResult programResult = new PestProgramTypeInferenceResult(); 
						
		boolean succeeded = true;
		
		for(Procedure proc : n.procs)
		{
			PredTypeJudgment preJudgment = proc.pre.accept(new PredTypeInferenceManager(), new PestTypedContext());
			if(!preJudgment.isValid)
			{
				programResult.procsInference.add(new PestProcedureTypeInferenceResult(proc.name, null, false, preJudgment.toString()));
				succeeded = false;
				continue;
			}
			
			PredTypeJudgment postJudgment = proc.post.accept(new PredTypeInferenceManager(), preJudgment.context);
			if(!postJudgment.isValid)
			{
				programResult.procsInference.add(new PestProcedureTypeInferenceResult(proc.name, null, false, postJudgment.toString()));
				succeeded = false;
				continue;
			}
								
			PestTypeInferenceResult result = proc.accept(this, postJudgment.context);
			if(!result.succeeded)
			{
				programResult.procsInference.add(new PestProcedureTypeInferenceResult(proc.name, null, false, result.toString()));
				succeeded = false;
				continue;
			}

			programResult.procsInference.add(new PestProcedureTypeInferenceResult(proc.name, null, true, result.toString()));
		}
		
		programResult.succeeded = succeeded;
		if(succeeded)
		{
			programResult.message = "Program typed OK!";
		}
		else
		{
			programResult.message = "Program did not type";
		}
		
		return programResult; 
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
		ExpTypeJudgment rightJudgment = n.right.accept(new ExpTypeInferenceManager(), context);
		if(!rightJudgment.isValid)
		{
			return new PestTypeInferenceResult(null,
					false, 
					rightJudgment.toString());
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
		ExpTypeJudgment rightJudgment = n.right.accept(new ExpTypeInferenceManager(), context);
		if(!rightJudgment.isValid)
		{
			return new PestTypeInferenceResult(null,
					false, 
					rightJudgment.toString());
		}
						
		context.setType(n.left.name, rightJudgment.type);
		return new PestTypeInferenceResult(new PestTypedContext(context), true, "LocalDefStmt typed OK!");
	}
			
	public PestTypeInferenceResult visit(SeqStmt n, PestTypedContext context)
	{
		PestTypeInferenceResult r1 = n.s1.accept(this, context);
		if(!r1.succeeded) return r1;
		return n.s2.accept(this, r1.context);
	}
	
	public PestTypeInferenceResult visit(IfStmt n, PestTypedContext context)
	{
		ExpTypeJudgment conditionJudgment = n.condition.accept(new ExpTypeInferenceManager(), context);
		if(!conditionJudgment.isValid)
		{
			return new PestTypeInferenceResult(null,
					false, 
					conditionJudgment.toString());
		}
		
		PestTypeInferenceResult resThen = n.thenS.accept(this, context);
		if(!resThen.succeeded) return resThen;
		
		return n.elseS.accept(this, context);
	}
	
	public PestTypeInferenceResult visit(LoopStmt n, PestTypedContext context)
	{
		ExpTypeJudgment conditionJudgment = n.condition.accept(new ExpTypeInferenceManager(), context);
		if(!conditionJudgment.isValid)
		{
			return new PestTypeInferenceResult(null,
					false, 
					conditionJudgment.toString());
		}
			
		return n.body.accept(this, context);
	}
			
	public PestTypeInferenceResult visit(SkipStmt n, PestTypedContext context)
	{
		return new PestTypeInferenceResult(context, true, "SkipStmt typed OK!");
	}
	
}
