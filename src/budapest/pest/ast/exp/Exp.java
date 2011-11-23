package budapest.pest.ast.exp;

import budapest.pest.ast.visitor.ExpVisitor;
import budapest.pest.dump.pest.ExpPrinter;
import budapest.util.ASTNode;
import budapest.util.printer.prec.IPrecedenceable;

public abstract class Exp extends ASTNode
	implements IPrecedenceable {

	public Exp(int line, int column) {
		super(line, column);
	}

	public abstract <R,A> R accept(ExpVisitor<R,A> v, A arg);
	
	@Override
	public String toString() {
		return accept(new ExpPrinter(), null);
	}
	
}
