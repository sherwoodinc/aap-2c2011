package budapest.pest.pesttocvc3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import budapest.pest.ast.exp.Exp;
import budapest.pest.ast.params.ConcreteParam;
import budapest.pest.ast.params.IntConcreteParam;
import budapest.pest.ast.pred.Pred;
import budapest.pest.ast.pred.trm.IntegerLiteralTrm;
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
import budapest.pest.dump.pest.PredPrinter;
import budapest.pest.predtocvc3.PredParamReplacer;
import budapest.pest.predtocvc3.PredToCVC3Translator;
import budapest.pest.predtocvc3.PredVarReplacer;
import budapest.pest.predtocvc3.TrmToCVC3Translator;
import budapest.pest.predtocvc3.TrmVarReplacer;
import budapest.util.FreshVarGenerator;

public final class PestToCVC3Translator extends PestVisitor<String, PestVarContext> {
	Program verifiedProgram;

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

	public String visit(Procedure n, PestVarContext context) {
		return n.stmt.accept(this, context);
	}

	public String visit(AssignStmt n, PestVarContext context) {
		Exp right = n.right.accept(new ExpVarReplacer(), context);
		String newVar = context.addNewInstanceOf(n.left.name);
		return getCVC3EQAssert(right, newVar);
	}
	
	public String visit(SeqStmt n, PestVarContext context) {
		String s1 = n.s1.accept(this, context);
		return s1 + n.s2.accept(this, context);
	}

	public String visit(SkipStmt n, PestVarContext context) {
		return "";
	}

	public String visit(IfStmt n, PestVarContext context) {
		Exp condition = n.condition.accept(new ExpVarReplacer(), context);
		String conditionAsString = condition.accept(new ExpToCVC3Translator(), null);
			
		PestVarContext thenContext = new PestVarContext(context);
		String thenBranch = n.thenS.accept(this, thenContext);
		Set<String> thenModifiedVars = thenContext.getModifiedVars();
				
		PestVarContext elseContext = new PestVarContext(context);
		String elseBranch = n.elseS.accept(this, elseContext);
		Set<String> elseModifiedVars = elseContext.getModifiedVars();
				
		String result = thenBranch + elseBranch;
		
		Set<String> allModifiedVars = new HashSet<String>(thenModifiedVars);
		allModifiedVars.addAll(elseModifiedVars);
		
		for(String var : allModifiedVars)
			result += context.addNewInstanceOf(var) + ":INT;\n";
		
		result += getIfJoinAssert(conditionAsString, context, thenContext);
		result += getIfJoinAssert("NOT " + conditionAsString, context, elseContext);
		
		return result;
	}
	
	public String visit(LocalDefStmt n, PestVarContext context) {
		String localNewVar = context.addNewInstanceOf(n.left.accept(new ExpToCVC3Translator(), null));
		return getCVC3EQAssert(n.right.accept(new ExpVarReplacer(), context), localNewVar);
	}
	
	public String visit(BlockStmt n, PestVarContext context) {
		return n.stmt.accept(this, context);
	}
		
	public String visit(AssertStmt n, PestVarContext context) {
		Pred query = n.query.accept(new PredVarReplacer(), context);
		String queryToCVC3 = query.accept(new PredToCVC3Translator(context), null);
		return "QUERY (" + queryToCVC3 + ");\n";
	}
	
	public String visit(AssumeStmt n, PestVarContext context) {
		Pred hypothesis = n.hypothesis.accept(new PredVarReplacer(), context);
		String hypothesisToCVC3 = hypothesis.accept(new PredToCVC3Translator(context), null);
		return "QUERY (" + hypothesisToCVC3 + ");\n";
	}
	
	public String visit(LoopStmt n, PestVarContext context) {
		//Loop invariant is true before it starts...
		Pred invBeforeStart = n.invariant.accept(new PredVarReplacer(), context);
		String invariantToCVC3 = invBeforeStart.accept(new PredToCVC3Translator(context), null);
		String result = "QUERY (" + invariantToCVC3 + ");\n";
		
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
		result += "QUERY (" + variantEndToCVC3 + " < " + variantFunctionName + ");\n";
		
		//Loop's body preserves its invariant...
		Pred invLoopBeforeEnd = n.invariant.accept(new PredVarReplacer(), whileContext);
		invariantToCVC3 = invLoopBeforeEnd.accept(new PredToCVC3Translator(context), null);
		result += "QUERY (" + invariantToCVC3 + ");\n";
		
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
		
		return result;
	}
	
	public String visit(CallStmt n, PestVarContext context) {
		String result = "%%% CALL " + n.procName + "\n";
		// Create var binder for this call
		ProcVarBinder binder = new ProcVarBinder(n, verifiedProgram);
		
		Pred pre = binder.callee.pre.accept(new PredParamReplacer(), binder.bindings).accept(new PredVarReplacer(), context);

		result += "%%% Verify PRE\n";
		//result += " QUERY ( " + pre.accept(new PredPrinter(), null) + " );\n";
		result += " QUERY ( " + pre.accept(new PredToCVC3Translator(context), null) + " );\n";
		result += "%%% Here the Procedure 'runs'...\n";

		// Overwrite context with changed vars
		for (String var: binder.touchedVars)
		{
			result += context.addNewInstanceOf(var) + ":INT;\n";			
		}
		
		result += "%%% Assume POST\n";
		Pred post = binder.callee.post.accept(new PredParamReplacer(), binder.bindings).accept(new PredVarReplacer(), context);		
		result += "ASSERT ( " + post.accept(new PredToCVC3Translator(context), null)  + " );\n";
		result += "%%% CALL DONE\n";
		return result;
	}
	
	private String getCVC3EQAssert(Exp exp, String var){
		String expAsString = exp.accept(new ExpToCVC3Translator(), null);
		String result = var + ":INT;\n";
		result += "ASSERT " + var + " = " + expAsString + ";\n";
		return result;
	}
	
	private String getIfJoinAssert(String condition, PestVarContext mainContext, PestVarContext ifContext) {
		String result = "";
		String newVarInstances = "";
		String joinIf = "";
		
		for(String var : ifContext.getModifiedVars())
			joinIf += ifContext.getInstanceOf(var) + " = " + mainContext.getInstanceOf(var) + " AND ";
				
		result += newVarInstances;
		if(joinIf.length() > 0)
			joinIf = joinIf.substring(0, joinIf.length() - " AND ".length());
		
		if(joinIf == "")
			result = "";
		else
			result += "ASSERT ((" + condition + ") => (" + joinIf + "));\n";
		
		return result;
	}
}
