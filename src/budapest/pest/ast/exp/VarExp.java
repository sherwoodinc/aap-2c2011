package budapest.pest.ast.exp;

import budapest.pest.ast.visitor.ExpVisitor;
import budapest.util.printer.prec.Precedence;

public class VarExp extends TopExp {

	public final String name;

	public VarExp(int line, int column, final String name) {
		super(line, column);
		this.name = name;
	}
	
	@Override
	public <R, A> R accept(ExpVisitor<R, A> v, A arg) {
		return v.visit(this, arg);
	}

	public Precedence getPrecedence() {
		return Precedence.ATOM_TERM;
	}
	
}
