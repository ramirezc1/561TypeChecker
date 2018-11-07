import java.util.List;

public abstract class Class_Block
{
    public static class Clazz_Block
    {
        public String _classIdent;
        public Args _argList;
        public String _extendsIdent;
        List<Statement> _stmtList;
        List<Methods> _methods;

        public Clazz_Block(String class_ident, Args argList, String extends_ident, List<Statement> stmts, List<Methods> mthds)
        {
            this._classIdent = class_ident;
            this._argList = argList;
            this._extendsIdent = extends_ident;
            this._stmtList = stmts;
            this._methods = mthds;

        }

        public Clazz_Block(String class_ident, Args argList, List<Statement> stmts, List<Methods> mthds)
        {
            this._classIdent = class_ident;
            this._argList = argList;
            this._stmtList = stmts;
            this._methods = mthds;


            // sig: all classes extend Obj by default right?
            this._extendsIdent = "Obj";
        }

        public VarTable getConstructor()
        {
            VarTable varTable = new VarTable();
            for (Statement s : this._stmtList)
            {
                // TODO: populate VarTable with idents and their types for assignment statements of the form "this.__"
                System.out.println(s);
            }

            return varTable;
        }

        public void visit()
        {
            System.out.println("visiting class " + _classIdent);
            ClassesTable ct = ClassesTable.getInstance();
            ct.addClass(_classIdent, _extendsIdent);
            for (Statement s : this._stmtList)
            {
                s.visit();
            }
            for (Methods m : this._methods)
            {
                m.visit();
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

    public static Class_Block.Clazz_Block class_block(String class_ident, Args argList, String extends_ident, List<Statement> stmts, List<Methods> mthds)
    {
        return new Class_Block.Clazz_Block(class_ident, argList, extends_ident, stmts, mthds);
    }

    public static Class_Block.Clazz_Block class_block(String class_ident, Args argList, List<Statement> stmts, List<Methods> mthds)
    {
        return new Class_Block.Clazz_Block(class_ident, argList, stmts, mthds);
    }


}
