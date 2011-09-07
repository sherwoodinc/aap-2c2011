package budapest.util.report;

public class WarningReport extends Report {

	public WarningReport(String msg, int line, int col) {
		super(msg, line, col);
	}

	@Override
	public String toString() {
		return "warning> " + super.toString();
	}
}
