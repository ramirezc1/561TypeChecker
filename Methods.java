import java.util.List;

public abstract class Methods
{
    public Methods() { }
    abstract void visit(ClassesTable ct) throws Exception;

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
        	System.out.println("TypeChecking Method "+_methodIdent);
        	if(ct.classTable.containsKey(_methodIdent))
        		throw new Exception("Method "+_methodIdent + " has same name as class ");
        	
        	//Make sure args have existing type, 
        	_formalArgs.visit();
        	//Make sure return type exists
        	if(!ct.classTable.containsKey(_methodType))
        		throw new Exception("MethodType "+_methodType + " does not exist");
        	
        	for (Statement s : this._statements)
            {
        		
        		if(s.toString().contains("return ")) {
        			if(!checkSubtype(s.getExpr().getType(), _methodType)) {
        				//if(type is subtype)
        				throw new Exception("Problem with return: "+s.getExpr().getType()+ " is not a subtype of "+ _methodType);
        			}
        			
        		}
        		else {
        			s.visit();
        		}
            }
        }
        public static boolean checkSubtype(String typeInherited, String typeSuper) throws Exception {
    		//check that types are valid
    		if(!Tree.getInstance().exists(typeInherited))
    			throw new Exception("Problem: " + typeInherited + " is not a valid type");
    		if (!Tree.getInstance().exists(typeSuper))
            {
                throw new Exception("Problem: " + typeSuper + " is not a valid type");
            }
    		
    		Node n= Tree.getInstance().LCA(Tree.getInstance().getRoot(),typeInherited,typeSuper);
    		
    		//returns false if typeInherited is not a subtype of SuperType
    		if(n==null||!n.toString().equals(typeSuper))
    			return false;
    		
    		return true;
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
