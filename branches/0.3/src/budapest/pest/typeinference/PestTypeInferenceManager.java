package budapest.pest.typeinference;

import budapest.pest.ast.proc.Procedure;
import budapest.pest.ast.proc.Program;
import budapest.pest.ast.stmt.AssertStmt;
import budapest.pest.ast.stmt.AssignStmt;
import budapest.pest.ast.stmt.AssumeStmt;
import budapest.pest.ast.stmt.BlockStmt;
import budapest.pest.ast.stmt.CallStmt;
import budapest.pest.ast.stmt.IfStmt;
import budapest.pest.ast.stmt.LocalDefStmt;
import budapest.pest.ast.stmt.LoopStmt;
import budapest.pest.ast.stmt.SeqStmt;
import budapest.pest.ast.stmt.SkipStmt;
import budapest.pest.ast.visitor.PestVisitor;

public class PestTypeInferenceManager extends PestVisitor<PestTypeJudgment, PestTypedContext> {

	public void checkTyping(Program n)
	{
		for(Procedure proc : n.procs)
		{
			proc.accept(this, new PestTypedContext());
		}
	}
	
	public PestTypeJudgment visit(Procedure n, PestTypedContext context)
	{
		return n.stmt.accept(this, context);
	}
	
	public PestTypeJudgment visit(BlockStmt n, PestTypedContext context)
	{
		return n.stmt.accept(this, context);
	}
	
	public PestTypeJudgment visit(AssignStmt n, PestTypedContext context)
	{
		return null;
	}
	
	public PestTypeJudgment visit(LocalDefStmt n, PestTypedContext context)
	{
		return null;
	}
	
	public PestTypeJudgment visit(CallStmt n, PestTypedContext context)
	{
		return null;
	}
	
	public PestTypeJudgment visit(SeqStmt n, PestTypedContext context)
	{
		return null;
	}
	
	public PestTypeJudgment visit(IfStmt n, PestTypedContext context)
	{
		return null;
	}
	
	public PestTypeJudgment visit(LoopStmt n, PestTypedContext context)
	{
		return null;
	}
	
	public PestTypeJudgment visit(AssumeStmt n, PestTypedContext context)
	{
		return null;
	}
	
	public PestTypeJudgment visit(AssertStmt n, PestTypedContext context)
	{
		return null;
	}
	
	public PestTypeJudgment visit(SkipStmt n, PestTypedContext context)
	{
		return null;
	}
	
}
