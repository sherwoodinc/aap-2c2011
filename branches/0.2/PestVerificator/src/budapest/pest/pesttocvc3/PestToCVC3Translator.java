/*
 * 
 */
package budapest.pest.pesttocvc3;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import budapest.pest.ast.exp.Exp;
import budapest.pest.ast.pred.Pred;
import budapest.pest.ast.pred.trm.Trm;
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
import budapest.pest.predtocvc3.PredParamReplacer;
import budapest.pest.predtocvc3.PredToCVC3Translator;
import budapest.pest.predtocvc3.PredVarReplacer;
import budapest.pest.predtocvc3.TrmToCVC3Translator;
import budapest.pest.predtocvc3.TrmVarReplacer;
import budapest.util.FreshVarGenerator;

/**
 * PestToCVC3Translator convierte un programa PEST en una secuencia de comandos CVC
 * que pretenden verificar el programa de entrada. 
 */
public final class PestToCVC3Translator extends PestVisitor<String, PestVarContext> {
	
	/** Referencia al Program para consultas durante el visit. */
	Program verifiedProgram;

	/**
	 * Punto de entrada principal, procesa un programa completo de a un procedimiento
	 *
	 * @param n el programa Pest de entrada
	 * @return un String con la salida CVC
	 */
	public String execute(Program n) {
		verifiedProgram = n;
		String result = "";

		for (Procedure proc: n.procs)
		{			
			result += "%%% ENTER PROC " + proc.name + ";\n";
			List<String> declaredVars = new ArrayList<String>(proc.paramNames());
			PestVarContext initContext = new PestVarContext(declaredVars);
			PestVarContext procContext = new PestVarContext(initContext);

			for(String var : initContext.getVarInstances())
				result += var + ":INT;\n";

			Pred pre = proc.pre.accept(new PredVarReplacer(), procContext);
			result += "ASSERT " + pre.accept(new PredToCVC3Translator(initContext), null) + ";\n"; 

			result += proc.accept(this, procContext);

			Pred post = proc.post.accept(new PredVarReplacer(), procContext);
			result += "QUERY " + post.accept(new PredToCVC3Translator(initContext), null) + ";\n";			
			result += "%%% EXIT PROC " + proc.name + ";\n\n";
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see budapest.pest.ast.visitor.PestVisitor#visit(budapest.pest.ast.proc.Procedure, java.lang.Object)
	 */
	public String visit(Procedure n, PestVarContext context) {
		return n.stmt.accept(this, context);
	}

	/* (non-Javadoc)
	 * @see budapest.pest.ast.visitor.PestVisitor#visit(budapest.pest.ast.stmt.AssignStmt, java.lang.Object)
	 */
	public String visit(AssignStmt n, PestVarContext context) {
		Exp right = n.right.accept(new ExpVarReplacer(), context);
		String newVar = context.addNewInstanceOf(n.left.name);		
		return "%%% ASSIGN : "+n.toString()+ getCVC3EQAssert(right, newVar);
	}

	/* (non-Javadoc)
	 * @see budapest.pest.ast.visitor.PestVisitor#visit(budapest.pest.ast.stmt.SeqStmt, java.lang.Object)
	 */
	public String visit(SeqStmt n, PestVarContext context) {
		String s1 = n.s1.accept(this, context);
		return s1 + n.s2.accept(this, context);
	}

	/* (non-Javadoc)
	 * @see budapest.pest.ast.visitor.PestVisitor#visit(budapest.pest.ast.stmt.SkipStmt, java.lang.Object)
	 */
	public String visit(SkipStmt n, PestVarContext context) {
		return "";
	}

	/* (non-Javadoc)
	 * @see budapest.pest.ast.visitor.PestVisitor#visit(budapest.pest.ast.stmt.IfStmt, java.lang.Object)
	 */
	public String visit(IfStmt n, PestVarContext context) {
		Exp condition = n.condition.accept(new ExpVarReplacer(), context);
		String conditionAsString = condition.accept(new ExpToCVC3Translator(), null);

		PestVarContext thenContext = new PestVarContext(context);
		String thenBranch = n.thenS.accept(this, thenContext);

		PestVarContext elseContext = new PestVarContext(context);
		String elseBranch = n.elseS.accept(this, elseContext);

		String result = "%%% IF " + n.condition.toString() +"\n"
		+ "%%% THEN \n"+ thenBranch 
		+ "%%% ELSE \n"+ elseBranch;

		result += getIfJoinAssert(conditionAsString, context, thenContext, elseContext);

		return result;
	}

	/* (non-Javadoc)
	 * @see budapest.pest.ast.visitor.PestVisitor#visit(budapest.pest.ast.stmt.LocalDefStmt, java.lang.Object)
	 */
	public String visit(LocalDefStmt n, PestVarContext context) {
		String localNewVar = context.addNewInstanceOf(n.left.accept(new ExpToCVC3Translator(), null));
		return getCVC3EQAssert(n.right.accept(new ExpVarReplacer(), context), localNewVar);
	}

	/* (non-Javadoc)
	 * @see budapest.pest.ast.visitor.PestVisitor#visit(budapest.pest.ast.stmt.BlockStmt, java.lang.Object)
	 */
	public String visit(BlockStmt n, PestVarContext context) {
		return n.stmt.accept(this, context);
	}

	/* (non-Javadoc)
	 * @see budapest.pest.ast.visitor.PestVisitor#visit(budapest.pest.ast.stmt.AssertStmt, java.lang.Object)
	 */
	public String visit(AssertStmt n, PestVarContext context) {
		Pred query = n.query.accept(new PredVarReplacer(), context);
		String queryToCVC3 = query.accept(new PredToCVC3Translator(context), null);
		return "QUERY (" + queryToCVC3 + ");\n";
	}

	/* (non-Javadoc)
	 * @see budapest.pest.ast.visitor.PestVisitor#visit(budapest.pest.ast.stmt.AssumeStmt, java.lang.Object)
	 */
	public String visit(AssumeStmt n, PestVarContext context) {
		Pred hypothesis = n.hypothesis.accept(new PredVarReplacer(), context);
		String hypothesisToCVC3 = hypothesis.accept(new PredToCVC3Translator(context), null);
		return "QUERY (" + hypothesisToCVC3 + ");\n";
	}

	/* (non-Javadoc)
	 * @see budapest.pest.ast.visitor.PestVisitor#visit(budapest.pest.ast.stmt.LoopStmt, java.lang.Object)
	 */
	public String visit(LoopStmt n, PestVarContext context) {
		//Loop invariant is true before it starts...
		Pred invBeforeStart = n.invariant.accept(new PredVarReplacer(), context);
		String invariantToCVC3 = invBeforeStart.accept(new PredToCVC3Translator(context), null);
		String result = "%%% CHECK PRE-WHILE INVARIANT\n"+ "QUERY (" + invariantToCVC3 + ");\n";
		result += "%%% WHILE "+ n.condition.toString() +"\n";

		//All variables that are modified in the loop must be new...
		PestVarContext whileContext = new PestVarContext(context);
		Set<String> modifiedVarsInLoop = n.accept(new StmtModifiedVarGetter(verifiedProgram), null);
		for(String var : modifiedVarsInLoop)
			result += whileContext.addNewInstanceOf(var) + ":INT;\n";

		//Loop invariant is true just when started...
		Pred invLoopStarted = n.invariant.accept(new PredVarReplacer(), whileContext);
		invariantToCVC3 = invLoopStarted.accept(new PredToCVC3Translator(context), null);
		result += "ASSERT (" + invariantToCVC3 + ");\n";

		//Condition is true...
		Exp loopCondition = n.condition.accept(new ExpVarReplacer(), whileContext);
		String loopConditionToCVC3 = loopCondition.accept(new ExpToCVC3Translator(), null);
		result += "ASSERT (" + loopConditionToCVC3 + ");\n";

		//Variant function start...
		String variantFunctionName = FreshVarGenerator.freshVar();
		result += variantFunctionName + ":INT;\n";
		Trm variantStart = n.variant.accept(new TrmVarReplacer(), whileContext);
		String variantStartToCVC3 = variantStart.accept(new TrmToCVC3Translator(context), null);
		result += "ASSERT (" + variantFunctionName + " = " + variantStartToCVC3 + ");\n";

		//Process loop's body...
		result += n.body.accept(this, whileContext);

		//Variant function end...
		Trm variantEnd = n.variant.accept(new TrmVarReplacer(), whileContext);
		String variantEndToCVC3 = variantEnd.accept(new TrmToCVC3Translator(context), null);

		//Check variant function decreases...
		result += "%%% CHECK POST-LOOP K VARIANT\n";
		result += "QUERY (" + variantEndToCVC3 + " < " + variantFunctionName + ");\n";

		//Loop's body preserves its invariant...
		result += "%%% CHECK POST-LOOP K INVARIANT\n";
		Pred invLoopBeforeEnd = n.invariant.accept(new PredVarReplacer(), whileContext);
		invariantToCVC3 = invLoopBeforeEnd.accept(new PredToCVC3Translator(context), null);
		result += "QUERY (" + invariantToCVC3 + ");\n";

		result += "%%% CHECK POST-WHILE INVARIANT + CONDITION NEGATION\n";

		//We'll use new variables to replace all variables modified in the loop...
		for(String var : modifiedVarsInLoop)
			result += context.addNewInstanceOf(var) + ":INT;\n";

		//After the loop ended, invariant is true...
		Pred invLoopAfterEnd = n.invariant.accept(new PredVarReplacer(), context);
		invariantToCVC3 = invLoopAfterEnd.accept(new PredToCVC3Translator(context), null);
		result += "ASSERT (" + invariantToCVC3 + ");\n";

		//Loop condition is no longer valid...
		Exp loopConditionAfterEnd = n.condition.accept(new ExpVarReplacer(), context);
		loopConditionToCVC3 = loopConditionAfterEnd.accept(new ExpToCVC3Translator(), null);
		result += "ASSERT (NOT " + loopConditionToCVC3 + ");\n";
		result += "%%% END WHILE \n";

		return result;
	}

	/* (non-Javadoc)
	 * @see budapest.pest.ast.visitor.PestVisitor#visit(budapest.pest.ast.stmt.CallStmt, java.lang.Object)
	 */
	public String visit(CallStmt n, PestVarContext context) {
		String result = "%%% CALL " + n.procName + "\n";
		// Create var binder for this call
		ProcVarBinder binder = new ProcVarBinder(n, verifiedProgram);

		Pred pre = binder.callee.pre.accept(new PredParamReplacer(), binder.bindings).accept(new PredVarReplacer(), context);

		result += "%%% Verify PRE\n";
		result += " QUERY ( " + pre.accept(new PredToCVC3Translator(context), null) + " );\n";
		result += "%%% Here the Procedure 'runs'...\n";

		// Overwrite context with changed vars
		for (String var: binder.touchedVars) {
			result += context.addNewInstanceOf(var) + ":INT;\n";			
		}

		result += "%%% Assume POST\n";
		Pred post = binder.callee.post.accept(new PredParamReplacer(), binder.bindings).accept(new PredVarReplacer(), context);		
		result += "ASSERT ( " + post.accept(new PredToCVC3Translator(context), null)  + " );\n";
		result += "%%% CALL DONE\n";
		return result;
	}

	/**
	 * Convierte algo de la forma var = <exp> en un ASSERT CVC
	 * 
	 * @param exp
	 *            Expresión de la derecha
	 * @param var
	 *            Variable 
	 * @return el ASSERT correspondiente
	 */
	private String getCVC3EQAssert(Exp exp, String var){
		String expAsString = exp.accept(new ExpToCVC3Translator(), null);
		String result = var + ":INT;\n";
		result += "ASSERT " + var + " = " + expAsString + ";\n";
		return result;
	}

	/**
	 * Devuelve la secuencia de comandos que "une" las ejecuciones del then y el else de un if
	 * 
	 * @param condition
	 *            la guarda del if
	 * @param mainContext
	 *            el contexto de variables que contiene al if 
	 * @param thenContext
	 *            el contexto resultante de ejecutar el then
	 * @param elseContext
	 *            el contexto resultante de ejecutar el else
	 * @return la secuencia de comandos CVC resultante
	 */
	private String getIfJoinAssert(String condition, PestVarContext mainContext, PestVarContext thenContext, PestVarContext elseContext) {
		String result = "";

		Set<String> varsSoloThen = new HashSet<String>(thenContext.getModifiedVars());
		Set<String> varsElse = elseContext.getModifiedVars();
		Set<String> varsComunes = new HashSet<String>(thenContext.getModifiedVars());		
		varsComunes.retainAll(varsElse);
		varsSoloThen.removeAll(varsElse);
		Set<String> varsAll = new HashSet<String>(thenContext.getModifiedVars());
		varsAll.addAll(varsElse);

		result += "%%% JOIN IF " + condition + "\n";
		for(String var : varsAll)
		{
			String oldVar = mainContext.getInstanceOf(var);

			if (varsComunes.contains(var)) {																
				result += mainContext.addNewInstanceOf(var) + ":INT;\n";
				String newVar = mainContext.getInstanceOf(var);
				result += "ASSERT (" + condition + " => "+ newVar + " = " + thenContext.getInstanceOf(var) + " );\n";
				result += "ASSERT ( (NOT " + condition + ") => "+ newVar + " = " + elseContext.getInstanceOf(var) + " );\n";
			}
			else if (varsSoloThen.contains(var)) {
				result += mainContext.addNewInstanceOf(var) + ":INT;\n";
				String newVar = mainContext.getInstanceOf(var);
				result += "ASSERT (" + condition + " => "+newVar+ " = " + thenContext.getInstanceOf(var) + " );\n";
				result += "ASSERT ( (NOT " + condition + ") => "+newVar+ " = " + oldVar + " );\n";
			}
			else if (varsElse.contains(var)) {
				result += mainContext.addNewInstanceOf(var) + ":INT;\n";
				String newVar = mainContext.getInstanceOf(var);
				result += "ASSERT (" + condition + " => "+newVar+ " = " + oldVar + " );\n";
				result += "ASSERT ( (NOT " + condition + ") => "+newVar+ " = " + elseContext.getInstanceOf(var) + " );\n";
			}
		}
		return result;
	}
}
