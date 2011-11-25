package budapest.pest.typeinference;

public class PestTypingConstant extends PestType
{
	private String _constant;
	
	public PestTypingConstant()
	{
		_constant = "t" + String.valueOf(getNewConstant()); 
	}
	
	public String getTypeName() 
	{
		return _constant;
	}
	
	private static int _maxConstant = 0;
	
	private static int getNewConstant()
	{
		return ++_maxConstant;
	}
}
