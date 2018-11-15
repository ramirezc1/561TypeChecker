import java.util.List;

public abstract class Expression
{
    public Expression() { }
    abstract void visit2(String classIdent) throws Exception;
    abstract String getType() throws Exception;
    protected abstract String getIdent() throws Exception;
    
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

        public String getType() throws Exception
        {
            return e.getType();
        }



        public void visit2(String classIdent) throws Exception
        {
            e.visit2(classIdent);
        }

        public String toString()
        {
            return "(" + e + ")";
        }


		protected String getIdent() throws Exception
        {
            return e.getIdent();
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

        public Binex(Expression e1, String op, Expression e2)
        {
            this.e1 = e1;
            this.e2 = e2;
            this.op = op;
        }

        public String getType() throws Exception
        {
            String e1Type = e1.getType();
            String e2Type = e2.getType();
            String operatorString = OperatorToString.getOperatorDict().get(this.op);
            String rtype = TypeChecker.typeCheckOperator(e1Type, operatorString, e2Type);
            if (rtype == null)
                throw new Exception(operatorString + "(" + e1Type + ", " + e2Type + ") not defined");
            return rtype;
        }

        public void visit2(String classIdent) throws Exception
        {
            e1.visit2(classIdent);
            e2.visit2(classIdent);
        }

        public String toString()
        {
            return e1 + " " + op + " " + e2;
        }

		protected String getIdent() {
			// there is no identifier for a binary expression
            return null;
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
        public int _left, _right;
        public Unex(String op, Expression e1, int left, int right)
        {
            this.e1 = e1;
            this.op = op;
            this._left = left;
            this._right = right;
        }

        public String getType() throws Exception
        {
            String eType = this.e1.getType();
            String opString = OperatorToString.getUnaryOperatorDict().get(this.op);
            String unopType = TypeChecker.typeCheckUnaryOperator(eType, opString);
            return unopType;
        }

        public void visit2(String classIdent) throws Exception
        {
            e1.visit2(classIdent);
        }

        public String toString()
        {
            return op + e1.toString();
        }


		protected String getIdent() {
            // there is no identifier for a unary expression
			return null;
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

        public void visit2(String classIdent)
        {
            // TODO
        }

        public String toString()
        {
            return _s;
        }

		protected String getIdent() {
            // there is no identifier for a string lit
			return null;
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

        public void visit2(String classIdent)
        {
            // TODO
        }

        public String toString()
        {
            return i + "";
        }

		protected String getIdent() {
            // there is no identifier for an int const
			return null;
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
            if (ident.equals("true") || ident.equals("false"))
            {
                return "Boolean";
            }
            String identType = VarTableSingleton.getTableByClassName(TypeChecker.currentClass).GetVarType(this.ident);
            return identType;
        }

        public void visit2(String classIdent) throws Exception
        {
        	 ClassesTable ct = ClassesTable.getInstance();
             if(ct.classTable.containsKey(ident))
             	throw new Exception("Var "+ ident + " (" + _left + ", " + _right + ") has same name as class ");
            VarTableSingleton.getCurrentInstance();
			VarTable varTable = VarTableSingleton.getTableByClassName(classIdent);
			
			String type = varTable.ExistsInVarTable(ident);
			if(type == null)
			{
				type = ClassesTable.getInstance().getClass("Nothing");
			}

			Var var = new Var(ident, type);
			varTable.AddToVarTable(var);
        }

        public String toString()
        {
            return ident;
        }

		protected String getIdent() {
			
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
        public String _ident;
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

        public String getType() throws Exception
        {
            if (this._e != null)
            {
                if (!this._e.getIdent().equals("this"))
                    throw new Exception("Class variable " + _e.getIdent() + "." + this._ident + " is private.");
                return VarTableSingleton.getTableByClassName(TypeChecker.currentClass).GetConstructorType(_e.getIdent() + "." + _ident);

            }
            return VarTableSingleton.getTableByClassName(TypeChecker.currentClass).GetVarType(this._ident);

        }
        public String getIdent() throws Exception
        {
            if (this._e != null)
            {
                if (!this._e.getIdent().equals("this"))
                    throw new Exception("Cannot access private variables in class " + this._e.getIdent());
                return _e.getIdent() + "." + _ident;
            }
        	return _ident;
        }

        public void visit2(String classIdent) throws Exception
        {
        	_e.visit2(classIdent);
        	ClassesTable ct = ClassesTable.getInstance();
            if(ct.classTable.containsKey(_ident))
            	throw new Exception("Var "+ _ident + " has same name as class ");
        	
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

    // Constructor class is for when a class gets instantiated: C1(4, "example");
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
            // the class identifier is the type, right?
        	
            return this._ident;
        }

        public void visit2(String classIdent) throws Exception
        {
        	//check constructor
        	List<Class_Block.Clazz_Block> class_blocks = TypeChecker.ast.get_cbs();
            for (Class_Block.Clazz_Block class_block : class_blocks)
            {
                if(class_block._classIdent.equals(_ident)) {
                	int i=0;
//                	System.out.println(class_block._argList._args.size());
//                	LinkedList<Expression> aSuper = _args.getArgs();
//                	System.out.println(_args.getArgs().get(0).getType());
                	
                	if(!(class_block._argList._args.size()==_args.getArgs().size()))
                		throw new Exception("Class "+class_block._classIdent+ "is getting instantiated with the wrong number of arguments");
                	for (Args.Arg a: class_block._argList._args)
                    {
                		
                		if(!TypeChecker.checkSubtype(a._type, _args.getArgs().get(i).getType()))
            				//if(type is super type)
            				throw new Exception("Problem with arguments in constructor "+a._type+" is not a subtype of "+_args.getArgs().get(i).getType());
                		i++;
                    }
                }
                	
            }
        }

        public String toString()
        {
            return _ident + _args;
        }
		protected String getIdent() {
			
			return _ident;
		}

    }
    public static Expression.Constructor constructor(String ident, Args args, int left, int right)
    {
        return new Expression.Constructor(ident, args, left, right);
    }
	

}