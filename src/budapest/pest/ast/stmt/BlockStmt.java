package budapest.pest.ast.stmt;

import budapest.pest.ast.visitor.PestVisitor;


public final class BlockStmt extends Stmt {

	public final Stmt stmt;

	public BlockStmt(int line, int column, Stmt stmt) {
		super(line, column);
		this.stmt = stmt;
	}

	@Override
	public <R, A> R accept(PestVisitor<R, A> v, A arg) {
		return v.visit(this, arg);
	}
	
}
