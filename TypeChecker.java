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
		 	//first visit only classes
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
	        //Second Visit
	        //visit to typeCheck everything
	        ast.visit2();
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
			 
				 if(!(cb._extendsIdent==currentSuper)) {
					 ex= builtinAST.get_cb(cb._extendsIdent);
					 if(ex == null)
						 ex= ast.get_cb(cb._extendsIdent);
				 }
				 //typecheck instance variables
				 if(!ex._stmtList.isEmpty())
				 {
					 
					 //check that the same instance vars declared in super are declared in inherrited class
					 //check that inherited instance vars are subtype of super class
			        for (Statement sSuper : ex._stmtList)
			        {
			        	
			           if(sSuper.getLexpr()!=null)
			           if(sSuper.getLexpr().toString().contains("this.")) {
			        	   s = cb.getStatement(sSuper.getLexpr().getIdent());
			           if(s==null)
			        	   throw new Exception("Super instance variable "+sSuper.getLexpr() +" not defined");
			          
			           
			         //inherited variable must be compatible with super type
			           	if(s.getDeclaredType()==""||sSuper.getDeclaredType()=="") {
			           		if(!checkSubtype(s.getRexpr().getType(), sSuper.getRexpr().getType())){
			           			throw new Exception("Problem with type of inherrited instance variable "+sSuper.getLexpr()+" of type "+s.getRexpr().getType()+ " is not a subtype of "+ sSuper.getRexpr().getType());
			           		}
			           	}
			           	else {
			           		if(!checkSubtype(s.getDeclaredType(), sSuper.getDeclaredType())){
			           			throw new Exception("Problem with type of inherrited instance variable "+sSuper.getLexpr().getIdent()+" : "+s.getDeclaredType()+ " is not a subtype of "+ sSuper.getDeclaredType());
			           		}
			           	}
			          
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
	
	private void checkOverridenMethod(Methods.Method m, Methods.Method mSuper) throws Exception {
		//make sure it has same number of arguments,
		//each argument is of correct type
		//return type is subtype of the type r inherited of the superclass method
		if(m._formalArgs._args.size()!=mSuper._formalArgs._args.size())
			throw new Exception("Overriden Method does not have same number of arguments");
		for (int i=0; i<m._formalArgs._args.size();i++)
	    {
			//check arguments to see if type matches
			//must be a inherited <: a (the overriding
			//System.out.println(m._formalArgs._args.get(i)._type);
			//System.out.println(mSuper._formalArgs._args.get(i)._type);
			
			if(!checkSubtype(m._formalArgs._args.get(i)._type,mSuper._formalArgs._args.get(i)._type))
				//if(type is super type)
				throw new Exception("Problem with arguments "+m._formalArgs._args.get(i)._type+" is not a subtype of "+mSuper._formalArgs._args.get(i)._type);
			
	    }
		
		//typecheck return statement declared type
		if(!checkSubtype(m._methodType, mSuper._methodType)) {
			//if(type is subtype)
			throw new Exception("Problem with return: "+m._methodType+ " is not a subtype of "+ mSuper._methodType);
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

	// sam's version - probably useless
//	public static boolean isSubType(String oldType, String newType)
//    {
//        String parentType = ClassesTable.getInstance().getClass(newType);
//        while (!parentType.equals(oldType) && !parentType.equals("Obj"))
//        {
//            parentType = ClassesTable.getInstance().getClass(parentType);
//        }
//        return parentType.equals(oldType);
//    }


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

    public static String typeCheckUnaryOperator(String classType, String operation)
    {
        for (Class_Block.Clazz_Block cb : builtinAST.get_cbs())
        {
            if (cb._classIdent.equals(classType))
            {
                for (Methods.Method m : cb.getMethods())
                {
                    if (m._methodIdent.equals(operation))
                    {
                        return m._methodType;
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
                        return m._methodType;
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
