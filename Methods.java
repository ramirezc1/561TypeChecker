import java.util.List;

public abstract class Methods
{
    public Methods() { }
    abstract void visit(String classIdent) throws Exception;
    abstract void visit2(String classIdent) throws Exception;

    public static class Method extends Methods
    {
        public String _methodIdent;
        public Args _formalArgs;
        String _methodType ="Nothing";
        List<Statement> _statements;


        public Method(String methodIdent, Args formalArgs, List<Statement> stmts)
        {
            this._methodIdent = methodIdent;
            this._formalArgs = formalArgs;
            this._statements = stmts;
            this._methodType = "Nothing";
        }

        public Method(String methodIdent, Args formalArgs, String type, List<Statement> stmts)
        {
            this._methodIdent = methodIdent;
            this._formalArgs = formalArgs;
            this._methodType = type;
            this._statements = stmts;
        }

        public void visit(String classIdent) throws Exception
        {
            Var method = new Var(this._methodIdent, this._methodType);
            VarTableSingleton.getTableByClassName(classIdent).AddMethodToMethodTable(method);
        }

        public void visit2(String classIdent) throws Exception
        {
        	//Make sure args have existing type
        	_formalArgs.visit2(classIdent, this._methodIdent);

        	String statementIdent = "";
        	int statementIndex = 0;
        	int statementCount = this._statements.size();
            Statement s = null;
        	while (!statementIdent.toLowerCase().equals("return") && statementIndex < statementCount)
            {
                s = this._statements.get(statementIndex++);
                statementIdent = s.StatementType();
                s.visit2(classIdent, this._methodIdent);
            }

            if (statementIndex < statementCount)
            {
                throw new Exception("Statement after return: " + s);
            }

            if (!s.StatementType().toLowerCase().equals("return"))
            {
                throw new Exception("Method " + this._methodIdent + " lacks a return statement");
            }

            if(s != null && !TypeChecker.checkSubtype(s.getExpr().getType(), _methodType))
            {
                throw new Exception("Problem with return: " + s.getExpr().getType() + " is not a subtype of "+ _methodType);
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
        public String getMethodIdent() {
        	return _methodIdent;
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
	protected abstract String getMethodIdent();

}
