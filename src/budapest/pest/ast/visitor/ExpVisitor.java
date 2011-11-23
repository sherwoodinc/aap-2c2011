package budapest.pest.ast.visitor;

import budapest.pest.ast.exp.ArithTopExp;
import budapest.pest.ast.exp.BinBoolExp;
import budapest.pest.ast.exp.Exp;
import budapest.pest.ast.exp.LiteralIntExp;
import budapest.pest.ast.exp.LiteralStringExp;
import budapest.pest.ast.exp.NegTopExp;
import budapest.pest.ast.exp.RelBoolExp;
import budapest.pest.ast.exp.NotBoolExp;
import budapest.pest.ast.exp.SizeTopExp;
import budapest.pest.ast.exp.VarExp;

public abstract class ExpVisitor<R,A> {

	// basic exp
	public R visit(Exp n, A arg) {
		throw new IllegalStateException(n.getClass().getName());
	}
	
	// int exps
	public R visit(ArithTopExp n, A arg) {
		n.left.accept(this, arg);
		n.right.accept(this, arg);
		return null;
	}
	
	public R visit(LiteralIntExp n, A arg) {
		return null;
	}
	
	public R visit(LiteralStringExp n, A arg) {
		return null;
	}
	
	public R visit(VarExp n, A arg) {
		return null;
	}

	public R visit(NegTopExp n, A arg) {
		n.subExp.accept(this, arg);
		return null;
	}
	
	// bool exps
	public R visit(RelBoolExp n, A arg) {
		n.left.accept(this, arg);
		n.right.accept(this, arg);
		return null;
	}
	
	public R visit(BinBoolExp n, A arg) {
		n.left.accept(this, arg);
		n.right.accept(this, arg);
		return null;
	}
	
	public R visit(NotBoolExp n, A arg) {
		n.subExp.accept(this, arg);
		return null;
	}
	
	public R visit(SizeTopExp n, A arg) {
		n.subExp.accept(this, arg);
		return null;
	}
}
