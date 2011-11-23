package budapest.pest.ast.pred.trm;

import budapest.pest.ast.visitor.TrmVisitor;
import budapest.pest.dump.pest.TrmPrinter;
import budapest.util.ASTNode;
import budapest.util.printer.prec.IPrecedenceable;

public abstract class Trm extends ASTNode 
	implements IPrecedenceable {

	public static enum Type { PRE_VALUE, CURR_VALUE }
	
	public Trm(int line, int column) {
		super(line, column);
	}

	public abstract <R,A> R accept(TrmVisitor<R,A> v, A arg);

	@Override
	public String toString() {
		return accept(new TrmPrinter(), null);
	}
	
}
