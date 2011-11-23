package budapest.pest.pesttocvc3;

import java.util.HashSet;
import java.util.Set;

import budapest.pest.ast.exp.ArithTopExp;
import budapest.pest.ast.exp.BinBoolExp;
import budapest.pest.ast.exp.LiteralIntExp;
import budapest.pest.ast.exp.NegTopExp;
import budapest.pest.ast.exp.NotBoolExp;
import budapest.pest.ast.exp.RelBoolExp;
import budapest.pest.ast.exp.VarExp;
import budapest.pest.ast.visitor.ExpVisitor;

public class ExpVarGetter extends ExpVisitor<Set<String>, Void> {

	public Set<String> visit(ArithTopExp n, Void arg) {
		Set<String> usedVars = new HashSet<String>();
		usedVars.addAll(n.left.accept(this, arg));
		usedVars.addAll(n.right.accept(this, arg));
		return usedVars;
	}
	
	public Set<String> visit(LiteralIntExp n, Void arg) {
		return new HashSet<String>();
	}
	
	public Set<String> visit(NegTopExp n, Void arg) {
		Set<String> usedVars = new HashSet<String>();
		usedVars.addAll(n.subExp.accept(this, arg));
		return usedVars;
	}
	
	public Set<String> visit(VarExp n, Void arg) {
		Set<String> usedVars = new HashSet<String>();
		usedVars.add(n.name);
		return usedVars;
	}
	
	public Set<String> visit(BinBoolExp n, Void arg) {
		Set<String> usedVars = new HashSet<String>();
		usedVars.addAll(n.left.accept(this, arg));
		usedVars.addAll(n.right.accept(this, arg));
		return usedVars;
	}
	
	public Set<String> visit(NotBoolExp n, Void arg) {
		Set<String> usedVars = new HashSet<String>();
		usedVars.addAll(n.subExp.accept(this, arg));
		return usedVars;
	}
	
	public Set<String> visit(RelBoolExp n, Void arg) {
		Set<String> usedVars = new HashSet<String>();
		usedVars.addAll(n.left.accept(this, arg));
		usedVars.addAll(n.right.accept(this, arg));
		return usedVars;
	}

}
