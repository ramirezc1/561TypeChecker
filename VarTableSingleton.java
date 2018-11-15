import java.util.LinkedList;

public class VarTableSingleton
{
    static LinkedList<VarTable> varTables = new LinkedList<>();
    static LinkedList<VarTable> constructorVarTables = new LinkedList<>();
    private static VarTableSingleton varTableInstance;

    public static VarTableSingleton getCurrentInstance()
    {
        if (varTableInstance == null)
            varTableInstance = new VarTableSingleton();
        return varTableInstance;
    }

    public void addTable(VarTable varTable)
    {
        varTables.add(varTable);
    }

    public static VarTable getTableByClassName(String className)
    {
        for (VarTable vt : varTables)
        {
            if (vt.className.equals(className))
                return vt;
        }
        return null;
    }

    public static VarTable getConstructorVarTable(String className)
    {
        for (VarTable vt : constructorVarTables)
        {
            if (vt.className.equals(className))
                return vt;
        }
        constructorVarTables.add(new VarTable(className));
        return constructorVarTables.get(constructorVarTables.size() - 1);
    }

   
}
