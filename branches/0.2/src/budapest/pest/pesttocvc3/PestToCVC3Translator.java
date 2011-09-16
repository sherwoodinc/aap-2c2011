package budapest.pest.pesttocvc3;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import budapest.pest.ast.exp.Exp;
import budapest.pest.ast.pred.Pred;
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
import budapest.pest.predtocvc3.PredToCVC3Translator;
import budapest.pest.predtocvc3.PredVarReplacer;

public final class PestToCVC3Translator extends PestVisitor<String, PestVarContext> {

	public String execute(Program n) {
		Procedure main = n.getMain();
		
		List<String> declaredVars = new ArrayList<String>(main.paramNames());
		PestVarContext mainContext = new PestVarContext(declaredVars);
		
		String result = "";
		for(String var : mainContext.getVarInstances())
			result += var + ":INT;\n";
				
		Pred pre = main.pre.accept(new PredVarReplacer(), mainContext);
		result += "ASSERT " + pre.accept(new PredToCVC3Translator(), null) + ";\n"; 
		
		result += main.accept(this, mainContext);
		
		Pred post = main.post.accept(new PredVarReplacer(), mainContext);
		result += "QUERY " + post.accept(new PredToCVC3Translator(), null) + ";";
		
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
		
		for(String var : allModifiedVars) {
			result += context.addNewInstanceOf(var) + ":INT;\n";
		}
		
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

	public String visit(LoopStmt n, PestVarContext context) {
		//TODO
		return null;
	}
	
	public String visit(AssertStmt n, PestVarContext context) {
		//TODO
		return null;
	}
	
	public String visit(AssumeStmt n, PestVarContext context) {
		//TODO
		return null;
	}
	
	public String visit(CallStmt n, PestVarContext context) {
		//TODO
		return null;
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
		
		for(String var : ifContext.getModifiedVars()) {
			joinIf += ifContext.getInstanceOf(var) + " = " + mainContext.getInstanceOf(var) + " AND ";
		}
		
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
