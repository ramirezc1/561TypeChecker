import java.util.LinkedList;
import java.util.List;

public abstract class Class_Block
{
    public static class Clazz_Block
    {
        public String _classIdent;
        public Args _argList;
        public String _extendsIdent;
        List<Statement> _stmtList;
        List<Methods.Method> _methods;

        public Clazz_Block(String class_ident, Args argList, String extends_ident, List<Statement> stmts, List<Methods.Method> mthds)
        {
            this._classIdent = class_ident;
            this._argList = argList;
            this._extendsIdent = extends_ident;
            this._stmtList = stmts;
            this._methods = mthds;

        }

        public Clazz_Block(String class_ident, Args argList, List<Statement> stmts, List<Methods.Method> mthds)
        {
            this._classIdent = class_ident;
            this._argList = argList;
            this._stmtList = stmts;
            this._methods = mthds;


            // sig: all classes extend Obj by default right?
            this._extendsIdent = "Obj";
        }

        public Methods.Method getMethod(String methodName)
        {
        	for (Methods.Method m: _methods)
            {
                if(m._methodIdent.matches(methodName)) {
                	return m;
                }
            }
            return null;
        }
        public List<Methods.Method> getMethods()
        {
            return this._methods;
        }
        public List<Statement> getStatements()
        {
            return this._stmtList;
        }
        public Statement getStatement(String var) throws Exception
        {
        	for (Statement s: _stmtList)
            {
                if(s.getLexpr().getIdent().equals(var)) {
                	return s;
                }
            }
            return null;
        }

        public VarTable checkConstructor()
        {
//            VarTable varTable = new VarTable();
            for (Statement s : this._stmtList)
            {
                // TODO: populate VarTable with idents and their types for assignment statements of the form "this.__"
                System.out.println(s);
            }

            return null;
        }

        public void visit() throws Exception
        {
            System.out.println("visiting class " + _classIdent);
            ClassesTable ct = ClassesTable.getInstance();
            if(!ct.addClass(_classIdent, _extendsIdent))
            	throw new Exception("Class "+_classIdent + " already defined ");
            
         
        }
        public void visit2() throws Exception
        {
        	System.out.println("TypeChecking class " + _classIdent);
            ClassesTable ct = ClassesTable.getInstance();
            
            for (Args.Arg a : this._argList._args)
            {
                a.visit2(_classIdent);
            }
            for (Statement s : this._stmtList)
            {
                s.visit2(_classIdent);
                
            }
            
            LinkedList<String> classNames = new LinkedList<String>();
            for (Methods m : this._methods)
            {
            	if(classNames.contains(m.getMethodIdent()))
            		throw new Exception("Method "+ m.getMethodIdent() + " already defined");
            	else
            	classNames.add(m.getMethodIdent());
            	
            	if(VarTableSingleton.getCurrentInstance().getCurrentTable().getType(m.getMethodIdent())!=null)
            		throw new Exception("Method "+ m.getMethodIdent() + " has same name as a variable");
            	
                m.visit2(_classIdent);
            }
            
        }
        

        public String toString()
        {
            StringBuilder result = new StringBuilder();
            result.append("class ");
            result.append(_classIdent);
            result.append(_argList.toString());
            if (_extendsIdent != null)
            {
                result.append(" extends ");
                result.append(_extendsIdent);
            }
            result.append("\n\t");

            StringBuilder stmtResult = new StringBuilder();
            StringBuilder mthdResult = new StringBuilder();
            for (Statement s: this._stmtList)
            {
                stmtResult.append("\n\t").append(s);
            }
            for (Methods m : this._methods)
            {
                mthdResult.append("\n\t").append(m);
            }

            result.append(stmtResult.toString()).append("\n").append(mthdResult.toString());
            return result.toString();
        }
    }

    public static Class_Block.Clazz_Block class_block(String class_ident, Args argList, String extends_ident, List<Statement> stmts, List<Methods.Method> mthds)
    {
        return new Class_Block.Clazz_Block(class_ident, argList, extends_ident, stmts, mthds);
    }

    public static Class_Block.Clazz_Block class_block(String class_ident, Args argList, List<Statement> stmts, List<Methods.Method> mthds)
    {
        return new Class_Block.Clazz_Block(class_ident, argList, stmts, mthds);
    }


}
