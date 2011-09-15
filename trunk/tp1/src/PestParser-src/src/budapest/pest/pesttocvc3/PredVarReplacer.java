package budapest.pest.pesttocvc3;

import budapest.pest.ast.pred.BinaryPred;
import budapest.pest.ast.pred.BooleanLiteralPred;
import budapest.pest.ast.pred.NotPred;
import budapest.pest.ast.pred.Pred;
import budapest.pest.ast.pred.QuantifiedPred;
import budapest.pest.ast.pred.RelationPred;
import budapest.pest.ast.visitor.PredVisitor;

public class PredVarReplacer extends PredVisitor<Pred, VarReplacement> {
	
	public Pred visit(BinaryPred n, VarReplacement arg) {
		return new BinaryPred(n.line,
				n.column,
				n.left.accept(this, arg),
				n.op,
				n.right.accept(this, arg));
	}
	
	public Pred visit(BooleanLiteralPred n, VarReplacement arg) {
		return new BooleanLiteralPred(n.line, n.column, n.type);
	}
	
	public Pred visit(NotPred n, VarReplacement arg) {
		return new NotPred(n.line, n.column, n.subPred.accept(this, arg));
	}
	
	public Pred visit(QuantifiedPred n, VarReplacement arg) {
		return new QuantifiedPred(n.line,
				n.column,
				n.type,
				arg.execute(n.var),
				n.lowerBound != null ?  n.lowerBound.accept(new TrmVarReplacer(), arg) : null,
			    n.upperBound != null ?  n.upperBound.accept(new TrmVarReplacer(), arg) : null,
				n.subPred.accept(this, arg));
	}
	
	public Pred visit(RelationPred n, VarReplacement arg) {
		return new RelationPred(n.line,
				n.column,
				n.left.accept(new TrmVarReplacer(), arg),
				n.op,
				n.right.accept(new TrmVarReplacer(), arg));
	}
	
}
