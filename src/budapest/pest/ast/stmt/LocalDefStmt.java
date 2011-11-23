package budapest.pest.ast.stmt;

import budapest.pest.ast.exp.Exp;
import budapest.pest.ast.exp.VarExp;
import budapest.pest.ast.visitor.PestVisitor;

public final class LocalDefStmt extends Stmt {

	public final VarExp left;
	public final Exp right;
	
	public LocalDefStmt(int line, int column, final VarExp left, 
			final Exp right) {
		super(line, column);
		this.left = left;
		this.right = right;
	}

	@Override
	public <R, A> R accept(PestVisitor<R, A> v, A arg) {
		return v.visit(this, arg);
	}
	
}
