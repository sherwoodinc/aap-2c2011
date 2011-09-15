package budapest.pest.pesttocvc3;

import budapest.pest.ast.exp.ArithIntExp;
import budapest.pest.ast.exp.ArrayAccessIntExp;
import budapest.pest.ast.exp.ArraySizeIntExp;
import budapest.pest.ast.exp.LiteralIntExp;
import budapest.pest.ast.exp.NegIntExp;
import budapest.pest.ast.exp.VarIntExp;
import budapest.pest.ast.pred.trm.ArrayAccessTrm;
import budapest.pest.ast.pred.trm.ArraySizeTrm;
import budapest.pest.ast.pred.trm.BinaryTrm;
import budapest.pest.ast.pred.trm.IntegerLiteralTrm;
import budapest.pest.ast.pred.trm.NegTrm;
import budapest.pest.ast.pred.trm.Trm;
import budapest.pest.ast.pred.trm.VarTrm;
import budapest.pest.ast.visitor.ExpVisitor;

public class ExpToTrmTranslator extends ExpVisitor<Trm, Void> {

	public Trm visit(ArithIntExp n, Void arg) {
		return new BinaryTrm(n.line,
				n.column,
				n.left.accept(this, arg),
				BinaryTrm.Operator.valueOf(n.op.toString()),
				n.right.accept(this, arg));
	}
	
	public Trm visit(ArrayAccessIntExp n, Void arg) {
		return new ArrayAccessTrm(n.line,
				n.column,
				n.array,
				n.index.accept(this, arg),
				Trm.Type.CURR_VALUE);
	}
	
	public Trm visit(ArraySizeIntExp n, Void arg) {
		return new ArraySizeTrm(n.line,
				n.column,
				n.array);
	}
	
	public Trm visit(LiteralIntExp n, Void arg) {
		return new IntegerLiteralTrm(n.line,
				n.column,
				n.value);
	}
	
	public Trm visit(NegIntExp n, Void arg) {
		return new NegTrm(n.line,
				n.column,
				n.subExp.accept(this, arg));
	}
	
	public Trm visit(VarIntExp n, Void arg) {
		return new VarTrm(n.line,
				n.column,
				n.name,
				Trm.Type.CURR_VALUE);
	}
}
