package budapest.pest.ast.pred;

import budapest.pest.ast.visitor.PredVisitor;
import budapest.pest.dump.pest.PredPrinter;
import budapest.util.ASTNode;
import budapest.util.printer.prec.IPrecedenceable;

public abstract class Pred extends ASTNode 
	implements IPrecedenceable {

	public Pred(int line, int column) {
		super(line, column);
	}
	
	public abstract <R,A> R accept(PredVisitor<R, A> v, A arg);
	
	@Override
	public String toString() {
		return accept(new PredPrinter(), null);
	}
	
}
