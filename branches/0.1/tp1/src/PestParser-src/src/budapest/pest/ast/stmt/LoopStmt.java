package budapest.pest.ast.stmt;

import budapest.pest.ast.exp.Exp;
import budapest.pest.ast.pred.Pred;
import budapest.pest.ast.pred.trm.Trm;
import budapest.pest.ast.visitor.PestVisitor;

public final class LoopStmt extends Stmt {

	public final Exp condition;
	
	public final Pred invariant;
	public final Trm variant;
	
	public final Stmt body;
	
	public LoopStmt(int line, int column, final Exp condition, 
			final Pred invariant, final Trm variant, final Stmt body) {
		super(line, column);
		this.condition = condition;
		this.invariant = invariant;
		this.variant = variant;
		this.body = body;
	}

	@Override
	public <R, A> R accept(PestVisitor<R, A> v, A arg) {
		return v.visit(this, arg);
	}

}
