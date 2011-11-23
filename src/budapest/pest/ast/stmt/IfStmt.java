package budapest.pest.ast.stmt;

import budapest.pest.ast.exp.BoolExp;
import budapest.pest.ast.visitor.PestVisitor;

public final class IfStmt extends Stmt {

	public final BoolExp condition;
	
	public final Stmt thenS;
	public final Stmt elseS;
	
	public IfStmt(int line, int column, final BoolExp condition, 
			final Stmt thenS, final Stmt elseS) {
		super(line, column);
		this.condition = condition;
		this.thenS = thenS;
		this.elseS = elseS;
	}
	
	@Override
	public <R, A> R accept(PestVisitor<R, A> v, A arg) {
		return v.visit(this, arg);
	}

}
