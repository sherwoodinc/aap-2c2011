package budapest.pest.typeinference;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class PestTypedContext {

	private TreeMap<String, PestType> _varTypes;
	
	public PestTypedContext() 
	{
		_varTypes = new TreeMap<String, PestType>();
	}
	
	public boolean isTyped(String var)
	{
		return _varTypes.containsKey(var);
	}
	
	public PestType getTypeOf(String var) throws Exception
	{
		if(!_varTypes.containsKey(var))
		{
			throw new Exception("There's no typing information for variable " + var);
		}
		
		return _varTypes.get(var);
	}
	
	public void add(String var, PestType type) throws Exception
	{
		if(_varTypes.containsKey(var))
		{
			PestType currentType = getTypeOf(var);
			throw new Exception("Variable " + var + "'s type has already been set to " + currentType.toString());
		}
		_varTypes.put(var, type);
	}
	
	public void setType(String var, PestType type)
	{
		_varTypes.put(var, type);
	}
	
	public List<String> getAllTypedVars()
	{
		List<String> result = new ArrayList<String>();
		for(String var : _varTypes.keySet())
			result.add(var);
		return result;
	}
	
	public void add(PestTypedContext context) throws Exception
	{
		List<String> typedVars = context.getAllTypedVars();
		for(String typedVar : typedVars)
		{
			if(!isTyped(typedVar))
			{
				add(typedVar, context.getTypeOf(typedVar));
			}
			else
			{
				PestType thisType = getTypeOf(typedVar);
				PestType otherType = context.getTypeOf(typedVar);
				PestType minimumType = thisType.minimumType(otherType);
				if(minimumType == null)
				{
					throw new Exception("Type mismatch. Variable " + typedVar + " cannot be " + 
										thisType.getTypeName() + " and " + 
										otherType.getTypeName());
				}
				else
				{
					setType(typedVar, minimumType);
				}
			}
		}
	}
}
