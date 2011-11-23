package budapest.pest.ast.exp;

import budapest.pest.ast.visitor.ExpVisitor;
import budapest.util.printer.prec.Precedence;

public class SizeTopExp extends TopExp {

	public final TopExp subExp;

	public SizeTopExp(int line, int column, final TopExp subExp) {
		super(line, column);
		this.subExp = subExp;
	}
	
	@Override
	public <R, A> R accept(ExpVisitor<R, A> v, A arg) {
		return v.visit(this, arg);
	}
	
	public Precedence getPrecedence() {
		return Precedence.ATOM_TERM;
	}
	
}
