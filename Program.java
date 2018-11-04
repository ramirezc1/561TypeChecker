//import java.util.List;
//


import java.util.List;

public class Program extends Obj
{
    List<Class_Block> _cbs;
    List<Statement> _stmts;
    public Program(List<Class_Block> cb, List<Statement> stmts)
    {
        this._cbs = cb;
        this._stmts = stmts;
        //System.out.println(this.toString());
    }


    public String toString(){
        StringBuilder classesString = new StringBuilder();
        StringBuilder statementsString = new StringBuilder();

        if (_cbs != null)
        {
            for (Class_Block c: _cbs) classesString.append(c.toString());
        }
        if (_stmts != null)
        {
            for (Statement s: _stmts)
            {
                statementsString.append("\t").append(s.toString()).append("\n");
            }
        }

        return "Classes:\n" + classesString + "\nStatements:\n" + statementsString;
    }

}
