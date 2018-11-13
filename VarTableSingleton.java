import java.util.LinkedList;

public class VarTableSingleton
{
    LinkedList<VarTable> varTables = new LinkedList<>();
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

    public VarTable getTableByClassName(String className)
    {
        for (VarTable vt : varTables)
        {
            if (vt.className.equals(className))
                return vt;
        }
        return null;
    }

    public VarTable getCurrentTable()
    {
        // it's important we always append new varTables to the end
        return varTables.get(varTables.size() - 1);
    }
}
