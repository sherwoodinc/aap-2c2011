package budapest.pest.pesttocvc3;

import budapest.pest.ast.exp.ArithIntExp;
import budapest.pest.ast.exp.ArrayAccessIntExp;
import budapest.pest.ast.exp.ArraySizeIntExp;
import budapest.pest.ast.exp.IntExp;
import budapest.pest.ast.exp.LiteralIntExp;
import budapest.pest.ast.exp.NegIntExp;
import budapest.pest.ast.exp.VarIntExp;
import budapest.pest.ast.visitor.ExpVisitor;

public class IntExpVarReplacer extends ExpVisitor<IntExp, VarReplacement> {

	public IntExp visit(ArithIntExp n, VarReplacement arg) {
		return new ArithIntExp(n.line,
				n.column,
				n.left.accept(this, arg),
				n.op,
				n.right.accept(this, arg));
	}
	
	public IntExp visit(ArrayAccessIntExp n, VarReplacement arg) {
		return new ArrayAccessIntExp(n.line,
				n.column,
				arg.execute(n.array),
				n.index.accept(this, arg));
	}
	
	public IntExp visit(ArraySizeIntExp n, VarReplacement arg) {
		return new ArraySizeIntExp(n.line,
				n.column,
				arg.execute(n.array));
	}
	
	public IntExp visit(LiteralIntExp n, VarReplacement arg) {
		return new LiteralIntExp(n.line,
				n.column,
				n.value);
	}
	
	public IntExp visit(NegIntExp n, VarReplacement arg) {
		return new NegIntExp(n.line,
				n.column,
				n.subExp.accept(this, arg));
	}
	
	public IntExp visit(VarIntExp n, VarReplacement arg) {
		return new VarIntExp(n.line,
				n.column,
				arg.execute(n.name));
	}

}
