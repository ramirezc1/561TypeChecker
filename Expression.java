public abstract class Expression
{
    public Expression() { }
    abstract void visit();
    abstract String getType();

    public static class Priority extends Expression
    {
        public Expression e;
        public int _left, _right;

        public Priority(Expression e, int left, int right)
        {
            this.e = e;
            this._left = left;
            this._right = right;
        }

        public String getType()
        {
            return e.getType();
        }



        public void visit()
        {
            e.visit();
        }

        public String toString()
        {
            return "(" + e + ")";
        }
    }
    public static Expression.Priority priority(Expression e, int left, int right)
    {
        return new Expression.Priority(e, left, right);
    }



    public static class Binex extends Expression
    {
        public Expression e1, e2;
        public String op;
        public int _left, _right;

        public Binex(Expression e1, String op, Expression e2, int left, int right)
        {
            this.e1 = e1;
            this.e2 = e2;
            this.op = op;
            this._left = left;
            this._right = right;
        }

        public String getType()
        {
            String e1Type = e1.getType();
            String e2Type = e2.getType();
            String operatorString = OperatorToString.getOperatorDict().get(this.op);
            String rtype = Main.typeCheckOperator(e1Type, operatorString, e2Type);
            // TODO: throw error if rtype is null
            return rtype;
        }

        public void visit()
        {
            e1.visit();
            e2.visit();
        }

        public String toString()
        {
            return e1 + " " + op + " " + e2;
        }

    }
    public static Expression.Binex binop(Expression e1, String op, Expression e2, int left, int right)
    {
        return new Expression.Binex(e1, op, e2, left, right);
    }



    public static class Unex extends Expression
    {
        public String op;
        public Expression e1;
        public int _left, _right;
        public Unex(String op, Expression e1, int left, int right)
        {
            this.e1 = e1;
            this.op = op;
            this._left = left;
            this._right = right;
        }

        public String getType()
        {
            return "";
        }

        public void visit()
        {
            e1.visit();
        }

        public String toString()
        {
            return op + e1.toString();
        }
    }
    public static Expression.Unex unop(String op, Expression e, int left, int right)
    {
        return new Expression.Unex(op, e, left, right);
    }



    public static class StringLit extends Expression
    {
        public String _s;
        public int _left, _right;
        public StringLit(String s, int left, int right)
        {
            this._s = s;
            this._left = left;
            this._right = right;
        }

        public String getType()
        {
            return ClassesTable.getInstance().getClass("String");
        }

        public void visit()
        {
            // TODO
        }

        public String toString()
        {
            return _s;
        }
    }
    public static Expression.StringLit stringLit(String s, int left, int right)
    {
        return new Expression.StringLit(s, left, right);
    }


    public static class IntConst extends Expression
    {
        public int i;
        public int _left, _right;
        public IntConst(int i, int left, int right)
        {
            this.i = i;
            this._left = left;
            this._right = right;
        }

        public String getType()
        {
            return ClassesTable.getInstance().getClass("Int");
        }

        public void visit()
        {
            // TODO
        }

        public String toString()
        {
            return i + "";
        }
    }
    public static Expression.IntConst intconst(int i, int left, int right)
    {
        return new Expression.IntConst(i, left, right);
    }




    public static class Identifier extends Expression
    {
        //        public Location left, right;
        public int _left, _right;
        public String ident;

        public Identifier(String i, int left, int right)
        {
            this.ident = i;
            this._left = left;
            this._right = right;
        }

        public String getType()
        {
            return "";
        }

        public void visit()
        {
            VarTable varTable = VarTable.getInstance();
            try
            {
                String type = ClassesTable.getInstance().getClass("Nothing");
                Var var = new Var(ident, type);
                varTable.addVar(var);
            }
            catch (Exception e)
            {
                System.out.println(e.getMessage());
            }
        }

        public String toString()
        {
            return ident;
        }
    }
    public static Expression.Identifier ident(String s, int left, int right)
    {
        return new Expression.Identifier(s, left, right);
    }


    public static class Method_Call extends Expression
    {
        Expression _e;
        String _ident;
        Args _optionalArgs;
        public int _left, _right;

        public Method_Call(Expression e, String ident, int left, int right)
        {
            this._e = e;
            this._ident = ident;
            this._left = left;
            this._right = right;
        }

        public Method_Call(Expression e, String ident, Args args, int left, int right)
        {
            this._e = e;
            this._ident = ident;
            this._optionalArgs = args;
            this._left = left;
            this._right = right;
        }

        public String getType()
        {
            return "";
        }

        public void visit()
        {

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
    public static Expression.Method_Call methodCall(Expression e, String ident, int left, int right)
    {
        return new Expression.Method_Call(e, ident, left, right);
    }
    public static Expression.Method_Call methodCall(Expression e, String ident, Args args, int left, int right)
    {
        return new Expression.Method_Call(e, ident, args, left, right);
    }

    public static class Constructor extends Expression
    {
        String _ident;
        Args _args;
        public int _left, _right;
        public Constructor(String ident, Args args, int left, int right)
        {
            this._ident = ident;
            this._args = args;
            this._left = left;
            this._right = right;
        }

        public String getType()
        {
            return "";
        }

        public void visit()
        {

        }

        public String toString()
        {
            return _ident + _args;
        }
    }
    public static Expression.Constructor constructor(String ident, Args args, int left, int right)
    {
        return new Expression.Constructor(ident, args, left, right);
    }

}