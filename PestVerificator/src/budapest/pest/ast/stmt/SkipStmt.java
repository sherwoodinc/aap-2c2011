package budapest.pest.ast.stmt;

import budapest.pest.ast.visitor.PestVisitor;

public final class SkipStmt extends Stmt {

	public SkipStmt(int line, int column) {
		super(line, column);
	}

	@Override
	public <R, A> R accept(PestVisitor<R, A> v, A arg) {
		return v.visit(this, arg);
	}

}
