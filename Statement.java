import java_cup.runtime.ComplexSymbolFactory.Location;

import java.util.LinkedList;
import java.util.List;

public abstract class Statement
{
	public Statement() { }
    abstract void visit2(String classIdent) throws Exception;

    public static class Assignment_Statement extends Statement
    {
        public Expression _lexpr;
        public Expression _rexpr;
        public String _declaredType = "";

        public Assignment_Statement(Expression e1, Expression e2)
        {
            this._lexpr = e1;
            this._rexpr = e2;
        }

        public Assignment_Statement(Expression e1, String declaredType, Expression e2)
        {
            this._lexpr = e1;
            this._rexpr = e2;
            this._declaredType = declaredType;
        }
        public Expression getLexpr() {
        	return _lexpr;
        }
        public Expression getRexpr() {
        	return _rexpr;
        }
        public String getDeclaredType() {
        	return _declaredType;
        }

        public void visit2(String classIdent) throws Exception
        {
        	//first time visit
        	//add to var table 
        	//???? should visit go here
        	_rexpr.visit2(classIdent);
            String type = _rexpr.getType();

            // if the type of _rexpr isn't in the class table,
            if (type == null)
                throw new Exception("\"" + this._rexpr.getIdent() + "\" is not declared");
            // if the declared type of _lexpr doesn't match the type of _rexpr
            if ((!this._declaredType.equals("") && !this._declaredType.equals(type)))
            {
                // throw exception: type not found or doesn't match declared type
                throw new Exception("Exception: " + this._declaredType + " != " + type);
            }

            // Temporarily assuming/casting an _lexpr to Expression.Ident because
            // we don't distinguish between lexpr and rexprs.
            //String tempIdent = ((Expression.Identifier)this._lexpr).ident;
            
            //?????should visit on _lexpr get called?? for this var it will add type Nothing. which later won't be able to be updated
            //maybe latice needs to be updated
            //_lexpr.visit();
            String tempIdent = this._lexpr.getIdent();
            
            Var var = new Var(tempIdent, type);
            VarTable varTable;
            if (this._lexpr.getIdent().contains("this."))
                varTable = VarTableSingleton.getConstructorVarTable(classIdent);
            else
                varTable = VarTableSingleton.getTableByClassName(classIdent);
            varTable.addVar(var);
        }

        public String toString()
        {
            String type ="";
            if (!_declaredType.equals("")) type = ": " + _declaredType + " ";
            return _lexpr + type + " = " + _rexpr;
        }
    }
    public static Assignment_Statement asmt_stmt(Expression e1, Expression e2)
    {
        return new Assignment_Statement(e1, e2);
    }
    public static Assignment_Statement asmt_stmt(Expression e1, String ident, Expression e2)
    {
        return new Assignment_Statement(e1, ident, e2);
    }
    public static Assignment_Statement asmt_stmt(Location l, Expression e1, String ident, Expression e2, Location r)
    {
        return new Assignment_Statement(e1, ident, e2);
    }


    public static class Return_Statement extends Statement
    {
        Location _left;
        public Expression _e;
        Location _right;

        public Return_Statement(Expression e)
        {
            _e = e;
        }

        public void visit2(String ClassIdent) throws Exception
        {	
        	
            _e.getType();
        }

        public String toString()
        {
            return "return " + _e.toString() + "\n";
        }
        public Expression getExpr()
        {	
			return _e;
        }
        

    }
    public static Assignment_Statement.Return_Statement returnStatement(Expression e)
    {
        return new Assignment_Statement.Return_Statement(e);
    }

    public static class While_Statement extends Statement
    {
        public Expression _expression;
        public List<Statement> _statements;

        public While_Statement(Expression e, List<Statement> stmts)
        {
            this._expression = e;
            this._statements = stmts;
        }

        public void visit2(String classIdent) throws Exception
        {
            // adds statements to table
            // type checks statements
            // removes statements from table
        	for (Statement s : this._statements)
            {
                s.visit2(classIdent);
            }
        }

        public String toString()
        {
            StringBuilder whileResult = new StringBuilder();
            whileResult.append("while ");
            whileResult.append(this._expression.toString());
            for (Statement s: this._statements)
            {
                whileResult.append("\t\n").append(s.toString());
            }
            return whileResult.toString();
        }


    }
    public static Statement.While_Statement whileStatement(Expression e, List<Statement> stmts)
    {
        return new Statement.While_Statement(e, stmts);
    }

