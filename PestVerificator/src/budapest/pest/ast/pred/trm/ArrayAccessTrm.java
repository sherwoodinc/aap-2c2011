package budapest.pest.ast.pred.trm;

import budapest.pest.ast.visitor.TrmVisitor;
import budapest.util.printer.prec.Precedence;

public final class ArrayAccessTrm extends Trm {

	public final String array;
	public final Trm index;
	
	public final Type type;
	
	public ArrayAccessTrm(int line, int column, final String array, 
			final Trm index, Type type) {
		super(line, column);
		this.array = array;
		this.index = index;
		this.type = type;
	}
	
	@Override
	public <R, A> R accept(TrmVisitor<R, A> v, A arg) {
		return v.visit(this, arg);
	}

	public Precedence getPrecedence() {
		return Precedence.ATOM_TERM;
	}
	
}
