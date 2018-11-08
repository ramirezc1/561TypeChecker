
//Starter code from Cool 

import java_cup.runtime.ComplexSymbolFactory;
import java_cup.runtime.Symbol;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.Options;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Main {
    
    // Command line options
    String sourceFile = ""; 

    // Internal state
    ErrorReport report;

    // built-in program
    public static Program builtinAST;
    public static Program ast;

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


    ClassesTable classesTable;
    VarTable varTable;

    boolean DebugMode = false; // True => parse in debug mode 


    static public void main(String args[])
    {
        Main q = new Main();
        q.go(args);
    }

    public void go(String[] args)
    {
        report = new ErrorReport(); 
        parseCommandLine(args);
        parseProgram();
    }

    void parseCommandLine(String args[])
    {
	    try
        {
            // Command line parsing
            Options options = new Options();
            options.addOption("d", false, "debug mode (trace parse states)");
            CommandLineParser  cliParser = new GnuParser();
            CommandLine cmd = cliParser.parse( options, args);
            DebugMode = cmd.hasOption("d");
            String[] remaining = cmd.getArgs();
            int argc = remaining.length;
            if (argc == 0)
            {
                report.err("Input file name required");
                System.exit(1);
            }
            else if (argc == 1)
            {
                sourceFile = remaining[0];
            }
            else
            {
                report.err("Only 1 input file name can be given;"+ " ignoring other(s)");
            }
        }
        catch (Exception e)
        {
            System.err.println("Argument parsing problem");
            System.err.println(e.toString());
            System.exit(1);
        }
    }

    void parseProgram()
    {
        System.out.println("Beginning parse ...");
        try
        {
            classesTable = ClassesTable.getInstance();

            Symbol result;
            ComplexSymbolFactory symbolFactory = new ComplexSymbolFactory();
            Lexer scanner = new Lexer (new FileReader( "built-ins.qk" ), symbolFactory);
                parser p = new parser( scanner, symbolFactory);
                result = p.parse();
            //ast of built in clasess
            builtinAST = (Program) result.value;
            builtinAST.visit();
            //System.out.println("Built in classes parsed, ast built");
//            System.out.println(builtinAST.toString());

            scanner = new Lexer (new FileReader ( sourceFile), symbolFactory);
                p = new parser( scanner, symbolFactory);
                p.setErrorReport(report);

            if (DebugMode) { result =  p.debug_parse(); }
            else
            {
                result = p.parse();
            }

            ast = (Program) result.value;


            TypeCheckProgram(ast);

            System.out.println(ast.toString());
            System.out.println("Done parsing");
        }
        catch (Exception e)
        {
            System.err.println("Yuck, blew up in parse/validate phase");
            e.printStackTrace();
	        System.exit(1);
        }
    }

    void TypeCheckProgram(Program ast) throws Exception {

        ast.visit();

        if (checkForUndefined())
        {
            throw new Exception("Undefined class");
        }
        System.out.println("Passed check for undefined class inheritance");

        if (checkForCycles())
        {
            throw new Exception("Class cycles");
        }
        System.out.println("Passed check for class cycles");

        if (checkConstructor(ast))
        {
            throw new Exception("Bad constructor");
        }
        System.out.println("Passed check for subclass matching constructor of parent class");
    }

    boolean checkForUndefined()
    {
        HashMap<String, String> clazzTable = classesTable.getClassTable();
        for (String ident : clazzTable.values())
        {
            String currExtends = clazzTable.get(ident);
            if (currExtends == null)
            {
                return true;
            }
        }

        return false;
    }

    boolean checkForCycles()
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
                    return true;
                }
                currIdent = currExtends;
                currExtends = clazzTable.get(currIdent);
            }
        }
        return false;
    }

    boolean checkConstructor(Program ast)
    {
        List<Class_Block.Clazz_Block> class_blocks = ast.get_cbs();
        for (Class_Block.Clazz_Block class_block : class_blocks)
        {
            VarTable vt = class_block.getConstructor();
        }
        return false;
    }
}
