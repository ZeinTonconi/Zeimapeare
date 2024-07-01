/*
 * generated by Xtext 2.35.0
 */
package zeimapeare.validation;

import org.eclipse.xtext.validation.Check;

import zeimapeare.zeimapeare.ActorDeclaration;
import zeimapeare.zeimapeare.ActorExpression;
import zeimapeare.zeimapeare.Assigment;
import zeimapeare.zeimapeare.ComplexIntExpression;
import zeimapeare.zeimapeare.Expression;
import zeimapeare.zeimapeare.IntExpression;
import zeimapeare.zeimapeare.SceneCall;
import zeimapeare.zeimapeare.Value;
import zeimapeare.zeimapeare.ZeimapearePackage;

/**
 * This class contains custom validation rules. 
 *
 * See https://www.eclipse.org/Xtext/documentation/303_runtime_concepts.html#validation
 */
public class ZeimapeareValidator extends AbstractZeimapeareValidator {
	
//	public static final String INVALID_NAME = "invalidName";
//
//	@Check
//	public void checkGreetingStartsWithCapital(Greeting greeting) {
//		if (!Character.isUpperCase(greeting.getName().charAt(0))) {
//			warning("Name should start with a capital",
//					ZeimapearePackage.Literals.GREETING__NAME,
//					INVALID_NAME);
//		}
//	}
//	@Check
//	public void checkActorInIntExpression(IntExpression intExpr) {
//		if(intExpr instanceof ActorExpression) {
//			ActorExpression actor = (ActorExpression) intExpr;
//			if(actor.getActor().getDatatype().equals("Capulet"))
//				error("Wrong Family",
//						ZeimapearePackage.Literals.ACTOR_DECLARATION__DATATYPE,
//						"Mismatch family");
//		}
//	}
	
	public String findFamily(Value v) {
		return "Montague";
	}
	
	public String findFamily(ActorExpression actor) {
		return actor.getActor().getDatatype();
	}
	
	public String findFamily(ComplexIntExpression complex) {
		
		String family1=findFamily(complex.getExp1());
		String family2=findFamily(complex.getExp2());
	
		if(family1.equals(family2))
			return family1;
		return "No family";
	}
	
	public String findFamily(IntExpression intExpr) {
		if(intExpr instanceof ComplexIntExpression)
			return findFamily((ComplexIntExpression)intExpr);
		if(intExpr instanceof Value)
			return findFamily((Value)intExpr);
		if(intExpr instanceof ActorExpression)
			return findFamily((ActorExpression) intExpr);
		return "No family";
	}
	
	public String findFamily(Expression expr) {
		if(expr instanceof IntExpression && findFamily((IntExpression) expr).equals("Montague")) {
			return "Montague";
		}
			
		return "No family";
	}
	
	
	
	@Check
	public void checkAssignmentDataTypeActor(Assigment assignment) {
		
		if(!assignment.getActor().getDatatype().equals(findFamily(assignment.getExpression())))
			error("Wrong Family", assignment, ZeimapearePackage.Literals.ASSIGMENT__EXPRESSION);
			
	}
	
	@Check
	public void checkParametersOfCallScene(SceneCall sceneCall) {
		if(sceneCall.getActorScene().getActors() && sceneCall.getActorScene().getActorsExtra() != sceneCall.get )
	}

}
