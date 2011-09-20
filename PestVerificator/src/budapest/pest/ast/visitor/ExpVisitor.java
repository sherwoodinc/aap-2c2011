package budapest.pest.ast.visitor;

import budapest.pest.ast.exp.ArithIntExp;
import budapest.pest.ast.exp.ArrayAccessIntExp;
import budapest.pest.ast.exp.ArraySizeIntExp;
import budapest.pest.ast.exp.BinBoolExp;
import budapest.pest.ast.exp.Exp;
import budapest.pest.ast.exp.LiteralIntExp;
import budapest.pest.ast.exp.NegIntExp;
import budapest.pest.ast.exp.RelBoolExp;
import budapest.pest.ast.exp.NotBoolExp;
import budapest.pest.ast.exp.VarIntExp;

public abstract class ExpVisitor<R,A> {

	// basic exp
	public R visit(Exp n, A arg) {
		throw new IllegalStateException(n.getClass().getName());
	}
	
	// int exps
	public R visit(ArithIntExp n, A arg) {
		n.left.accept(this, arg);
		n.right.accept(this, arg);
		return null;
	}
	
	public R visit(ArraySizeIntExp n, A arg) {
		return null;
	}
	
	public R visit(ArrayAccessIntExp n, A arg) {
		n.index.accept(this, arg);
		return null;
	}
	
	public R visit(LiteralIntExp n, A arg) {
		return null;
	}
	
	public R visit(VarIntExp n, A arg) {
		return null;
	}

	public R visit(NegIntExp n, A arg) {
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
	
}
