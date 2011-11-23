package budapest.pest.ast.pred.trm;

import budapest.pest.ast.visitor.TrmVisitor;
import budapest.util.printer.prec.Precedence;

public final class IntegerLiteralTrm extends Trm {

	public final int value;

	public IntegerLiteralTrm(int line, int column, final int value) {
		super(line, column);
		this.value = value;
	}
	
	@Override
	public <R, A> R accept(TrmVisitor<R, A> v, A arg) {
		return v.visit(this, arg);
	}
	
	public Precedence getPrecedence() {
		return Precedence.ATOM_TERM;
	}

}