    public static class If_Statement extends Statement
    {
        public Expression _expression;
        public List<Statement> _statements;
        public Statement _elseStatement;

        public If_Statement(Expression e, List<Statement> stmts)
        {
            this._expression = e;
            this._statements = stmts;
        }

        public If_Statement(Expression e, List<Statement> stmts, Statement elseStatement)
        {
            this._expression = e;
            this._statements = stmts;
            this._elseStatement = elseStatement;
        }

        public void visit2(String classIdent) throws Exception
        {
            for (Statement s : this._statements)
            {
                s.visit2(classIdent);
            }
            if (this._elseStatement != null)
                this._elseStatement.visit2(classIdent);
        }

        public String toString()
        {
            StringBuilder ifString = new StringBuilder();
            ifString.append("if ").append(_expression);
            StringBuilder stmts = new StringBuilder();
            for (Statement s : _statements)
            {
                stmts.append("\n\t\t").append(s.toString());
            }
            ifString.append(stmts.toString());
            if (_elseStatement != null)
                ifString.append("\n").append(_elseStatement.toString());
            return ifString.toString();
        }

    }
    public static Statement.If_Statement ifStatement(Expression e, List<Statement> stmts)
    {
        return new Statement.If_Statement(e, stmts);
    }
    public static Statement.If_Statement ifStatement(Expression e, List<Statement> stmts, Statement elseStatement)
    {
        return new Statement.If_Statement(e, stmts, elseStatement);
    }


    public static class Else_Statement extends Statement
    {
        public List<Statement> _elseStatements;

        public Else_Statement(List<Statement> elseStatements)
        {
            this._elseStatements = elseStatements;
        }

        public void visit2(String classIdent) throws Exception
        {
            for (Statement s : this._elseStatements)
            {
                s.visit2(classIdent);
            }
        }

        public String toString()
        {
            StringBuilder elseStatements = new StringBuilder();
            elseStatements.append("\telse");
            for (Statement s: this._elseStatements)
            {
                elseStatements.append("\n\t\t").append(s.toString());
            }
            return elseStatements.toString();
        }

    }
    public static Statement.Else_Statement elseStatement(List<Statement> elseStmts)
    {
        return new Statement.Else_Statement(elseStmts);
    }

    public static class Typecase extends Statement
    {
        String _typecaseIdent;
        List<Statement> _typeAlts = new LinkedList<>();

        public Typecase(String ident, List<Statement> typeAlts)
        {
            this._typecaseIdent = ident;
            this._typeAlts = typeAlts;
        }

        public void visit2(String classIdent)
        {
            // TODO
        }

        public String toString()
        {
            return "Typecase";
        }

    }
    public static Statement.Typecase typecase(String ident, List<Statement> typeAlts)
    {
        return new Statement.Typecase(ident, typeAlts);
    }


    public static class Type_Statement extends Statement
    {
        String _ident;
        String _type;
        List<Statement> _stmtList = new LinkedList<>();

        public Type_Statement(String ident, String type, List<Statement> stmts)
        {
            this._ident = ident;
            this._type = type;
            this._stmtList = stmts;
        }

        public void visit2(String classIdent)
        {
            // TODO
        }

        public String toString()
        {
            return "Typecase: " + this._ident;
        }

    }
    public static Statement.Type_Statement typeStatement(String ident, String type, List<Statement> stmts)
    {
        return new Statement.Type_Statement(ident, type, stmts);
    }

    public static class Expression_Statement extends Statement
    {
        Expression _expression;
        public Expression_Statement(Expression e)
        {
            this._expression = e;
        }

        public void visit2(String classIdent) throws Exception
        {
        	_expression.visit2(classIdent);
        }

        public String toString()
        {
            return this._expression.toString();
        }
    }
    public static Statement.Expression_Statement expressionStatement(Expression e)
    {
        return new Statement.Expression_Statement(e);
    }
	public Expression getLexpr() {
		return null;
		// TODO Auto-generated method stub
		
	}
	public Expression getRexpr() {
		return null;
		// TODO Auto-generated method stub
		
	}
	public String getDeclaredType() {
		return null;
		// TODO Auto-generated method stub
		
	}
	public Expression getExpr() {
		return null;
		// TODO Auto-generated method stub
		
	}
	
	
}
