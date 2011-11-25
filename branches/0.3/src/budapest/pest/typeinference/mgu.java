package budapest.pest.typeinference;

public class mgu
{
	public static PestType execute(PestType type1, PestType type2)
	{
		if(type1.equals(type2))
		{
			return type1;
		}
		
		if(type1 instanceof PestTypingConstant)
		{
			return type2;
		}
		
		if(type2 instanceof PestTypingConstant)
		{
			return type1;
		}
		
		return null;
	}
}
