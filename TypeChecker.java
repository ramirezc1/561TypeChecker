import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TypeChecker {
	
	static Program builtinAST;
	static Program ast;
	ClassesTable classesTable;
	VarTable varTable;
	
	public TypeChecker(Program builtinAST, Program ast){
		TypeChecker.ast= ast;
		TypeChecker.builtinAST= builtinAST;
		classesTable = ClassesTable.getInstance();
	}
	
	 public boolean TypeCheck() throws Exception {

	        ast.visit();
	       

	       checkForUndefined();
	       
	        System.out.println("Passed check for undefined class inheritance");
	        checkForCycles();
	        System.out.println("Passed check for class cycles");

	        if (checkConstructor())
	        {
	            throw new Exception("Bad constructor");
	        }
	        System.out.println("Passed check for subclass matching constructor of parent class");
			return true;
	    }

	public static String typeCheckOperator(String classType, String operation, String argumentType)
    {
        for (Class_Block.Clazz_Block cb : builtinAST.get_cbs())
        {
            if (cb._classIdent.equals(classType))
            {
                for (Methods.Method m : cb.getMethods())
                {
                    if (m._methodIdent.equals(operation))
                    {
                        ArrayList<String> argTypes = ((Args.Formal_Args) m._formalArgs).getArgTypes();
                        if (argTypes.size() == 1)
                        {
                            if (argTypes.get(0).equals(argumentType))
                                return m._methodType;
                        }
                    }
                }
            }
        }
        // again, but for ast
        for (Class_Block.Clazz_Block cb : ast.get_cbs())
        {
            if (cb._classIdent.equals(classType))
            {
                for (Methods.Method m : cb.getMethods())
                {
                    if (m._methodIdent.equals(operation))
                    {
                        ArrayList<String> argTypes = ((Args.Formal_Args) m._formalArgs).getArgTypes();
                        if (argTypes.size() == 1)
                        {
                            if (argTypes.get(0).equals(argumentType))
                                return m._methodType;
                        }
                    }
                }
            }
        }

        return null;
    }
	void checkForUndefined() throws Exception
    {
        HashMap<String, String> clazzTable = classesTable.getClassTable();
        for (String ident : clazzTable.values())
        {
            String currExtends = clazzTable.get(ident);
            if (currExtends == null)
            {
            	throw new Exception("Undefined class "+ident);
            }
        }

    }

    void checkForCycles() throws Exception
    {
        HashMap<String, String> clazzTable = classesTable.getClassTable();
        for (String ident : clazzTable.values())
        {
            String startingIdent = ident;
            String currIdent = ident;
            String currExtends = clazzTable.get(currIdent);
            while (!currExtends.equals("Obj"))
            {
                if (currExtends.equals(startingIdent))
                {
                	throw new Exception("Class "+currIdent+" and Class "+currExtends +" have cycles");
                }
                currIdent = currExtends;
                currExtends = clazzTable.get(currIdent);
            }
        }
    }

    boolean checkConstructor()
    {
        List<Class_Block.Clazz_Block> class_blocks = ast.get_cbs();
        for (Class_Block.Clazz_Block class_block : class_blocks)
        {
            VarTable vt = class_block.getConstructor();
        }
        return false;
    }

}
