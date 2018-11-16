import edu.mit.csail.sdg.alloy4.Err;
import edu.mit.csail.sdg.alloy4.Util;
import edu.mit.csail.sdg.alloy4compiler.ast.*;
import edu.mit.csail.sdg.alloy4compiler.translator.A4Options;
import edu.mit.csail.sdg.alloy4compiler.translator.A4Solution;
import edu.mit.csail.sdg.alloy4compiler.translator.TranslateAlloyToKodkod;

import java.util.Arrays;
import java.util.List;

import static edu.mit.csail.sdg.alloy4.A4Reporter.NOP;
import static edu.mit.csail.sdg.alloy4compiler.ast.Sig.UNIV;

public class AlloyMain {
    public static void main(String[] args) throws Err {

        // Chooses the Alloy4 options
        A4Options opt = new A4Options();
        opt.solver = A4Options.SatSolver.SAT4J;

        // abstract sig A {}
        Sig.PrimSig A = new Sig.PrimSig("A", Attr.ABSTRACT);

        // sig B {}
        Sig.PrimSig B = new Sig.PrimSig("B");

        // one sig A1 extends A {}
        Sig.PrimSig A1 = new Sig.PrimSig("A1", A, Attr.ONE);

        // one sig A2 extends A {}
        Sig.PrimSig A2 = new Sig.PrimSig("A2", A, Attr.ONE);

        // A { f: B lone->lone B }
        Expr f = A.addField("f", B.lone_arrow_lone(B));
        // Since (B lone->lone B) is not unary,  the default is "setOf",  meaning "f:set (B lone->lone B)"

        // A { g: B }
        Expr g = A.addField("g", B);
        // The line above is the same as:   A.addField(null, "g", B.oneOf())  since B is unary.
        // If you want "setOf", you need:   A.addField(null, "g", B.setOf())

        // pred someG { some g }
        Func someG = new Func(null, "SomeG", null, null, g.some());

        // pred atMostThree[x:univ, y:univ] { #(x+y) >= 3 }
        Decl x = UNIV.oneOf("x");
        Decl y = UNIV.oneOf("y");
        Expr body = x.get().plus(y.get()).cardinality().lte(ExprConstant.makeNUMBER(3));
        Func atMost3 = new Func(null, "atMost3", Util.asList(x,y), null, body);

        List<Sig> sigs = Arrays.asList(new Sig[]{A, B, A1, A2});

        // run { some A && atMostThree[B,B] } for 3 but 3 int, 3 seq
        Expr expr1 = A.some().and(atMost3.call(B,B));
        Command cmd1 = new Command(false, 3, 3, 3, expr1);
        A4Solution sol1 = TranslateAlloyToKodkod.execute_command(NOP, sigs, cmd1, opt);
        System.out.println("[Solution1]:");
        System.out.println(sol1.toString());

        // run { some f && SomeG[] } for 3 but 2 int, 1 seq, 5 A, exactly 6 B
        Expr expr2 = f.some().and(someG.call());
        Command cmd2 = new Command(false, 3, 2, 1, expr2);
        cmd2 = cmd2.change(A, false, 1);
        cmd2 = cmd2.change(B, true, 1);
        A4Solution sol2 = TranslateAlloyToKodkod.execute_command(NOP, sigs, cmd2, opt);

        while (sol2.satisfiable()) {
            System.out.println("[Solution2]:");
            System.out.println(sol2.toString());
            sol2 = sol2.next();
        }
    }

}
