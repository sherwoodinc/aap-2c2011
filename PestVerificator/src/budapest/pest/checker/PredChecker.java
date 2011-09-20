package budapest.pest.checker;

import budapest.pest.ast.pred.QuantifiedPred;
import budapest.pest.ast.pred.RelationPred;
import budapest.pest.ast.visitor.PredVisitor;
import budapest.pest.checker.symtab.ScopeSymbolTable;
import budapest.pest.checker.symtab.SymbolTable;
import budapest.pest.checker.symtab.SymbolTable.SymbolInfo.Type;
import budapest.util.report.ReportSet;

public class PredChecker extends PredVisitor<Void, SymbolTable> {

	public final ReportSet reports;
	
	public PredChecker(ReportSet reports) {
		this.reports = reports;
	}

	@Override
	public Void visit(QuantifiedPred n, SymbolTable st) {
		
		SymbolTable childSt = new ScopeSymbolTable(st);
		
		if(st.knows(n.var))
			reports.addWarningReport("Quantified variable " + n.var
					+ " is not fresh", n);
		
		childSt.add(n.var, Type.PRED);
		
		n.lowerBound.accept(new TrmChecker(reports), childSt);
		n.upperBound.accept(new TrmChecker(reports), childSt);
		
		n.subPred.accept(this, childSt);
		
		return null;
	}
	
	@Override
	public Void visit(RelationPred n, SymbolTable st) {
		
		n.left.accept(new TrmChecker(reports), st);
		n.right.accept(new TrmChecker(reports), st);

		return null;
	}
	
}
