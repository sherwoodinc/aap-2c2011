package budapest.pest.pesttocvc3;

import budapest.pest.ast.exp.ArithTopExp;
import budapest.pest.ast.exp.LiteralIntExp;
import budapest.pest.ast.exp.NegTopExp;
import budapest.pest.ast.exp.VarExp;
import budapest.pest.ast.pred.trm.BinaryTrm;
import budapest.pest.ast.pred.trm.IntegerLiteralTrm;
import budapest.pest.ast.pred.trm.NegTrm;
import budapest.pest.ast.pred.trm.Trm;
import budapest.pest.ast.pred.trm.VarTrm;
import budapest.pest.ast.visitor.ExpVisitor;

public class ExpToTrmTranslator extends ExpVisitor<Trm, Void> {

	public Trm visit(ArithTopExp n, Void arg) {
		return new BinaryTrm(n.line,
				n.column,
				n.left.accept(this, arg),
				BinaryTrm.Operator.valueOf(n.op.toString()),
				n.right.accept(this, arg));
	}
	
	public Trm visit(LiteralIntExp n, Void arg) {
		return new IntegerLiteralTrm(n.line,
				n.column,
				n.value);
	}
	
	public Trm visit(NegTopExp n, Void arg) {
		return new NegTrm(n.line,
				n.column,
				n.subExp.accept(this, arg));
	}
	
	public Trm visit(VarExp n, Void arg) {
		return new VarTrm(n.line,
				n.column,
				n.name,
				Trm.Type.CURR_VALUE);
	}
}
