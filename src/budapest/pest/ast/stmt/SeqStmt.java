package budapest.pest.ast.stmt;

import budapest.pest.ast.visitor.PestVisitor;

public final class SeqStmt extends Stmt {

	public final Stmt s1;
	public final Stmt s2;
	
	public SeqStmt(int line, int column, final Stmt s1, final Stmt s2) {
		super(line, column);
		this.s1 = s1;
		this.s2 = s2;
	}

	@Override
	public <R, A> R accept(PestVisitor<R, A> v, A arg) {
		return v.visit(this, arg);
	}
	
}
