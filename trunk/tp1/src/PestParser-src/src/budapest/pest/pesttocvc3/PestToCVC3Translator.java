package budapest.pest.pesttocvc3;

import java.util.TreeSet;

import budapest.pest.ast.pred.trm.Trm;
import budapest.pest.ast.pred.BinaryPred;
import budapest.pest.ast.pred.NotPred;
import budapest.pest.ast.pred.Pred;
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
import budapest.pest.dump.pest.PredPrinter;
import budapest.pest.dump.pest.TrmPrinter;
import budapest.pest.predtocvc3.PredToCVC3Translator;
import budapest.pest.predtocvc3.PredVarManager;
import budapest.pest.predtocvc3.PredVarReplacer;
import budapest.pest.predtocvc3.TrmVarReplacer;
import budapest.pest.pesttocvc3.PestVarContext;

public final class PestToCVC3Translator extends PestVisitor<Pred, Pred> {

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
		// Crear contexto con vars
		PestVarContext ctxInicial; 
		if (context != null)
			ctxInicial = new PestVarContext(context);
		else
			ctxInicial = new PestVarContext();

		for (String param: n.paramNames())
		{
			// A la vez agrego parametros al context y hagoel output 
			System.out.println(ctxInicial.setVarAssignment(param)+": INT;");
		}

		// Creamos context hijo para la recursion
		context = new PestVarContext(ctxInicial);		

		// Reemplazar en el pre e imprimir, con el context inicial 
		Pred pre = ctxInicial.translate(n.pre);
		System.out.println("ASSERT (" + pre.accept(new PredToCVC3Translator(), null) + ");");
		// Creo un context hijo
		context = new PestVarContext(context);
		Pred ret = n.stmt.accept(this, requires);
		// Reemplazar en el post e imprimr 
		Pred post = context.translate(n.post);
		System.out.println("QUERY (" + post.accept(new PredToCVC3Translator(), null) + ");");
		
		return ret;
	}

	public Pred visit(AssignStmt n, Pred p) {			
		
		String var = n.left.name;
		String freshVar = new PredVarManager().getFreshVar(p);

		Trm rightAsTrm = n.right.accept(new ExpToTrmTranslator(), null);
		
		//E[x->x']
		Trm replacedTrm = rightAsTrm.accept(new TrmVarReplacer(), new VarReplacement(var, freshVar));
		
		// Reemplazo el lado derecho y luego actualizo el context.
		Trm replacedTrmc = context.translate(rightAsTrm);
		String newVar = context.setVarAssignment(var);		
		System.out.println(newVar + ": INT;");
		System.out.println("ASSERT ("+newVar+" = "+replacedTrmc.accept(new TrmPrinter(), null)+");");
		
		// Lo que sigue ya no hace falta en la version contexts...

		//A[x->x']
		Pred left = p.accept(new PredVarReplacer(), new VarReplacement(var, freshVar));

		//x==E[x->x']
		Pred right = new RelationPred(p.line,
				p.column,
				new VarTrm(p.line, p.column, var, Trm.Type.CURR_VALUE),
				RelationPred.Operator.EQ,
				replacedTrm);

		//A[x->x'] && x==E[x->x']
		return new BinaryPred(p.line,
				p.column,
				left,
				BinaryPred.Operator.AND,
				right);
	}

	public Pred visit(SeqStmt n, Pred p) {
		Pred s1 = n.s1.accept(this, p);
		return n.s2.accept(this, s1);
	}

	public Pred visit(SkipStmt n, Pred p) {
		return p;
	}

	public Pred visit(IfStmt n, Pred p) {
		
		// Create contexts
		PestVarContext ctxInicial = context;
		PestVarContext ctxThen = new PestVarContext(context);
		PestVarContext ctxElse = new PestVarContext(context);
			
		//Condition as Pred...
		Pred conditionPred = n.condition.accept(new ExpToPredTranslator(), null);

		//Translate the context
		Pred conditionPredc = context.translate(conditionPred);
		
		// Switch to the Then context
		context = ctxThen;

		//Condition && Post(ThenS)
		Pred andLeft = new BinaryPred(p.line,
				p.column,
				conditionPred,
				BinaryPred.Operator.AND,
				n.thenS.accept(this, p));

		Pred ret, andRight;
		
		if( n.elseS instanceof SkipStmt ){
			//If without Else
			andRight = new BinaryPred(p.line,
					p.column,
					new NotPred(p.line, p.column, conditionPred),
					BinaryPred.Operator.AND,
					p);
		}else{
		
			// Switch to the Else context
			context = ctxElse;

			//!Condition && Post(ElseS)
			andRight = new BinaryPred(p.line,
					p.column,
					new NotPred(p.line, p.column, conditionPred),
					BinaryPred.Operator.AND,
					n.elseS.accept(this, p));
		}	
	
		// Join de los contextos en el contexto original (TODO)
		TreeSet<String> valuations = ctxThen.localVarNames();
				
		// Restauro el contexto original
		context = ctxInicial;
		
		//(Condition && Post(ThenS)) OR (!Condition && Post(ElseS))
		ret = new BinaryPred(p.line,
					p.column,
					andLeft,
					BinaryPred.Operator.OR,
					andRight);
		
		return ret;
	}
	
	public Pred visit(BlockStmt n, Pred p){
		return n.stmt.accept(this, p);
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
	
	public Pred visit(CallStmt n, Pred d){
		//TODO
		return d;
	}
	
	public Pred visit(LocalDefStmt n, Pred d){
		//TODO
		return d;
	}
	
	protected PestVarContext context;  
}
