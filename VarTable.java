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

    public void addVar(Var v)
    {
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

