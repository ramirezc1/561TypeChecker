import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;


public class TypeChecker {
	
	static Program builtinAST;
	static Program ast;
	ClassesTable classesTable;
	VarTable varTable;
	Tree tree;
	
	public TypeChecker(Program builtinAST, Program ast){
		TypeChecker.ast= ast;
		TypeChecker.builtinAST= builtinAST;
		classesTable = ClassesTable.getInstance();
		tree= Tree.getInstance();
	}
	
	 public boolean TypeCheck() throws Exception {

	        ast.visit();
	        checkForUndefined();
	        System.out.println("Passed check for undefined class inheritance");
	        checkForCycles();
	        createLattice();
	        System.out.println("Passed check for class cycles");
	        if (checkConstructor())
	        {
	            throw new Exception("Bad constructor");
	        }
	        checkOverriden();
	       // System.out.println(" : "+tree.LCA(tree.getRoot(), "D","X"));
	        
	        System.out.println("Passed check for subclass matching constructor of parent class");
			return true;
	    }

	private void createLattice() throws Exception {
	    Tree tree = Tree.getInstance();
	    ClassesTable ct = ClassesTable.getInstance();
	    HashMap<String, String> c = new HashMap<>();
	    c.putAll(ct.classTable);
	    Node root = new Node("Obj");
     		 tree.setRoot(root);
     	c.remove("Obj", "Obj");
     	Iterator<Entry<String, String>> i = c.entrySet().iterator();
     	Entry<String,String> entry = null;
     	String key = "Obj";

     	while(true) {
     		
     		Node n =tree.findNode(root, key);
     		while(n==null) {
     			key = i.next().getKey();
     			n =tree.findNode(root, key);
     		}
     		entry =getElement(c,key);
     		while(entry!=null) {

     			n.addChild(new Node(entry.getKey()));
     			c.remove(entry.getKey());
     			entry =getElement(c,key);
     		
     		}
     		
     		if(c.isEmpty())
     			break;
     		key = c.entrySet().iterator().next().getValue();

     	}
		
	}
	public Entry<String, String> getElement(HashMap<String, String> t, String v) {
		for(Entry<String, String> entry: t.entrySet()){
            if(v.equals(entry.getValue())){
                return entry;
            }
        }
		return null;
	}
	

	private void checkOverriden() throws Exception {
		//check each classblock
		//check to see if super class has any method
		String currentSuper="";
		Class_Block.Clazz_Block ex=null;
		Methods.Method m= null;
		Statement s= null;
		//check each classblock
		 for (Class_Block.Clazz_Block cb : ast.get_cbs())
	      {	 
			 if(!cb._methods.isEmpty()) {
				 if(!(cb._extendsIdent==currentSuper)) {
					 ex= builtinAST.get_cb(cb._extendsIdent);
					 if(ex == null)
						 ex= ast.get_cb(cb._extendsIdent);
				 }
				 //typecheck instance variables
				 if(!ex._stmtList.isEmpty())
				 {
			        for (Statement sSuper : ex._stmtList)
			        {
			        	//System.out.println(sSuper.getLexpr().getClass().getSimpleName());
			           if(sSuper.getLexpr().getClass().getSimpleName().equals("Method_Call")) {
			           s = cb.getStatement(sSuper.getLexpr().getIdent());
			           
			           if(s==null)
			        	   throw new Exception("Super instance variable "+sSuper.getLexpr() +" not defined");
			           
			           //????????
			           //have way to check type of instance variable
			          //inherited variable must be compatible with super type
			           //System.out.println(sSuper);
			           //System.out.println(s.getLexpr().getIdent());
//			           if(!s.getLexpr().getIdent().equals(sSuper.getLexpr().getIdent()))
//			        		   throw new Exception("Super instance variables do not match");
			           }
			        }
				 }
				 //check to see if superclass has methods
				 if(!ex._methods.isEmpty()) { 			
				//check each method
				 for(Methods.Method mSuper : ex.getMethods()) {
					 m =cb.getMethod(mSuper._methodIdent);
					 if(m!=null) {
						 checkOverridenMethod(m, mSuper);
					 }
						
				 }
			 
				 }
			 }
	      }
		
	}
	
	private void checkOverridenMethod(Methods.Method m, Methods.Method mSuper) throws Exception {
		//make sure it has same number of arguments,
		//each argument is of correct type
		//return type is subtype of the type r inherited of the superclass method
		if(m._formalArgs._args.size()!=mSuper._formalArgs._args.size())
			throw new Exception("Overriden Method does not have same number of arguments");
		for (int i=0; i<m._formalArgs._args.size();i++)
	    {
			//?????MUST HAVE WAY to check subtype
			//RIGHT NOW IT ONLY CHECKS SAME TYPE
			//check arguments to see if type matches
			//must be a inherited <: a (the overriding
			//System.out.println(m._formalArgs._args.get(i)._type);
			//System.out.println(mSuper._formalArgs._args.get(i)._type);
			if(!m._formalArgs._args.get(i)._type.equals(mSuper._formalArgs._args.get(i)._type))
				
				//if(type is super type)
				throw new Exception("Arguments in super class method do not match");
			
	    }
		
		//typecheck return statement
		if(!m._methodType.equals(mSuper._methodType)) {
			//if(type is subtype)
			getSubtype();
			throw new Exception("Method return type do not match with super");
		}
		
	}

	private void getSubtype() {
		// TODO Auto-generated method stub 
		
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
