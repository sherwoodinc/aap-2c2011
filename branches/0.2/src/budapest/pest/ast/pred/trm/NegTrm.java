package budapest.pest.ast.pred.trm;

import budapest.pest.ast.visitor.TrmVisitor;
import budapest.util.printer.prec.Precedence;

public final class NegTrm extends Trm {

	public final Trm subTrm;

	public NegTrm(int line, int column, final Trm subTerm) {
		super(line, column);
		this.subTrm = subTerm;
	}

	@Override
	public <R, A> R accept(TrmVisitor<R, A> v, A arg) {
		return v.visit(this, arg);
	}
	
	public Precedence getPrecedence() {
		return Precedence.UNARY_TERM;
	}
	
}