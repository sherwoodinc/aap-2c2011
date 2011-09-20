package budapest.pest.pesttocvc3;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeMap;
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
import budapest.pest.ast.stmt.Stmt;
import budapest.pest.ast.visitor.PestVisitor;
import budapest.pest.dump.pest.TrmPrinter;
import budapest.pest.predtocvc3.PredToCVC3Translator;
import budapest.pest.predtocvc3.PredVarManager;
import budapest.pest.predtocvc3.PredVarReplacer;
import budapest.pest.predtocvc3.TrmVarReplacer;
import budapest.pest.pesttocvc3.PestVarContext;

public final class PestToCVC3Translator extends PestVisitor<String, String> {

	public PestToCVC3Translator() {}
	
	public PestToCVC3Translator(PestVarContext cntxt) {
		context = cntxt;
	}
	
	public String execute(Program n) {
		Procedure main = n.getMain();
		return visit(main,"");//main.accept(this, "");
	}

	public String visit(Procedure n, String cvcin) {
		String cvcout = "";
		// Crear contexto con vars
		PestVarContext ctxInicial = new PestVarContext();
		context = new PestVarContext(ctxInicial);
		//PestVarContext ctxretorno = context;

		cvcout += "%%% PROC: " + n.name + "\n";
		cvcout += "%%% PARAMS:\n";
		for (String param: n.paramNames())
		{
			// A la vez agrego parametros al context y hago el output 
			cvcout += ctxInicial.setVarAssignment(param)+": INT;\n";
		}

		// Reemplazar en el pre e imprimir, con el context inicial 
		Pred pre = context.translate(n.pre);
		cvcout += "%%% PRE: " + pre.accept(new PredToCVC3Translator(), null)+"\n";
		cvcout += "ASSERT (" + pre.accept(new PredToCVC3Translator(), null) + ");\n";
		
		statements = n.stmt;
		precondition = n.pre;
		postcondition = n.post;
		cvcout += n.stmt.accept(this, null);
				
		// Reemplazar en el post e imprimr 
		Pred post = context.translate(n.post);
		String postStr = "%%% POST: " + post.accept(new PredToCVC3Translator(), null) + "\n" +
						 "QUERY (" + post.accept(new PredToCVC3Translator(), null) + ");\n";
		
		// Replace @pre
		for (String param: n.paramNames())
		{
			try {
				postStr = postStr.replace(context.getVarAssignment(param)+"@pre", ctxInicial.getVarAssignment(param));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		cvcout += postStr;
				
		return cvcout;
	}

	public String visit(AssignStmt n, String cvcin) {			
		String cvcout = "";
		String var = n.left.name;

		Trm rightAsTrm = n.right.accept(new ExpToTrmTranslator(), null);
			
		// Reemplazo el lado derecho y luego actualizo el context.
		Trm replacedTrmc = context.translate(rightAsTrm);
		String newVar = context.setVarAssignment(var);
		context.setWasAssigned(var);
		cvcout += newVar + ": INT;\n";
		cvcout += "ASSERT ("+newVar+" = "+replacedTrmc.accept(new TrmPrinter(), null)+");\n";
		
		return cvcout;
		
	}

	public String visit(SeqStmt n, String cvcin) {
		String s1 = n.s1.accept(this, null);
		return s1 + n.s2.accept(this, null);
	}

	public String visit(SkipStmt n, String cvcin) {
		return "\n";
	}

	public String visit(IfStmt n, String cvcin) {
		String cvcout = "";
				
		// Create contexts
		PestVarContext ctxReturn = context;
		PestVarContext ctxThen = new PestVarContext(context);
		PestVarContext ctxElse = new PestVarContext(context);
			
		//Condition as Pred...
		Pred conditionPred = n.condition.accept(new ExpToPredTranslator(), null);

		//Translate the context
		Pred conditionPredc = context.translate(conditionPred);
		String Cond = conditionPredc.accept(new PredToCVC3Translator(), null);
		cvcout += "%%% -> IF " + Cond+"\n";
		
		// Switch to the Then context
		context = ctxThen;

		cvcout += "%%% -> THEN\n";
		cvcout += n.thenS.accept(this, null);
				
		
		// Switch to the Else context
		context = ctxElse;
		cvcout += "%%% -> ELSE " + Cond+"\n";
		cvcout += n.elseS.accept(this, null);
				
		// Join de los contextos en el contexto original
		TreeSet<String> varsSoloThen = ctxThen.rewrittenVarNames();
		TreeSet<String> varsElse = ctxElse.rewrittenVarNames();
		TreeSet<String> varsComunes = ctxThen.rewrittenVarNames();		
		varsComunes.retainAll(varsElse);
		varsSoloThen.removeAll(varsElse);
		TreeSet<String> varsTodas = ctxThen.rewrittenVarNames();
		varsTodas.addAll(varsElse);
		
		// Restauro el contexto original
		context = ctxReturn;

		// Por cada var que pudo ser tocada, genera un nuevo nombre 
		// y las implicaciones pertinentes.
		cvcout += "%%% -> JOIN " + Cond+"\n";
		try {
			
		for (String var: varsTodas)	{
			String oldVar = context.getVarAssignment(var);
					if (varsComunes.contains(var)) {																
						String newVar = context.setVarAssignment(var);
						cvcout += newVar + ": INT;\n";
						cvcout += "ASSERT (" + Cond + " => "+newVar+ " = " + ctxThen.getVarAssignment(var) + " );\n";
						cvcout += "ASSERT ( NOT " + Cond + " => "+newVar+ " = " + ctxElse.getVarAssignment(var) + " );\n";
					}
					else if (varsSoloThen.contains(var)) {
						String newVar = context.setVarAssignment(var);
						cvcout += newVar + ": INT;\n";
						cvcout += "ASSERT (" + Cond + " => "+newVar+ " = " + ctxThen.getVarAssignment(var) + " );\n";
						cvcout += "ASSERT ( NOT " + Cond + " => "+newVar+ " = " + oldVar + " );\n";
					}
					else if (varsElse.contains(var)) {
						String newVar = context.setVarAssignment(var);
						cvcout += newVar + ": INT;\n";
						cvcout += "ASSERT (" + Cond + " => "+newVar+ " = " + oldVar + " );\n";
						cvcout += "ASSERT ( NOT " + Cond + " => "+newVar+ " = " + ctxElse.getVarAssignment(var) + " );\n";
					}
				}
		} catch (Exception e)
		{
			e.printStackTrace();
		}
				
		cvcout += "%%% <- IF " + Cond+"\n";
		
		return cvcout;
	}
	
	public String visit(BlockStmt n, String cvcin){
		return n.stmt.accept(this, null);
	}

	public String visit(LoopStmt n, String cvcin){
		String cvcout = "";
		cvcout += "%%% INICIO CICLO\n";
		//Conditions as Pred...
		Pred conditionPred = n.condition.accept(new ExpToPredTranslator(), null);
		
		// Pruebo 1:
		// Antes del ciclo debe valer el invariante 
		cvcout += "%%% 1 - Antes del ciclo debe valer el invariante:\n";
		
		//Pred pre = context.translate(precondition);
		//cvcout += "ASSERT (" + pre.accept(new PredToCVC3Translator(), null) + ");\n";
		
		Pred invariantPredInit = context.translate(n.invariant);
		String invariantInit = invariantPredInit.accept(new PredToCVC3Translator(), null);
		
		cvcout += "QUERY (" + invariantInit + " );\n";
		
		// Pruebo 2:
		// En una iteracion cualquiera se preserva el invariante
		cvcout += "%%% 2 - En una iteracion cualquiera se preserva el invariante\n";
		
		//me tengo que crear un nuevo contexto para una iteracion cualquiera
		PestVarContext contextIt = new PestVarContext();
		contextIt.SetLabel("It");
		contextIt.copyContext(context);
		
		TreeMap<String,String> valuation = contextIt.allValuations();	
		for(Entry<String, String> entry : valuation.entrySet()) {
			  cvcout += entry.getValue()+": INT;\n";
		}
		
		Pred invariantPredIt = contextIt.translate(n.invariant);
		String invariantIt = invariantPredIt.accept(new PredToCVC3Translator(), null);
		cvcout += "ASSERT (" + invariantIt + " );\n";
		
		Pred conditionPredIt = contextIt.translate(conditionPred);
		String CondIt = conditionPredIt.accept(new PredToCVC3Translator(), null);
		cvcout += "ASSERT (" + CondIt +" );\n";
		
		// Recuerdo el valor inicial del variante		
		String v0 = contextIt.translate(n.variant).toString();
		
		//visitamos el cuerpo del ciclo en el contexto de iteracion
		PestToCVC3Translator iteratorVisitor = new PestToCVC3Translator(contextIt);
		cvcout += n.body.accept(iteratorVisitor, null);
		
		Pred invariantPredItNew = contextIt.translate(n.invariant);
		String invariantItNew = invariantPredItNew.accept(new PredToCVC3Translator(), null);
		cvcout += "QUERY (" + invariantItNew + " );\n";
		
		// Pruebo 4:
		// variante decrece
		cvcout += "%%% 4 - Variante decrece\n";				
		cvcout += "QUERY (" + contextIt.translate(n.variant).toString() + " < " + v0 +" );\n";
						
		// Pruebo 3:
		// Luego del ciclo cumple la postcondicion
		cvcout += "%%% 3 - Luego del ciclo cumple la postcondicion\n";

		// Toma context pre ciclo y lo actualiza con vars asignadas en la iteración
		TreeMap<String, String> preItVars = context.allValuations();
		for (String var : contextIt.assignedVars())
		{						
			if (preItVars.containsKey(var))
			{
				cvcout += context.setVarAssignment(var)+": INT;\n";				
			}
		}
		
		Pred invariantPredEnd = context.translate(n.invariant);
		String invariantEnd = invariantPredEnd.accept(new PredToCVC3Translator(), null);
		cvcout += "ASSERT (" + invariantEnd + " );\n";
		
		Pred conditionPredEnd = context.translate(conditionPred);
		String CondEnd = conditionPredEnd.accept(new PredToCVC3Translator(), null);
		cvcout += "ASSERT ( NOT " + CondEnd +" );\n";
				
		cvcout += "%%% FIN CICLO\n";
		return cvcout;
	}
	
	private List<Stmt> StmtAsList(Stmt s){
		List<Stmt> list = new ArrayList<Stmt>(); 
		if(s instanceof SeqStmt){
			list.addAll(StmtAsList(((SeqStmt) s).s1));
			list.add(((SeqStmt) s).s2);
		}else{
			list.add(s);
		}
		return list;
	}
	
	public String visit(AssertStmt n, String cvcin){
		//TODO
		
		return "\n";
	}
	
	public String visit(AssumeStmt n, String cvcin){
		//TODO
		return "\n";
	}
	
	public String visit(CallStmt n, String cvcin){		
		return "\n";
	}
	
	public String visit(LocalDefStmt n, String cvcin){
		// Igual a un assign
		
		String cvcout = "";
		String var = n.left.name;

		Trm rightAsTrm = n.right.accept(new ExpToTrmTranslator(), null);
			
		// Reemplazo el lado derecho y luego actualizo el context.
		Trm replacedTrmc = context.translate(rightAsTrm);
		String newVar = context.setVarAssignment(var);		
		cvcout += newVar + ": INT;\n";
		cvcout += "ASSERT ("+newVar+" = "+replacedTrmc.accept(new TrmPrinter(), null)+");\n";
		
		// Lo que sigue ya no hace falta en la version contexts...
		return cvcout;
	}
	
	protected PestVarContext context;
	protected Stmt statements;
	protected Pred precondition;
	protected Pred postcondition;
}
