package budapest.pest.predtocvc3;

import budapest.pest.ast.pred.BinaryPred;
import budapest.pest.ast.pred.BooleanLiteralPred;
import budapest.pest.ast.pred.NotPred;
import budapest.pest.ast.pred.Pred;
import budapest.pest.ast.pred.QuantifiedPred;
import budapest.pest.ast.pred.RelationPred;
import budapest.pest.ast.visitor.PredVisitor;
import budapest.pest.pesttocvc3.PestVarContext;

public class PredVarReplacer extends PredVisitor<Pred, PestVarContext> {
	
	public Pred visit(BinaryPred n, PestVarContext arg) {
		return new BinaryPred(n.line,
				n.column,
				n.left.accept(this, arg),
				n.op,
				n.right.accept(this, arg));
	}
	
	public Pred visit(BooleanLiteralPred n, PestVarContext arg) {
		return new BooleanLiteralPred(n.line, n.column, n.type);
	}
	
	public Pred visit(NotPred n, PestVarContext arg) {
		return new NotPred(n.line, n.column, n.subPred.accept(this, arg));
	}
	
	public Pred visit(QuantifiedPred n, PestVarContext arg) {
		return new QuantifiedPred(n.line,
				n.column,
				n.type,
				arg.getInstanceOf(n.var),
				n.lowerBound != null ?  n.lowerBound.accept(new TrmVarReplacer(), arg) : null,
			    n.upperBound != null ?  n.upperBound.accept(new TrmVarReplacer(), arg) : null,
				n.subPred.accept(this, arg));
	}
	
	public Pred visit(RelationPred n, PestVarContext arg) {
		return new RelationPred(n.line,
				n.column,
				n.left.accept(new TrmVarReplacer(), arg),
				n.op,
				n.right.accept(new TrmVarReplacer(), arg));
	}
	
}
