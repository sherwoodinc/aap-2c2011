package budapest.pest.ast.stmt;

import budapest.pest.ast.pred.Pred;
import budapest.pest.ast.visitor.PestVisitor;

public final class AssumeStmt extends Stmt {

	public final Pred hypothesis;

	public AssumeStmt(int line, int column, final Pred hypothesis) {
		super(line, column);
		this.hypothesis = hypothesis;
	}
	
	@Override
	public <R, A> R accept(PestVisitor<R, A> v, A arg) {
		return v.visit(this, arg);
	}
}
