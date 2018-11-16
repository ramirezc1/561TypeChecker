import java.util.HashMap;
import java.util.LinkedList;



public class VarTable
{

    // Var is class that basically acts as a struct to store data necessary to the variable table
    LinkedList<Var> varTable;
    LinkedList<Var> constructorTable;
    LinkedList<Var> methodTable;
    HashMap<String, LinkedList<Var>> methodVars;
    public String className;


    public VarTable(String className)
    {
        varTable = new LinkedList<>();
        constructorTable = new LinkedList<>();
        methodTable = new LinkedList<>();
        methodVars = new HashMap<>();
        this.className = className;
    }

    public boolean VarIdentExists(String ident)
    {
        for (Var v : varTable)
        {
            if (v.ident.equals(ident))
                return true;
        }
        return false;
    }

    public boolean ConstructorIdentExists(String ident)
    {
        for (Var v : constructorTable)
        {
            if (v.ident.equals(ident))
                return true;
        }
        return false;
    }

    public boolean MethodIdentExists(String ident)
    {
        for (Var v : methodTable)
        {
            if (v.ident.equals(ident))
                return true;
        }
        return false;
    }

    public Var GetVarFromVarTable(String ident)
    {
        for (Var v : varTable)
        {
            if (v.ident.equals(ident))
                return v;
        }
        return null;
    }

    public Var GetVarFromConstructorTable(String ident)
    {
        for (Var v : constructorTable)
        {
            if (v.ident.equals(ident))
                return v;
        }
        return null;
    }

    public Var GetVarFromMethodTable(String ident)
    {
        for (Var v : methodTable)
        {
            if (v.ident.equals(ident))
                return v;
        }
        return null;
    }

    public String ExistsInVarTable(Var v)
    {
        for (Var v1 : varTable)
        {
            if (v.ident.equals(v1.ident))
                return v1.type;
        }
        return null;
    }
    public String ExistsInVarTable(String s)
    {
        for (Var v1 : varTable)
        {
            if (s.equals(v1.ident))
                return v1.type;
        }
        return null;
    }

    public String ExistsInConstructorTable(Var v)
    {
        String currentClassIdent;
        for (Var v1 : constructorTable)
        {
            if (v.ident.equals(v1.ident))
                return v1.type;
        }
        return null;
    }
    public String ExistsInConstructorTable(String s)
    {
        for (Var v1 : constructorTable)
        {
            if (s.equals(v1.ident))
                return v1.type;
        }
        return null;
    }

    public String ExistsInMethodTable(Var v)
    {
        for (Var v1 : methodTable)
        {
            if (v.ident.equals(v1.ident))
                return v1.type;
        }
        return null;
    }
    public String ExistsInMethodTable(String s)
    {
        for (Var v1 : methodTable)
        {
            if (s.equals(v1.ident))
                return v1.type;
        }
        return null;
    }


    public void AddVarToVarTable(Var v) throws Exception
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
                break;
            }
        }
        if (!changed)
        {
            varTable.add(v);
        }
    }


    public void AddVarToConstructorTable(Var v) throws Exception
    {
        //make sure the varname is not same as a class name
        if(ClassesTable.getInstance().classTable.containsKey(v.ident))
            throw new Exception("Var " + v.ident + " has same name as class ");
        //make sure var type exists
        if(!ClassesTable.getInstance().classTable.containsKey(v.type))
            throw new Exception("Var " + v.ident + " has invalid type "+v.type);
        boolean changed = false;
        for (Var v2: constructorTable)
        {
            if (v2.ident.equals(v.ident))
            {
                changed = true;
                v2.UpdateType(v.type);
                break;
            }
        }
        if (!changed)
        {
            constructorTable.add(v);
        }
    }


    public void AddMethodToMethodTable(Var v) throws Exception
    {
        //make sure the varname is not same as a class name
        if(ClassesTable.getInstance().classTable.containsKey(v.ident))
            throw new Exception("Var " + v.ident + " has same name as class ");
        //make sure var type exists
        if(!ClassesTable.getInstance().classTable.containsKey(v.type))
            throw new Exception("Var " + v.ident + " has invalid type " + v.type);
        boolean changed = false;
        for (Var v2: methodTable)
        {
            if (v2.ident.equals(v.ident))
            {
                changed = true;
                v2.UpdateType(v.type);
                break;
            }
        }
        if (!changed)
        {
            methodTable.add(v);
        }
        methodVars.put(v.ident, new LinkedList<>());
    }

    public void AddVarToMethodVarTable(String methodIdent, Var v) throws Exception
    {
        //make sure the varname is not same as a class name
        if(ClassesTable.getInstance().classTable.containsKey(v.ident))
            throw new Exception("Var " + v.ident + " has same name as class ");
        //make sure var type exists
        if(!ClassesTable.getInstance().classTable.containsKey(v.type))
            throw new Exception("Var " + v.ident + " has invalid type "+v.type);
        boolean changed = false;
        for (Var v2: methodVars.get(methodIdent))
        {
            if (v2.ident.equals(v.ident))
            {
                changed = true;
                v2.UpdateType(v.type);
                break;
            }
        }
        if (!changed)
        {
            methodVars.get(methodIdent).add(v);
        }
    }

    public void RemoveVarFromVarTable(Var v)
    {
        varTable.remove(v);
    }


    public void RemoveVarFromConstructorTable(Var v)
    {
        constructorTable.remove(v);
    }


    public void RemoveVarFromMethodTable(Var v)
    {
        methodTable.remove(v);
    }

    public String GetTypeFromVarTable(Var v)
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

    public String GetTypeFromVarTable(String identifier)
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

    public String GetTypeFromConstructorTable(Var v)
    {
        for (Var v2 : constructorTable)
        {
            if (v2.ident.equals(v.ident))
            {
                return v.type;
            }
        }
        return null;
    }

    public String GetTypeFromConstructorTable(String identifier)
    {
        for (Var v : constructorTable)
        {
            if (v.ident.equals(identifier))
            {
                return v.type;
            }
        }
        return null;
    }

    public String GetTypeFromMethodTable(Var v)
    {
        for (Var v2 : methodTable)
        {
            if (v2.ident.equals(v.ident))
            {
                return v.type;
            }
        }
        return null;
    }

    public String GetTypeFromMethodTable(String identifier)
    {
        for (Var v : methodTable)
        {
            if (v.ident.equals(identifier))
            {
                return v.type;
            }
        }
        return null;
    }
    
}

