package budapest.pest.ast.stmt;

import budapest.pest.ast.exp.Exp;
import budapest.pest.ast.exp.VarIntExp;
import budapest.pest.ast.visitor.PestVisitor;

public final class AssignStmt extends Stmt {

	public final VarIntExp left; 
	
	public final Exp right;

	public AssignStmt(int line, int column, 
			final VarIntExp left, final Exp right) {
		super(line, column);
		this.left = left;
		this.right = right;
	}
	
	@Override
	public <R, A> R accept(PestVisitor<R, A> v, A arg) {
		return v.visit(this, arg);
	}
}
