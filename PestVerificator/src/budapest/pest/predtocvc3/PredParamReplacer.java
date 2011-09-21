package budapest.pest.predtocvc3;

import java.util.Map;

import budapest.pest.ast.pred.BinaryPred;
import budapest.pest.ast.pred.BooleanLiteralPred;
import budapest.pest.ast.pred.NotPred;
import budapest.pest.ast.pred.Pred;
import budapest.pest.ast.pred.QuantifiedPred;
import budapest.pest.ast.pred.RelationPred;
import budapest.pest.ast.pred.trm.Trm;
import budapest.pest.ast.visitor.PredVisitor;

public class PredParamReplacer extends PredVisitor<Pred, Map<String,Trm>> {
	
	public Pred visit(BinaryPred n, Map<String,Trm> arg) {
		return new BinaryPred(n.line,
				n.column,
				n.left.accept(this, arg),
				n.op,
				n.right.accept(this, arg));
	}
	
	public Pred visit(BooleanLiteralPred n, Map<String,Trm> arg) {
		return new BooleanLiteralPred(n.line, n.column, n.type);
	}
	
	public Pred visit(NotPred n, Map<String,Trm> arg) {
		return new NotPred(n.line, n.column, n.subPred.accept(this, arg));
	}
	
	public Pred visit(QuantifiedPred n, Map<String,Trm> arg) {
		return new QuantifiedPred(n.line,
				n.column,
				n.type,
				n.var,
				n.lowerBound != null ?  n.lowerBound.accept(new TrmParamReplacer(), arg) : null,
			    n.upperBound != null ?  n.upperBound.accept(new TrmParamReplacer(), arg) : null,
				n.subPred.accept(this, arg));
	}
	
	public Pred visit(RelationPred n, Map<String,Trm> arg) {
		return new RelationPred(n.line,
				n.column,
				n.left.accept(new TrmParamReplacer(), arg),
				n.op,
				n.right.accept(new TrmParamReplacer(), arg));
	}

}
