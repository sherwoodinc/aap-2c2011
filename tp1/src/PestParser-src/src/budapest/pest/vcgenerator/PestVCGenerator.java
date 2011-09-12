package budapest.pest.vcgenerator;

import budapest.pest.ast.pred.trm.Trm;
import budapest.pest.ast.pred.BinaryPred;
import budapest.pest.ast.pred.NotPred;
import budapest.pest.ast.pred.Pred;
import budapest.pest.ast.pred.QuantifiedPred;
import budapest.pest.ast.pred.RelationPred;
import budapest.pest.ast.pred.trm.VarTrm;
import budapest.pest.ast.proc.Procedure;
import budapest.pest.ast.proc.Program;
import budapest.pest.ast.stmt.AssertStmt;
import budapest.pest.ast.stmt.AssignStmt;
import budapest.pest.ast.stmt.AssumeStmt;
import budapest.pest.ast.stmt.BlockStmt;
import budapest.pest.ast.stmt.CallStmt;
import budapest.pest.ast.stmt.IfStmt;
import budapest.pest.ast.stmt.LocalDefStmt;
import budapest.pest.ast.stmt.LoopStmt;
import budapest.pest.ast.stmt.SeqStmt;
import budapest.pest.ast.stmt.SkipStmt;
import budapest.pest.ast.visitor.PestVisitor;

public final class PestVCGenerator extends PestVisitor<Pred, Pred> {

	public Pred execute(Program n) {
		Procedure main = n.getMain();
		Pred computedPost = main.accept(this, main.pre);
		return new BinaryPred(computedPost.line,
				computedPost.column,
				computedPost,
				BinaryPred.Operator.IMPLIES,
				main.post);
	}

	public Pred visit(Procedure n, Pred requires) {
		return n.stmt.accept(this, requires);
	}

	public Pred visit(AssignStmt n, Pred p) {
		String var = n.left.name;
		String freshVar = new PredFreshVarGetter().execute(p);

		Trm rightAsTrm = n.right.accept(new ExpToTrmTranslator(), null);

		//E[x->x']
		Trm replacedTrm = rightAsTrm.accept(new TrmVarReplacer(), new VarReplacement(var, freshVar));

		//A[x->x']
		Pred left = p.accept(new PredVarReplacer(), new VarReplacement(var, freshVar));

		//x==E[x->x']
		Pred right = new RelationPred(p.line,
				p.column,
				new VarTrm(p.line, p.column, var, Trm.Type.CURR_VALUE),
				RelationPred.Operator.EQ,
				replacedTrm);

		//A[x->x'] && x==[E->x']
		Pred and = new BinaryPred(p.line,
				p.column,
				left,
				BinaryPred.Operator.AND,
				right);

		//EXISTS x'|A[x->x'] && x==E[x->x']
		Pred exists = new QuantifiedPred(p.line,
				p.column,
				QuantifiedPred.Type.EXISTS,
				var,
				null,
				null,
				and);

		return new QuantifiedPred(p.line,
				p.column,
				QuantifiedPred.Type.EXISTS,
				freshVar,
				null,
				null,
				exists);
	}

	public Pred visit(SeqStmt n, Pred p) {
		Pred s1 = n.s1.accept(this, p);
		return n.s2.accept(this, s1);
	}

	public Pred visit(SkipStmt n, Pred p) {
		return p;
	}

	public Pred visit(IfStmt n, Pred p) {
		//(Cond AND POS(IF)) OR (!COND AND POS(ELSE))
		Pred conditionPred = n.condition.accept(new ExpToPredTranslator(), null);

		Pred andLeft = new BinaryPred(p.line,
				p.column,
				conditionPred,
				BinaryPred.Operator.AND,
				n.thenS.accept(this, p));

		Pred andRight = new BinaryPred(p.line,
				p.column,
				new NotPred(p.line, p.column, conditionPred),
				BinaryPred.Operator.AND,
				n.elseS.accept(this, p));

		return new BinaryPred(p.line,
				p.column,
				andLeft,
				BinaryPred.Operator.OR,
				andRight);
	}

	public Pred visit(LoopStmt n, Pred d){
		//TODO
		return d;
	}
	
	public Pred visit(AssertStmt n, Pred d){
		//TODO
		return d;
	}
	
	public Pred visit(AssumeStmt n, Pred d){
		//TODO
		return d;
	}
	
	public Pred visit(BlockStmt n, Pred d){
		//TODO
		return d;
	}
	
	public Pred visit(CallStmt n, Pred d){
		//TODO
		return d;
	}
	
	public Pred visit(LocalDefStmt n, Pred d){
		//TODO
		return d;
	}
}
