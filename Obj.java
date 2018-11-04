
public class Obj
{
    public String STR() { return this.toString(); }

    public void PRINT() { System.out.println(this.STR());}

    public boolean EQUALS(Obj o1, Obj o2) { return o1 == o2; }
}
