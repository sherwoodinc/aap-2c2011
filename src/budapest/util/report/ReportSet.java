package budapest.util.report;

import java.io.PrintStream;
import java.util.Set;
import java.util.TreeSet;

import budapest.util.ASTNode;

public class ReportSet {
	
	private Set<ErrorReport> errorReports = new TreeSet<ErrorReport>();
	private Set<WarningReport> warningReports = new TreeSet<WarningReport>();
	
	public void addErrorReport(final String msg, ASTNode n) {
		errorReports.add(new ErrorReport(msg, n.line, n.column));
	}
	
	public void addWarningReport(final String msg, ASTNode n) {
		warningReports.add(new WarningReport(msg, n.line, n.column));
	}
	
	public int getErrorCount() {
		return errorReports.size();
	}
	
	public int getWarningCount() {
		return warningReports.size();
	}
	
	public Set<ErrorReport> getErrors() {
		return errorReports;
	}

	public Set<WarningReport> getWarnings() {
		return warningReports;
	}
	
	public void dump(PrintStream out) {
		if(getErrorCount() > 0) {
			for(ErrorReport er : getErrors())
				out.println(er);
			out.println();
		}
		
		if(getWarningCount() > 0) {
			for(WarningReport wr : getWarnings())
				out.println(wr);
			out.println();
		}
		
		out.println(getErrorCount() + " errors, " +
				getWarningCount() + " warnings.");
		out.println();
	}

}
