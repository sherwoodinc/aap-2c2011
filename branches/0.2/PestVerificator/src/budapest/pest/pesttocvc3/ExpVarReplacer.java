package budapest.pest.pesttocvc3;

import budapest.pest.ast.exp.ArithIntExp;
import budapest.pest.ast.exp.ArrayAccessIntExp;
import budapest.pest.ast.exp.ArraySizeIntExp;
import budapest.pest.ast.exp.BinBoolExp;
import budapest.pest.ast.exp.BoolExp;
import budapest.pest.ast.exp.Exp;
import budapest.pest.ast.exp.IntExp;
import budapest.pest.ast.exp.LiteralIntExp;
import budapest.pest.ast.exp.NegIntExp;
import budapest.pest.ast.exp.NotBoolExp;
import budapest.pest.ast.exp.RelBoolExp;
import budapest.pest.ast.exp.VarIntExp;
import budapest.pest.ast.visitor.ExpVisitor;

public class ExpVarReplacer extends ExpVisitor<Exp, PestVarContext> {

	public IntExp visit(ArithIntExp n, PestVarContext arg) {
		return new ArithIntExp(n.line,
				n.column,
				(IntExp) n.left.accept(this, arg),
				n.op,
				(IntExp) n.right.accept(this, arg));
	}
	
	public IntExp visit(ArrayAccessIntExp n, PestVarContext arg) {
		return new ArrayAccessIntExp(n.line,
				n.column,
				arg.addNewInstanceOf(n.array),
				(IntExp) n.index.accept(this, arg));
	}
	
	public IntExp visit(ArraySizeIntExp n, PestVarContext arg) {
		return new ArraySizeIntExp(n.line,
				n.column,
				arg.getInstanceOf(n.array));
	}
	
	public IntExp visit(LiteralIntExp n, PestVarContext arg) {
		return new LiteralIntExp(n.line,
				n.column,
				n.value);
	}
	
	public IntExp visit(NegIntExp n, PestVarContext arg) {
		return new NegIntExp(n.line,
				n.column,
				(IntExp)n.subExp.accept(this, arg));
	}
	
	public IntExp visit(VarIntExp n, PestVarContext arg) {
		return new VarIntExp(n.line,
				n.column,
				arg.getInstanceOf(n.name));
	}
	
	public BoolExp visit(BinBoolExp n, PestVarContext arg) {
		return new BinBoolExp(n.line,
				n.column,
				(BoolExp) n.left.accept(this, arg),
				n.op,
				(BoolExp) n.right.accept(this, arg));
	}
	
	public BoolExp visit(NotBoolExp n, PestVarContext arg) {
		return new NotBoolExp(n.line,
				n.column,
				(BoolExp) n.subExp.accept(this, arg));
	}
	
	public BoolExp visit(RelBoolExp n, PestVarContext arg) {
		return new RelBoolExp(n.line,
				n.column,
				(IntExp) n.left.accept(this, arg),
				n.op,
				(IntExp) n.right.accept(this, arg));
	}
	
}
