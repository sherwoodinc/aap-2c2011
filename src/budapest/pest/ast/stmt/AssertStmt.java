package budapest.pest.ast.stmt;

import budapest.pest.ast.pred.Pred;
import budapest.pest.ast.visitor.PestVisitor;

public class AssertStmt extends Stmt {

	public final Pred query;

	public AssertStmt(int line, int column, final Pred query) {
		super(line, column);
		this.query = query;
	}
	
	@Override
	public <R, A> R accept(PestVisitor<R, A> v, A arg) {
		return v.visit(this, arg);
	}
	
}
