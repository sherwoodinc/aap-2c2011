package budapest.pest.pesttocvc3;

import java.util.HashSet;
import java.util.Set;

import budapest.pest.ast.proc.Procedure;
import budapest.pest.ast.stmt.AssertStmt;
import budapest.pest.ast.stmt.AssignStmt;
import budapest.pest.ast.stmt.AssumeStmt;
import budapest.pest.ast.stmt.BlockStmt;
import budapest.pest.ast.stmt.IfStmt;
import budapest.pest.ast.stmt.LocalDefStmt;
import budapest.pest.ast.stmt.LoopStmt;
import budapest.pest.ast.stmt.SeqStmt;
import budapest.pest.ast.stmt.SkipStmt;
import budapest.pest.ast.visitor.PestVisitor;

public class StmtModifiedVarGetter extends PestVisitor<Set<String>, Void> {
	
	public Set<String> visit(Procedure n, Void arg) {
		return n.stmt.accept(this, arg);
	}
	
	public Set<String> visit(BlockStmt n, Void arg) {
		return n.stmt.accept(this, arg);
	}

	public Set<String> visit(AssignStmt n, Void arg) {
		Set<String> result = new HashSet<String>();
		result.add(n.left.name);
		return result;
	}
	
	public Set<String> visit(LocalDefStmt n, Void arg) {
		Set<String> result = new HashSet<String>();
		result.add(n.left.name);
		return result;
	}
		
	public Set<String> visit(SeqStmt n, Void arg) {
		Set<String> result = new HashSet<String>();
		result.addAll(n.s1.accept(this, arg));
		result.addAll(n.s2.accept(this, arg));
		return result;
	}
	
	public Set<String> visit(IfStmt n, Void arg) {
		Set<String> result = new HashSet<String>();
		result.addAll(n.thenS.accept(this, arg));
		result.addAll(n.elseS.accept(this, arg));
		return result;
	}
	
	public Set<String> visit(LoopStmt n, Void arg) {
		Set<String> result = new HashSet<String>();
		result.addAll(n.body.accept(this, arg));
		return result;
	}
	
	public Set<String> visit(AssumeStmt n, Void arg) {
		return new HashSet<String>();
	}
	
	public Set<String> visit(AssertStmt n, Void arg) {
		return new HashSet<String>();
	}
	
	public Set<String> visit(SkipStmt n, Void arg) {
		return new HashSet<String>();
	}
	
}
