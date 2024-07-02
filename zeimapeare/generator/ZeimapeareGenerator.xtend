/*
 * generated by Xtext 2.35.0
 */
package zeimapeare.generator

import org.eclipse.emf.ecore.resource.Resource
import org.eclipse.xtext.generator.AbstractGenerator
import org.eclipse.xtext.generator.IFileSystemAccess2
import org.eclipse.xtext.generator.IGeneratorContext
import zeimapeare.zeimapeare.Program
import zeimapeare.zeimapeare.Prologue
import zeimapeare.zeimapeare.ActorDeclaration
import zeimapeare.zeimapeare.Expression
import zeimapeare.zeimapeare.ComplexIntExpression
import zeimapeare.zeimapeare.ActorExpression
import zeimapeare.zeimapeare.Number
import zeimapeare.zeimapeare.ValueSustantive
import zeimapeare.zeimapeare.Assigment
import zeimapeare.zeimapeare.Output
import zeimapeare.zeimapeare.Act
import zeimapeare.zeimapeare.Scene
import zeimapeare.zeimapeare.If
import zeimapeare.zeimapeare.ActorScene
import zeimapeare.zeimapeare.ComplexStringExpression
import zeimapeare.zeimapeare.ValueString
import zeimapeare.zeimapeare.Condition
import zeimapeare.zeimapeare.SceneCall
import zeimapeare.zeimapeare.ParameterSceneCall

/**
 * Generates code from your model files on save.
 * 
 * See https://www.eclipse.org/Xtext/documentation/303_runtime_concepts.html#code-generation
 */
class ZeimapeareGenerator extends AbstractGenerator {

	override void doGenerate(Resource resource, IFileSystemAccess2 fsa, IGeneratorContext context) {
		val p = resource.allContents.head as Program;
		fsa.generateFile(p.title + '.js', generateProgram(p))
	}
	
	def generateProgram(Program p)'''
		
		«initializeActors(p.prologue)»
		
		
		«FOR act: p.acts»
			«generateSceneFromAct(act)»
		«ENDFOR»
		
		«generateCallFunction(p.prologue.main)»
		
	'''
	
	def generateSceneFromAct(Act act)'''
		«FOR scene: act.scene»
			«generateFunctionFromScene(scene)»
		«ENDFOR»
	'''
	
	def generateParams(ActorScene actors)'''
		«FOR actor: actors.actorsExtra»
			«actor.name», 
		«ENDFOR»
	'''
	
	def generateFunctionFromScene(Scene scene)'''
		function scene«scene.romanNumber.romanNumber»«scene.name»(«generateParams(scene.actorScene)»){
			«FOR instruction: scene.instructions»
				 «generateInstruction(instruction)»
			«ENDFOR»
		}
	'''
	
	def dispatch generateExpression(ComplexIntExpression expression)'''
		«IF expression.operation.equals("the love")»
			«generateExpression(expression.exp1)» + «generateExpression(expression.exp2)»
		«ELSEIF expression.operation.equals("the hostility")»
			«generateExpression(expression.exp1)» - «generateExpression(expression.exp2)»
		«ELSEIF expression.operation.equals("the dismemberment")»
			«generateExpression(expression.exp1)» / «generateExpression(expression.exp2)»
		«ELSEIF expression.operation.equals("the leftovers")»
			«generateExpression(expression.exp1)» % «generateExpression(expression.exp2)»
		«ELSEIF expression.operation.equals("M-m-maybe next time...")»
			«generateExpression(expression.exp1)» && «generateExpression(expression.exp2)»
		«ELSEIF expression.operation.equals("or")»
			«generateExpression(expression.exp1)» || «generateExpression(expression.exp2)»
		«ELSEIF expression.operation.equals("inmensity")»
				«generateExpression(expression.exp1)».lengt
		«ENDIF»
	'''
	
	def dispatch generateExpression(ComplexStringExpression expr)'''
		«generateExpression(expr.exp1)» + «generateExpression(expr.exp2)»
	'''	
	
	def dispatch generateExpression(Number number)'''
		«number.number»
	'''

	def calculateValue(ValueSustantive valueSustantive){
		if(valueSustantive.sustantive.sustantive.equals("nothing"))
			return 0
		
		val numAdj = valueSustantive.adj?.adjectives?.size()
		return (1<<numAdj)
	}

	def dispatch generateExpression(ValueSustantive valueSustantive)'''
		«calculateValue(valueSustantive)»
	'''
	
	def dispatch generateExpression(ValueString value)'''
		"«value.value»"
	'''
	
	def dispatch generateExpression(ActorExpression actor) '''
		«actor.actor.name»
	'''
	def dispatch generateExpression(Expression exp) '''
		«generateExpression(exp)»
	'''
	
	def dispatch generateInstruction(Assigment assigment)'''
		«assigment.actor.name» = «generateExpression(assigment.expression)»
	'''
	
	def dispatch generateInstruction(Output output) '''
		console.log(«output.actor.name»)
	'''
	
	def generateComparator(String comp)'''
		«IF comp.equals("better")» >
		«ELSE» <
		«ENDIF»
	'''
	
	def generateCondition(Condition condition)'''
		«generateExpression(condition.exp1)» «generateComparator(condition.comp)»«generateExpression(condition.exp2)»
	'''
	
	def dispatch generateInstruction(If ifInst)'''
		if(«generateCondition(ifInst.condition)»){
			«FOR inst: ifInst.instructions»
				«generateInstruction(inst)»
			«ENDFOR»
		} else{
			«FOR inst: ifInst.^else.instructions»
				«generateInstruction(inst)»
			«ENDFOR»
		}
	'''
	
	def generateArguments(ParameterSceneCall args)'''
		«IF args!=null»
			«FOR extra: args.actorsExtra»
				«extra.name»,
			«ENDFOR»
		«ENDIF»
	'''
	
	def generateCallFunction(SceneCall call)'''
		scene«call.sceneCall.romanNumber.romanNumber»«call.sceneCall.name»(«generateArguments(call.parameterSceneCall)»)
	'''
	
	def dispatch generateInstruction(SceneCall call)'''
		«generateCallFunction(call)»
	'''
	
	def initializeActors(Prologue prologue)'''
		«FOR init: prologue.initials»
			«init.actor.name» = «generateExpression(init.expression)»
		«ENDFOR»
	'''
	
}
