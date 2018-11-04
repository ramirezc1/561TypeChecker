import java.util.List;

public abstract class Class_Block extends Obj
{
    public Class_Block() { }

    public static class Clazz extends Class_Block
    {
        public String _classIdent;
        public Args _argList;
        public String _extendsIdent;
        public Class_Body _classBody;

        public Clazz(String class_ident, Args argList, String extends_ident, Class_Body class_body)
        {
            this._classIdent = class_ident;
            this._argList = argList;
            this._extendsIdent = extends_ident;
            this._classBody = class_body;
        }

        public Clazz(String class_ident, Args argList, Class_Body class_body)
        {
            this._classIdent = class_ident;
            this._argList = argList;
            this._classBody = class_body;
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
            result.append(_classBody);
            return result.toString();
        }
    }
    public static Class_Block.Clazz clazz(String class_ident, Args argList, String extends_ident, Class_Body class_body)
    {
        return new Class_Block.Clazz(class_ident, argList, extends_ident, class_body);
    }

    public static Class_Block.Clazz clazz(String class_ident, Args argList, Class_Body class_body)
    {
        return new Class_Block.Clazz(class_ident, argList, class_body);
    }


    public static class Class_Body extends Class_Block
    {
        List<Statement> _stmtList;
        List<Methods> _methods;
        public Class_Body(List<Statement> stmts, List<Methods> mthds)
        {
            this._stmtList = stmts;
            this._methods = mthds;
        }

        public String toString()
        {
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

            return stmtResult.toString() + "\n" + mthdResult.toString();
        }
    }
    public static Class_Block.Class_Body classBody(List<Statement> stmts, List<Methods> mthds)
    {
        return new Class_Block.Class_Body(stmts, mthds);
    }

}
