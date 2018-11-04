import java_cup.runtime.ComplexSymbolFactory.Location;

import java.util.LinkedList;
import java.util.List;

public abstract class Statement extends Obj
{
    public Statement() { }

    public static class Assignment_Statement extends Statement
    {
        Location _left;
        Location _right;
        public Expression _e1;
        public Expression _e2;
        public String _ident = "";

        public Assignment_Statement(Expression e1, Expression e2)
        {
            this._e1 = e1;
            this._e2 = e2;
        }

        public Assignment_Statement(Expression e1, String ident, Expression e2)
        {
            this._e1 = e1;
            this._e2 = e2;
            this._ident = ident;
        }

        public String toString()
        {
            String type ="";
            if (!_ident.equals("")) type = ": " + _ident + " ";
            return _e1 + type + " = " + _e2;
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

        public String toString()
        {
            return "return " + _e.toString() + "\n";
        }
    }
    public static Assignment_Statement.Return_Statement returnStatement(Expression e)
    {
        return new Assignment_Statement.Return_Statement(e);
    }

    public static class While_Statement extends Statement
    {
        public Expression _expression;
        public List<Statement> _statements = new LinkedList<>();

        public While_Statement(Expression e, List<Statement> stmts)
        {
            this._expression = e;
            this._statements = stmts;
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
        public List<Statement> _statements = new LinkedList<>();
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
        public List<Statement> _elseStatements = new LinkedList<Statement>();

        public Else_Statement(List<Statement> elseStatements)
        {
            this._elseStatements = elseStatements;
        }

        public String toString()
        {
            StringBuilder elseStatements = new StringBuilder();
            for (Statement s: this._elseStatements)
            {
                elseStatements.append("\n").append(s.toString());
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
        public String toString()
        {
            return this._expression.toString();
        }
    }
    public static Statement.Expression_Statement expressionStatement(Expression e)
    {
        return new Statement.Expression_Statement(e);
    }
}
