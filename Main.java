
//Starter code from Cool 

import java_cup.runtime.ComplexSymbolFactory;
import java_cup.runtime.Symbol;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.Options;

import java.io.FileReader;

public class Main {
    
    // Command line options
    String sourceFile = ""; 

    // Internal state
    ErrorReport report; 

    boolean DebugMode = false; // True => parse in debug mode 


    static public void main(String args[])
    {
        Main q = new Main();
        q.go(args);

        // Symbol table tests
//        Expression e1 = Expression.intconst(4, 0, 1);
//        Expression e2 = Expression.stringLit("four", 0, 1);
//        Expression e3 = Expression.intconst(5, 0, 1);
//
//
//        SymbolTable.Var v1 = new SymbolTable.Var("x", e1.getType());
//        SymbolTable.Var v2 = new SymbolTable.Var("y", e2.getType());
//        SymbolTable.Var v3 = new SymbolTable.Var("x", e3.getType());
//        SymbolTable.Var v4 = new SymbolTable.Var("z", e3.getType());
//
//        SymbolTable.VarTable vt = new SymbolTable.VarTable();
//        vt.addVar(v1);
//        vt.addVar(v2);
//        vt.addVar(v3);
//        vt.removeVar(v4);
//        vt.removeVar(v3);
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
            Symbol result;
            ComplexSymbolFactory symbolFactory = new ComplexSymbolFactory();
            Lexer scanner = new Lexer (new FileReader( "built-ins.qk" ), symbolFactory);
                parser p = new parser( scanner, symbolFactory);
                result = p.parse();
            //ast of built in clasess
            Program builtinAST = (Program) result.value;
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

            Program ast = (Program) result.value;
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

}
