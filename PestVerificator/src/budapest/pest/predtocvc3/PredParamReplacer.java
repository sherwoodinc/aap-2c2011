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

// TODO: Auto-generated Javadoc
/**
 * Este visitor de Predicados reemplaza nombres de variables por sus Terms correspondientes
 * El Map parámetro es el binding de los nombres de variables a Terms.
 */
public class PredParamReplacer extends PredVisitor<Pred, Map<String,Trm>> {
	
	/* (non-Javadoc)
	 * @see budapest.pest.ast.visitor.PredVisitor#visit(budapest.pest.ast.pred.BinaryPred, java.lang.Object)
	 */
	public Pred visit(BinaryPred n, Map<String,Trm> arg) {
		return new BinaryPred(n.line,
				n.column,
				n.left.accept(this, arg),
				n.op,
				n.right.accept(this, arg));
	}
	
	/* (non-Javadoc)
	 * @see budapest.pest.ast.visitor.PredVisitor#visit(budapest.pest.ast.pred.BooleanLiteralPred, java.lang.Object)
	 */
	public Pred visit(BooleanLiteralPred n, Map<String,Trm> arg) {
		return new BooleanLiteralPred(n.line, n.column, n.type);
	}
	
	/* (non-Javadoc)
	 * @see budapest.pest.ast.visitor.PredVisitor#visit(budapest.pest.ast.pred.NotPred, java.lang.Object)
	 */
	public Pred visit(NotPred n, Map<String,Trm> arg) {
		return new NotPred(n.line, n.column, n.subPred.accept(this, arg));
	}
	
	/* (non-Javadoc)
	 * @see budapest.pest.ast.visitor.PredVisitor#visit(budapest.pest.ast.pred.QuantifiedPred, java.lang.Object)
	 */
	public Pred visit(QuantifiedPred n, Map<String,Trm> arg) {
		return new QuantifiedPred(n.line,
				n.column,
				n.type,
				n.var,
				n.lowerBound != null ?  n.lowerBound.accept(new TrmParamReplacer(), arg) : null,
			    n.upperBound != null ?  n.upperBound.accept(new TrmParamReplacer(), arg) : null,
				n.subPred.accept(this, arg));
	}
	
	/* (non-Javadoc)
	 * @see budapest.pest.ast.visitor.PredVisitor#visit(budapest.pest.ast.pred.RelationPred, java.lang.Object)
	 */
	public Pred visit(RelationPred n, Map<String,Trm> arg) {
		return new RelationPred(n.line,
				n.column,
				n.left.accept(new TrmParamReplacer(), arg),
				n.op,
				n.right.accept(new TrmParamReplacer(), arg));
	}

}
