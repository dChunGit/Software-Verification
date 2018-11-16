package wrappers;

import edu.mit.csail.sdg.alloy4.A4Reporter;
import edu.mit.csail.sdg.alloy4.Err;
import edu.mit.csail.sdg.alloy4compiler.ast.*;
import edu.mit.csail.sdg.alloy4compiler.translator.A4Options;


public class AlloyWrapper {

    public A4Reporter getNOP() {
        return A4Reporter.NOP;
    }

    public Sig.PrimSig getPrimSig(String label, Attr val) throws Err {
        return new Sig.PrimSig(label, val);
    }

    public Sig.PrimSig getSimplePrimSig(String label) throws Err {
        return new Sig.PrimSig(label);
    }

    public Sig.PrimSig getParentPrimSig(String label, Sig.PrimSig parent, Attr val) throws Err {
        return new Sig.PrimSig(label, parent, val);
    }

    public A4Options.SatSolver getSatSolver() {
        return A4Options.SatSolver.SAT4J;
    }

    public Sig.PrimSig getStandardPrimSig(int type) {
        switch (type) {
            case 1: {
                return Sig.UNIV;
            }
            default: {
                return null;
            }
        }
    }

    public Expr getExpr(int type, Expr mine, Expr op) {
        switch (type) {
            case 1: {
                return mine.and(op);
            }
            default: {
                return null;
            }
        }
    }
}
