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
		function initialize(){
			«generateMain(p.prologue)»
		}
		
		initialize()
		
	'''
	
	
	def dispatch generateExpression(ComplexIntExpression expression)'''
		«IF expression.operation.equals("the love")»
			«generateExpression(expression.exp1)» + «generateExpression(expression.exp2)»
		«ELSE»
			«generateExpression(expression.exp1)» - «generateExpression(expression.exp2)»
		«ENDIF»
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
	
	def dispatch generateExpression(ActorExpression actor) '''
		«actor.actor.name»
	'''
	
	def generateMain(Prologue prologue)'''
		«FOR init: prologue.initials»
			«init.actor.name» = «generateExpression(init.expression)»
		«ENDFOR»
	'''
	
}
