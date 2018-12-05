import timing
from py4j.java_gateway import JavaGateway
gateway = JavaGateway()
from py4j.java_gateway import java_import
java_import(gateway.jvm,'java.util.*')
java_import(gateway.jvm,'edu.mit.csail.sdg.alloy4compiler.translator.*')
java_import(gateway.jvm,'edu.mit.csail.sdg.alloy4compiler.ast.Attr')
java_import(gateway.jvm,'edu.mit.csail.sdg.alloy4compiler.ast.Func')
java_import(gateway.jvm,'edu.mit.csail.sdg.alloy4compiler.ast.Command')
java_import(gateway.jvm,'edu.mit.csail.sdg.alloy4compiler.ast.ExprConstant')
# java_import(gateway.jvm,'edu.mit.csail.sdg.alloy4.Util')
wrapper = gateway.entry_point

A4Options = gateway.jvm.A4Options
Attr = gateway.jvm.Attr 
Func = gateway.jvm.Func
Command = gateway.jvm.Command
ExprConstant = gateway.jvm.ExprConstant
TranslateAlloyToKodkod = gateway.jvm.TranslateAlloyToKodkod
ArrayList = gateway.jvm.ArrayList
Array = gateway.jvm.Arrays
timing.log("Start Execution")

satsolver = wrapper.getSatSolver()
opt = wrapper.getA4Options()
opt.solver = satsolver
A = wrapper.getPrimSig("A", Attr.ABSTRACT)
B = wrapper.getSimplePrimSig("B")
A1 = wrapper.getParentPrimSig("A1", A, Attr.ONE)
A2 = wrapper.getParentPrimSig("A2", A, Attr.ONE)
f = A.addField("f", B.lone_arrow_lone(B))
g = A.addField("g", B)

someG = Func(None, "SomeG", None, None, g.some())

x = wrapper.getStandardPrimSig(1).oneOf("x")
y = wrapper.getStandardPrimSig(1).oneOf("y")
body = x.get().plus(y.get()).cardinality().lte(ExprConstant.makeNUMBER(3))
atMost3 = Func(None, "atMost3", wrapper.getAsList(x, y), None, body)

sigs = ArrayList()
sigs.add(A)
sigs.add(B)
sigs.add(A1)
sigs.add(A2)

expr1 = wrapper.getExpr(1, A.some(), wrapper.getPrimSigCall(atMost3, B, B))
cmd1 = Command(False, 3, 3, 3, expr1)
sol1 = TranslateAlloyToKodkod.execute_command(wrapper.getNOP(), sigs, cmd1, opt)
print("[Solution1]:")
print(sol1.toString())
