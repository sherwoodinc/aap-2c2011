package budapest.pest.ast.visitor;

import budapest.pest.ast.pred.BinaryPred;
import budapest.pest.ast.pred.BooleanLiteralPred;
import budapest.pest.ast.pred.QuantifiedPred;
import budapest.pest.ast.pred.Pred;
import budapest.pest.ast.pred.RelationPred;
import budapest.pest.ast.pred.NotPred;

public abstract class PredVisitor<R,A> {

	// basic pred
	public R visit(Pred n, A arg) {
		throw new IllegalStateException(n.getClass().getName());
	}
	
	// concrete preds
	public R visit(BinaryPred n, A arg) {
		n.left.accept(this, arg);
		n.right.accept(this, arg);
		return null;
	}
	
	public R visit(QuantifiedPred n, A arg) {
		n.subPred.accept(this, arg);
		return null;
	}
	
	public R visit(BooleanLiteralPred n, A arg) {
		return null;
	}
	
	public R visit(RelationPred n, A arg) {
		return null;
	}
	
	public R visit(NotPred n, A arg) {
		n.subPred.accept(this, arg);
		return null;
	}
	
}
