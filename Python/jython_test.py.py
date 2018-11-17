import sys
sys.path.append("C:\\jython2.7.0\\bin\\Alloy.jar")

from java.io import File

from edu.mit.csail.sdg.alloy4 import A4Reporter
from edu.mit.csail.sdg.alloy4 import XMLNode
from edu.mit.csail.sdg.alloy4compiler.ast import *
from edu.mit.csail.sdg.alloy4compiler.parser import CompUtil
from edu.mit.csail.sdg.alloy4compiler.translator import A4Options
from edu.mit.csail.sdg.alloy4compiler.translator import A4Solution
from edu.mit.csail.sdg.alloy4compiler.translator import A4SolutionReader
from edu.mit.csail.sdg.alloy4compiler.translator import TranslateAlloyToKodkod

def EvaluatorExample():
	
	model = "sig Point {} \n" + "\n" + "run { #Point > 1 } for 3 but 3 Int"
	outputfilename = "/tmp/myissue.xml"
	rep = A4Reporter()
	tmpAls = CompUtil.flushModelToFile(model, None)
	world = CompUtil.parseEverything_fromString(rep, model)
	opt = A4Options()
	opt.originalFilename = tmpAls.getAbsolutePath()
	opt.solver = A4Options.SatSolver.SAT4J
	cmd = world.getAllCommands().get(0)
	sol = TranslateAlloyToKodkod.execute_command(rep, world.getAllReachableSigs(), cmd, opt)
	print sol.satisfiable()
	sol.writeXML(outputfilename)

	e = CompUtil.parseOneExpression_fromString(world, "univ")
	print sol.eval(e)
	e = CompUtil.parseOneExpression_fromString(world, "Point")
	print(sol.eval(e))

	xmlNode = XMLNode(File(outputfilename))
	alloySourceFilename = xmlNode.iterator().next().getAttribute("filename")
	ansWorld = CompUtil.parseEverything_fromFile(rep, None, alloySourceFilename)
	ans = A4SolutionReader.read(ansWorld.getAllReachableSigs(), xmlNode)
	
	e = CompUtil.parseOneExpression_fromString(ansWorld, "univ")
	print ans.eval(e)
	e = CompUtil.parseOneExpression_fromString(ansWorld, "Point")
	print ans.eval(e)

EvaluatorExample()

