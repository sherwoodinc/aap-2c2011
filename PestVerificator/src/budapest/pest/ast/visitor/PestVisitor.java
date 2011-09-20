package budapest.pest.ast.visitor;

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
import budapest.util.ASTNode;

public abstract class PestVisitor<R, A> {

	// basic node
	public R visit(ASTNode n, A arg) {
		throw new IllegalStateException(n.getClass().getName());
	}

	// full program with many procs
	public R visit(Program n, A arg) {
		for(Procedure proc : n.procs)
			proc.accept(this, arg);
		return null;
	}
	
	// procedure declaration nodes
	public R visit(Procedure n, A arg) {
		n.stmt.accept(this, arg);
		return null;
	}
	
	// block statements
	public R visit(BlockStmt n, A arg) {
		n.stmt.accept(this, arg);
		return null;
	}

	// basic statements
	public R visit(AssignStmt n, A arg) {
		return null;
	}
	
	public R visit(LocalDefStmt n, A arg) {
		return null;
	}
	
	public R visit(CallStmt n, A arg) {
		return null;
	}

	public R visit(SeqStmt n, A arg) {
		n.s1.accept(this, arg);
		n.s2.accept(this, arg);
		return null;
	}
	
	public R visit(IfStmt n, A arg) {
		n.thenS.accept(this, arg);
		n.elseS.accept(this, arg);
		return null;
	}
	
	public R visit(LoopStmt n, A arg) {
		n.body.accept(this, arg);
		return null;
	}
	
	public R visit(AssumeStmt n, A arg) {
		return null;
	}
	
	public R visit(AssertStmt n, A arg) {
		return null;
	}
	
	public R visit(SkipStmt n, A arg) {
		return null;
	}
	
}

