package budapest.util.report;

public abstract class Report implements Comparable<Report> {

	public final String msg;
	
	public final int line;
	
	public final int col;

	protected Report(final String msg, final int line, final int col) {
		super();
		this.msg = msg;
		this.line = line;
		this.col = col;
	}
	
	@Override
	public String toString() {
		return msg + ". Line " + line + ", column " + col + ".";
	}

	public int compareTo(Report o) {
		if(line < o.line)
			return -1;
		else
			if(line == o.line && col < o.col)
				return -1;
			else
				if(line == o.line && col == o.col)
					return msg.compareTo(o.msg);
				else
					return 1;
	}
}
