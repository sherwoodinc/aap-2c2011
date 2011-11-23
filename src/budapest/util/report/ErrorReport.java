package budapest.util.report;

public class ErrorReport extends Report {

	public ErrorReport(String msg, int line, int col) {
		super(msg, line, col);
	}
	
	@Override
	public String toString() {
		return "ERROR> " + super.toString();
	}

}
