/**
 * 
 */
package edelta.interpreter;

import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.xtext.common.types.JvmGenericType;
import org.eclipse.xtext.xbase.interpreter.IEvaluationResult;

import edelta.edelta.EdeltaEcoreBaseEClassManipulationWithBlockExpression;

/**
 * @author Lorenzo Bettini
 *
 */
public interface IEdeltaInterpreter {

	IEvaluationResult run(EdeltaEcoreBaseEClassManipulationWithBlockExpression exp, EClass eClass,
			JvmGenericType jvmGenericType, List<EPackage> ePackages);

	void setInterpreterTimeout(int interpreterTimeout);

	public void setClassLoader(ClassLoader classLoader);
}
