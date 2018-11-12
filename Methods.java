import java.util.List;

public abstract class Methods
{
    public Methods() { }
    abstract void visit(ClassesTable ct) throws Exception;

    public static class Method extends Methods
    {
        public String _methodIdent;
        public Args _formalArgs;
        String _methodType;
        List<Statement> _statements;


        public Method(String methodIdent, Args formalArgs, List<Statement> stmts)
        {
            this._methodIdent = methodIdent;
            this._formalArgs = formalArgs;
            this._statements = stmts;
        }

        public Method(String methodIdent, Args formalArgs, String type, List<Statement> stmts)
        {
            this._methodIdent = methodIdent;
            this._formalArgs = formalArgs;
            this._methodType = type;
            this._statements = stmts;
        }

        public void visit(ClassesTable ct) throws Exception
        {
            //check if method has same name as a class
        	if(ct.classTable.containsKey(_methodIdent))
        		throw new Exception("Method "+_methodIdent + " has same name as class ");
        	for (Statement s : this._statements)
            {
                s.visit();
            }
        }

        public String toString()
        {
            StringBuilder statements = new StringBuilder();
            StringBuilder header = new StringBuilder();
            header.append(this._methodIdent).append(" ");
            if (this._methodType != null)
                header.append(this._methodType);
            if (this._formalArgs != null)
                header.append(_formalArgs);
            for (Statement s : this._statements)
            {
                statements.append("\n\t\t").append(s);
            }

            return header + "\n" + statements;
        }

    }
    public static Methods.Method method(String methodIdent, Args formalArgs, List<Statement> stmts)
    {
        return new Methods.Method(methodIdent, formalArgs, stmts);
    }
    public static Methods.Method method(String methodIdent, Args formalArgs, String methodType, List<Statement> stmts)
    {
        return new Methods.Method(methodIdent, formalArgs, methodType, stmts);
    }

}
