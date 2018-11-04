public class Expression extends Obj
{
    public Expression() { }

    public static class Priority extends Expression
    {
        public Expression e;

        public Priority(Expression e)
        {
            this.e = e;
        }

        public String toString()
        {
            return "(" + e + ")";
        }
    }
    public static Expression.Priority priority(Expression e)
    {
        return new Expression.Priority(e);
    }



    public static class Binex extends Expression
    {
        public Expression e1, e2;
        public String op;

        public Binex(Expression e1, String op, Expression e2)
        {
            this.e1 = e1;
            this.e2 = e2;
            this.op = op;
        }

        public String toString()
        {
            return e1 + " " + op + " " + e2;
        }

    }
    public static Expression.Binex binop(Expression e1, String op, Expression e2)
    {
        return new Expression.Binex(e1, op, e2);
    }



    public static class Unex extends Expression
    {
        public String op;
        public Expression e1;

        public Unex(String op, Expression e1)
        {
            this.e1 = e1;
            this.op = op;
        }

        public String toString()
        {
            return op + e1.toString();
        }
    }
    public static Expression.Unex unop(String op, Expression e)
    {
        return new Expression.Unex(op, e);
    }



    public static class StringLit extends Expression
    {
        public String _s;

        public StringLit(String s)
        {
            this._s = s;
        }

        public String toString()
        {
            return _s;
        }
    }
    public static Expression.StringLit stringLit(String s)
    {
        return new Expression.StringLit(s);
    }


    public static class IntConst extends Expression
    {
        public int i;

        public IntConst(int i)
        {
            this.i = i;
        }

        public String toString()
        {
            return i + "";
        }
    }
    public static Expression.IntConst intconst(int i)
    {
        return new Expression.IntConst(i);
    }



    public static class Identifier extends Expression
    {
//        public Location left, right;
        public String i;

        public Identifier(String i)
        {
            this.i = i;
//            this.left = l;
//            this.right = r;
        }

        public String toString()
        {
            return i;
        }
    }
    public static Expression.Identifier ident(String s)
    {
        return new Expression.Identifier(s);
    }


    public static class Method_Call extends Expression
    {
        Expression _e;
        String _ident;
        Args _optionalArgs;
        public Method_Call(Expression e, String ident)
        {
            this._e = e;
            this._ident = ident;
        }

        public Method_Call(Expression e, String ident, Args args)
        {
            this._e = e;
            this._ident = ident;
            this._optionalArgs = args;
        }

        public String toString()
        {
            StringBuilder args = new StringBuilder();
            if (this._optionalArgs != null)
            {
                args.append(this._optionalArgs.toString());
            }
            return _e + "." + _ident + args;
        }
    }
    public static Expression.Method_Call methodCall(Expression e, String ident)
    {
        return new Expression.Method_Call(e, ident);
    }
    public static Expression.Method_Call methodCall(Expression e, String ident, Args args)
    {
        return new Expression.Method_Call(e, ident, args);
    }

    public static class Constructor extends Expression
    {
        String _ident;
        Args _args;
        public Constructor(String ident, Args args)
        {
            this._ident = ident;
            this._args = args;
        }
        public String toString()
        {
            return _ident + _args;
        }
    }
    public static Expression.Constructor constructor(String ident, Args args)
    {
        return new Expression.Constructor(ident, args);
    }

}