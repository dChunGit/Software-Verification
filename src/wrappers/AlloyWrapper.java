package wrappers;
import edu.mit.csail.sdg.alloy4.ConstList;
import edu.mit.csail.sdg.alloy4.Util;
import py4j.GatewayServer;

import edu.mit.csail.sdg.alloy4.A4Reporter;
import edu.mit.csail.sdg.alloy4.Err;
import edu.mit.csail.sdg.alloy4compiler.ast.*;
import edu.mit.csail.sdg.alloy4compiler.translator.A4Options;

import java.lang.management.ManagementFactory;
import java.util.Collection;


public class AlloyWrapper {
    public static void main(String[] args) {
        float currentTime = System.currentTimeMillis();
        float startup = currentTime - ManagementFactory.getRuntimeMXBean().getStartTime();
        System.out.println(startup);
        AlloyWrapper app = new AlloyWrapper();
        // app is now the gateway.entry_point
        GatewayServer server = new GatewayServer(app);
        server.start();
    }

    public A4Reporter getNOP() {
        return A4Reporter.NOP;
    }

    public A4Options getA4Options() {
        return new A4Options();
    }

    public Expr getPrimSigCall(Func func, Sig.PrimSig a, Sig.PrimSig b) {
        return func.call(a, b);
    }

    public ConstList<Decl> getAsList(Decl x, Decl y) {
        return Util.asList(x, y);
    }

    public Sig.PrimSig getPrimSig(String label, Attr val) throws Err {
        return new Sig.PrimSig(label, val);
    }

    public Sig.SubsetSig getSubSetSig(String label, Collection<Sig> parents, Attr... attributes) throws Err {
        return new Sig.SubsetSig(label, parents, attributes);
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

    public Attr.AttrType getAttr(int type) {
        switch (type) {
            case 1: {
                return Attr.AttrType.ABSTRACT;
            }
            case 2: {
                return Attr.AttrType.ONE;
            }
            case 3: {
                return Attr.AttrType.LONE;
            }
            default: return null;
        }
    }
}
