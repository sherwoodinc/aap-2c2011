package budapest.pest.pesttocvc3;

import java.util.HashSet;
import java.util.Set;

import budapest.pest.ast.exp.ArithIntExp;
import budapest.pest.ast.exp.ArrayAccessIntExp;
import budapest.pest.ast.exp.ArraySizeIntExp;
import budapest.pest.ast.exp.BinBoolExp;
import budapest.pest.ast.exp.LiteralIntExp;
import budapest.pest.ast.exp.NegIntExp;
import budapest.pest.ast.exp.NotBoolExp;
import budapest.pest.ast.exp.RelBoolExp;
import budapest.pest.ast.exp.VarIntExp;
import budapest.pest.ast.visitor.ExpVisitor;

public class ExpVarGetter extends ExpVisitor<Set<String>, Void> {

	public Set<String> visit(ArithIntExp n, Void arg) {
		Set<String> usedVars = new HashSet<String>();
		usedVars.addAll(n.left.accept(this, arg));
		usedVars.addAll(n.right.accept(this, arg));
		return usedVars;
	}
	
	public Set<String> visit(ArrayAccessIntExp n, Void arg) {
		Set<String> usedVars = new HashSet<String>();
		usedVars.add(n.array);
		usedVars.addAll(n.index.accept(this, arg));
		return usedVars;
	}
	
	public Set<String> visit(ArraySizeIntExp n, Void arg) {
		Set<String> usedVars = new HashSet<String>();
		usedVars.add(n.array);
		return usedVars;
	}
	
	public Set<String> visit(LiteralIntExp n, Void arg) {
		return new HashSet<String>();
	}
	
	public Set<String> visit(NegIntExp n, Void arg) {
		Set<String> usedVars = new HashSet<String>();
		usedVars.addAll(n.subExp.accept(this, arg));
		return usedVars;
	}
	
	public Set<String> visit(VarIntExp n, Void arg) {
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
