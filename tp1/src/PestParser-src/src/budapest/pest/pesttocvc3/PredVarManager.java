package budapest.pest.pesttocvc3;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import budapest.pest.ast.pred.BinaryPred;
import budapest.pest.ast.pred.BooleanLiteralPred;
import budapest.pest.ast.pred.NotPred;
import budapest.pest.ast.pred.Pred;
import budapest.pest.ast.pred.QuantifiedPred;
import budapest.pest.ast.pred.RelationPred;
import budapest.pest.ast.visitor.PredVisitor;

public class PredVarManager extends PredVisitor<List<String>, Void> {
	
	public String getFreshVar(Pred p) {
		String allVars = "abcdefghijklmnopqrstuvwxyz"; 
		
		List<String> allUsedVars = getUsedVars(p);
		Collections.sort(allUsedVars);
		
		String freshVar = null;
		
		if(allUsedVars.size() > 0) {
			String lastUsedVar = allUsedVars.get(allUsedVars.size() - 1);
			char lastUsedChar = lastUsedVar.charAt(0);
			int lastUsedIndex = allVars.indexOf(lastUsedChar);
			int freshVarIndex = lastUsedIndex;
			//Trying to get first letter that is not used...
			do
			{
				freshVarIndex = ++freshVarIndex % allVars.length();
				char freshChar = allVars.charAt(freshVarIndex);
				freshVar = String.valueOf(freshChar);
			}
			while(allUsedVars.contains(freshVar) && freshVarIndex != lastUsedIndex);
			
			//If all letters are used, we're going to add a number to the end of a letter...
			if(freshVarIndex == lastUsedIndex){
				int newVarIndex = 1;
				do
				{
					do
					{
						freshVarIndex = ++freshVarIndex % allVars.length();
						char freshChar = allVars.charAt(freshVarIndex);
						freshVar = String.valueOf(freshChar) + String.valueOf(newVarIndex);
					}
					while(allUsedVars.contains(freshVar) && freshVarIndex != lastUsedIndex);
					newVarIndex++;
				}
				while(allUsedVars.contains(freshVar));
			}
		}
		else
		{
			//If not variables were used, return "a"...
			freshVar = "a";
		}
		
		return freshVar;
	}
	
	public List<String> getUsedVars(Pred p) {
		List<String> resultVars = new ArrayList<String>();
		List<String> vars = p.accept(this, null);
		for(String var : vars){
			if(!resultVars.contains(var))
				resultVars.add(var);
		}
		return resultVars;
	}
	
	public List<String> visit(BinaryPred n, Void arg) {
		List<String> result = new ArrayList<String>();
		result.addAll(n.left.accept(this, arg));
		result.addAll(n.right.accept(this, arg));
		return result;
	}
	
	public List<String> visit(BooleanLiteralPred n, Void arg) {
		return new ArrayList<String>();
	}
	
	public List<String> visit(NotPred n, Void arg) {
		List<String> result = new ArrayList<String>();
		result.addAll(n.subPred.accept(this, arg));
		return result;
	}
	
	public List<String> visit(QuantifiedPred n, Void arg) {
		List<String> result = new ArrayList<String>();
		result.add(n.var);
		result.addAll(n.subPred.accept(this, arg));
		if (n.lowerBound != null)
			result.addAll(n.lowerBound.accept(new TrmVarManager(), arg));
		if (n.upperBound != null)
			result.addAll(n.upperBound.accept(new TrmVarManager(), arg));
		return result;
	}
	
	public List<String> visit(RelationPred n, Void arg) {
		List<String> result = new ArrayList<String>();
		result.addAll(n.left.accept(new TrmVarManager(), arg));
		result.addAll(n.right.accept(new TrmVarManager(), arg));
		return result;
	}
}
