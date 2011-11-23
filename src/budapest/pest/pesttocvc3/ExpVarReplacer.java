package budapest.pest.pesttocvc3;

import budapest.pest.ast.exp.ArithTopExp;
import budapest.pest.ast.exp.BinBoolExp;
import budapest.pest.ast.exp.BoolExp;
import budapest.pest.ast.exp.Exp;
import budapest.pest.ast.exp.LiteralIntExp;
import budapest.pest.ast.exp.NegTopExp;
import budapest.pest.ast.exp.NotBoolExp;
import budapest.pest.ast.exp.RelBoolExp;
import budapest.pest.ast.exp.TopExp;
import budapest.pest.ast.exp.VarExp;
import budapest.pest.ast.visitor.ExpVisitor;

public class ExpVarReplacer extends ExpVisitor<Exp, PestVarContext> {

	public TopExp visit(ArithTopExp n, PestVarContext arg) {
		return new ArithTopExp(n.line,
				n.column,
				(TopExp) n.left.accept(this, arg),
				n.op,
				(TopExp) n.right.accept(this, arg));
	}
	
	public TopExp visit(LiteralIntExp n, PestVarContext arg) {
		return new LiteralIntExp(n.line,
				n.column,
				n.value);
	}
	
	public TopExp visit(NegTopExp n, PestVarContext arg) {
		return new NegTopExp(n.line,
				n.column,
				(TopExp)n.subExp.accept(this, arg));
	}
	
	public VarExp visit(VarExp n, PestVarContext arg) {
		return new VarExp(n.line,
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
				(TopExp) n.left.accept(this, arg),
				n.op,
				(TopExp) n.right.accept(this, arg));
	}
	
}
