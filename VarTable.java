import java.util.LinkedList;



public class VarTable
{

    // Var is class that basically acts as a struct to store data necessary to the variable table
    LinkedList<Var> varTable;
    public String className;


    public VarTable(String className)
    {
        varTable = new LinkedList<>();
        this.className = className;
    }

    public String VarExists(Var v)
    {
        for (Var v1 : varTable)
        {
            if (v.ident.equals(v1.ident))
                return v1.type;
        }
        return null;
    }
    public String varExists(String s)
    {
        for (Var v1 : varTable)
        {
            if (s.equals(v1.ident))
                return v1.type;
        }
        return null;
    }


    public void addVar(Var v) throws Exception
    {
    	//make sure the varname is not same as a class name
        if(ClassesTable.getInstance().classTable.containsKey(v.ident))
         	throw new Exception("Var " + v.ident + " has same name as class ");
        //make sure var type exists
    	if(!ClassesTable.getInstance().classTable.containsKey(v.type))
    		throw new Exception("Var " + v.ident + " has invalid type "+v.type);
        boolean changed = false;
        for (Var v2: varTable)
        {
            if (v2.ident.equals(v.ident))
            {
                changed = true;
                v2.UpdateType(v.type);
            }
        }
        if (!changed)
        {
            varTable.add(v);
        }
    }

    public void removeVar(Var v)
    {
        varTable.remove(v);
    }

    public String getType(Var v)
    {
        for (Var v2 : varTable)
        {
            if (v2.ident.equals(v.ident))
            {
                return v.type;
            }
        }
        return null;
    }

    public String getType(String identifier)
    {
        for (Var v : varTable)
        {
            if (v.ident.equals(identifier))
            {
                return v.type;
            }
        }
        return null;
    }
    
}

