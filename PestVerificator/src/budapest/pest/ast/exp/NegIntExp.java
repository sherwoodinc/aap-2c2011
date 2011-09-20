package budapest.pest.ast.exp;

import budapest.pest.ast.visitor.ExpVisitor;
import budapest.util.printer.prec.Precedence;

public final class NegIntExp extends IntExp {
	
	public final IntExp subExp;

	public NegIntExp(int line, int column, final IntExp subExp) {
		super(line, column);
		this.subExp = subExp;
	}

	public Precedence getPrecedence() {
		return Precedence.UNARY_TERM;
	}
	
	@Override
	public <R, A> R accept(ExpVisitor<R, A> v, A arg) {
		return v.visit(this, arg);
	}
	
}
