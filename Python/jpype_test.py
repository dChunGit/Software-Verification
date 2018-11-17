from jpype import *

startJVM(getDefaultJVMPath(), "-ea", "-Djava.class.path=/home/dchun/Code/alloy4.jar:/home/dchun/Code/Alloy/out/artifacts/Alloy_jar/Alloy.jar")

A4Options = JClass('edu.mit.csail.sdg.alloy4compiler.translator.A4Options')
AlloyWrapper = JClass('wrappers.AlloyWrapper')
Attr = JClass('edu.mit.csail.sdg.alloy4compiler.ast.Attr')
Func = JClass('edu.mit.csail.sdg.alloy4compiler.ast.Func')
Command = JClass('edu.mit.csail.sdg.alloy4compiler.ast.Command')
ExprConstant = JClass('edu.mit.csail.sdg.alloy4compiler.ast.ExprConstant')
TranslateAlloyToKodkod = JClass('edu.mit.csail.sdg.alloy4compiler.translator.TranslateAlloyToKodkod')

Util = JClass('edu.mit.csail.sdg.alloy4.Util')

List = JClass('java.util.ArrayList')
Arrays = JClass('java.util.Arrays')

Alloy = AlloyWrapper()
satsolver = Alloy.getSatSolver()

opt = A4Options()
opt.solver = satsolver
A = Alloy.getPrimSig("B", Attr.ABSTRACT)
B = Alloy.getSimplePrimSig("B")
A1 = Alloy.getParentPrimSig("A1", A, Attr.ONE)
A2 = Alloy.getParentPrimSig("A2", A, Attr.ONE)
f = A.addField("f", B.lone_arrow_lone(B))
g = A.addField("g", B)

someG = Func(None, "SomeG", None, None, g.some())

x = Alloy.getStandardPrimSig(1).oneOf("x")
y = Alloy.getStandardPrimSig(1).oneOf("y")
body = x.get().plus(y.get()).cardinality().lte(ExprConstant.makeNUMBER(3))
atMost3 = Func(None, "atMost3", Util.asList(x, y), None, body)

sigs = List()
sigs.add(A)
sigs.add(B)
sigs.add(A1)
sigs.add(A2)

expr1 = Alloy.getExpr(1, A.some(), atMost3.call(B, B))
cmd1 = Command(False, 3, 3, 3, expr1)
sol1 = TranslateAlloyToKodkod.execute_command(Alloy.getNOP(), sigs, cmd1, opt)
print("[Solution1]:")
print(sol1.toString())

shutdownJVM()
