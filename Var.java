

public class Var
{
    public String ident;
    public String type;

    public Var(String i, String t)
    {
        this.ident = i;
        this.type = t;
    }

    public void UpdateType(String newType)
    {
        this.type = newType;
    }
}

