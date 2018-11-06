import java.util.LinkedList;
import java.util.Stack;

public abstract class SymbolTable
{

    public static class VarTable
    {
        LinkedList<Stack<Var>> varTable = new LinkedList<>();

        public void addVar(Var v)
        {
            boolean added = false;
            for (Stack<Var> s: varTable)
            {
                if (((Var)s.peek()).ident.equals(v.ident))
                {
                    added = true;
                    s.push(v);
                }
            }
            if (!added)
            {
                Stack<Var> newStack = new Stack<>();
                newStack.push(v);
                varTable.add(newStack);
            }
        }

        public void removeVar(Var v)
        {
            for (Stack<Var> s : varTable)
            {
                if (s.peek().ident.equals(v.ident))
                {
                    s.pop();
                    break;
                }
            }
        }

        public String getType(Var v)
        {
            for (Stack<Var> s : varTable)
            {
                if (s.peek().ident.equals(v.ident))
                {
                    return s.peek().type;
                }
            }
            return null;
        }
    }
    public static class Var
    {
        public String ident;
        public String type;

        public Var(String i, String t)
        {
            this.ident = i;
            this.type = t;
        }
    }

}
