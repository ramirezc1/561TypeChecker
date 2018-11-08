import java.util.HashMap;

public class OperatorToString
{
    private static HashMap<String, String> operatorDict;

    public static HashMap<String ,String> getOperatorDict()
    {
        if (operatorDict == null)
        {
            operatorDict = new HashMap<>();
            operatorDict.put("+", "PLUS");
            operatorDict.put("==", "EQUALS");
            operatorDict.put("<=", "ATMOST");
            operatorDict.put("<", "LESS");
            operatorDict.put(">=", "ATLEAST");
            operatorDict.put(">", "MORE");
            operatorDict.put("*", "TIMES");
            operatorDict.put("-", "MINUS");
            operatorDict.put("/", "DIVIDE");
        }

        return operatorDict;
    }
}