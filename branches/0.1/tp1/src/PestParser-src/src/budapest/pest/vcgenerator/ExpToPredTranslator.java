package budapest.pest.vcgenerator;

import budapest.pest.ast.exp.BinBoolExp;
import budapest.pest.ast.exp.NotBoolExp;
import budapest.pest.ast.exp.RelBoolExp;
import budapest.pest.ast.pred.BinaryPred;
import budapest.pest.ast.pred.NotPred;
import budapest.pest.ast.pred.Pred;
import budapest.pest.ast.pred.RelationPred;
import budapest.pest.ast.visitor.ExpVisitor;

public class ExpToPredTranslator extends ExpVisitor<Pred, Void> {

	public Pred visit(BinBoolExp n, Void arg) {
		return new BinaryPred(n.line,
				n.column,
				n.left.accept(this, arg),
				BinaryPred.Operator.valueOf(n.op.toString()),
				n.right.accept(this, arg));
	}
	
	public Pred visit(NotBoolExp n, Void arg) {
		return new NotPred(n.line,
				n.column,
				n.subExp.accept(this, arg));
	}
	
	public Pred visit(RelBoolExp n, Void arg) {
		return new RelationPred(n.line,
				n.column,
				n.left.accept(new ExpToTrmTranslator(), arg),
				RelationPred.Operator.valueOf(n.op.toString()),
				n.right.accept(new ExpToTrmTranslator(), arg));
	}
}
