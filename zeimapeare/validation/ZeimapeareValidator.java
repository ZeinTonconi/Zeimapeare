/*
 * generated by Xtext 2.35.0
 */
package zeimapeare.validation;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.xtext.validation.Check;

import zeimapeare.zeimapeare.ActorDeclaration;
import zeimapeare.zeimapeare.ActorExpression;
import zeimapeare.zeimapeare.ActorParameter;
import zeimapeare.zeimapeare.ActorSceneCall;
import zeimapeare.zeimapeare.Assigment;
import zeimapeare.zeimapeare.ComplexCondition;
import zeimapeare.zeimapeare.ComplexIntExpression;
import zeimapeare.zeimapeare.ComplexStringExpression;
import zeimapeare.zeimapeare.Expression;
import zeimapeare.zeimapeare.If;
import zeimapeare.zeimapeare.Instructions;
import zeimapeare.zeimapeare.IntExpression;
import zeimapeare.zeimapeare.Output;
import zeimapeare.zeimapeare.Scene;
import zeimapeare.zeimapeare.SceneCall;
import zeimapeare.zeimapeare.StringExpression;
import zeimapeare.zeimapeare.Value;
import zeimapeare.zeimapeare.ValueString;
import zeimapeare.zeimapeare.ZeimapearePackage;

/**
 * This class contains custom validation rules. 
 *
 * See https://www.eclipse.org/Xtext/documentation/303_runtime_concepts.html#validation
 */
public class ZeimapeareValidator extends AbstractZeimapeareValidator {
	
	public String findFamily(ComplexStringExpression complex) {
		String family1=findFamily(complex.getExp1());
		String family2=findFamily(complex.getExp2());
	
		if(family1.equals(family2))
			return family1;
		return "No family";
	}
	
	public String findFamily(ValueString value) {
		return "Capulet";
	}
	
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
	
	public String findFamily(StringExpression stringExpr) {
		if(stringExpr instanceof ComplexStringExpression)
			return findFamily((ComplexStringExpression)stringExpr);
		if(stringExpr instanceof ValueString)
			return findFamily((ValueString)stringExpr);
		if(stringExpr instanceof ActorExpression)
			return findFamily((ActorExpression) stringExpr);
		return "No family";
	}
	
	public String findFamily(Expression expr) {

		if(expr instanceof IntExpression && findFamily((IntExpression) expr).equals("Montague")) {
			return "Montague";
		}
		if(expr instanceof StringExpression && findFamily((StringExpression) expr).equals("Capulet"))
			return "Capulet";
		
		if(expr instanceof ActorExpression)
			return findFamily((ActorExpression)expr);

		return "No family";
	}
	
	
	@Check
	public void checkAssignmentDataTypeActor(Assigment assignment) {
		
		if(!assignment.getActor().getDatatype().equals(findFamily(assignment.getExpression())))
			error("Wrong Family", assignment, ZeimapearePackage.Literals.ASSIGMENT__EXPRESSION);
			
	}

	public boolean checkCondition(ComplexCondition condition) {
		String family = findFamily(condition.getCond1().getExp1());
		if(!family.equals(findFamily(condition.getCond1().getExp2())))
			return false;
		if(condition.getCond2() == null)
			return true;
		else
			return checkCondition(condition.getCond2());
	}
	
	@Check
	public void checkIfExpression(If ifExpr) {
		if(!checkCondition(ifExpr.getCondition())){
			error("Wrong Family condition",ifExpr, ZeimapearePackage.Literals.IF__CONDITION);
		}
	}
	
	@Check
	public void checkParametersOfCallScene(SceneCall sceneCall) {
		Scene scene = sceneCall.getSceneCall();
		int sizeOfActorSceneCall = 0;
		if(sceneCall.getParameterSceneCall() != null) {
			sizeOfActorSceneCall = sceneCall.getParameterSceneCall().getActorsExtra().size();
		}
		
		if(sizeOfActorSceneCall > scene.getActorScene().getActorsExtra().size())
			error("There are impostors on the scene", sceneCall, ZeimapearePackage.Literals.SCENE_CALL__PARAMETER_SCENE_CALL);
		
		if(sizeOfActorSceneCall < scene.getActorScene().getActorsExtra().size())
			error("You killed actors without God's permission", sceneCall, ZeimapearePackage.Literals.SCENE_CALL__PARAMETER_SCENE_CALL);
	}
	
	@Check
	public void checkTypeOfParametersSceneCall(ActorSceneCall actorSceneCall) {
		List<ActorDeclaration> arguments = actorSceneCall.getParameterSceneCall().getActorsExtra();
		List<ActorDeclaration> parameters = actorSceneCall.getSceneCall().getActorScene().getActorsExtra();
		for(int i=0;i<arguments.size();i++) {
			if(!arguments.get(i).getDatatype().equals(parameters.get(i).getDatatype()))
				error("The love between these actor is forbidden", actorSceneCall, ZeimapearePackage.Literals.ACTOR_SCENE_CALL__PARAMETER_SCENE_CALL);
		}
		
	}
	
	public boolean findActorOnStage(ActorDeclaration actor, List<String> actorsOnStage) {
		for(String actorOnStage: actorsOnStage) {
			if(actor.getName().equals(actorOnStage))
				return true;
		}
		return false;
	}
	
	
	@Check
	public void checkActorOnStage(Scene scene) {
	
		List<String> actorsOnStage =  new ArrayList<>();
		for(ActorParameter actor: scene.getActorScene().getActors()) {
			actorsOnStage.add(actor.getName().getName());
		}
		for(ActorDeclaration actor: scene.getActorScene().getActorsExtra()) {
			actorsOnStage.add(actor.getName());
		}
		
		for(Instructions instruction: scene.getInstructions()) {
			if(instruction instanceof Assigment) {
				ActorDeclaration actor = ((Assigment)instruction).getActor();
				if(!findActorOnStage(actor, actorsOnStage))
					error("The actor is on vacations", instruction, ZeimapearePackage.Literals.ASSIGMENT__ACTOR);
			}
			
			if(instruction instanceof ActorSceneCall) {
				ActorDeclaration actor = ((ActorSceneCall)instruction).getActor();
				if(!findActorOnStage(actor, actorsOnStage))
					error("The actor is on vacations", instruction, ZeimapearePackage.Literals.ASSIGMENT__ACTOR);
			}
			if(instruction instanceof Output) {
				ActorDeclaration actor = ((Output)instruction).getActor();
				if(!findActorOnStage(actor, actorsOnStage))
					error("The actor is on vacations", instruction, ZeimapearePackage.Literals.ASSIGMENT__ACTOR);
			}
		}
	}
	
}
