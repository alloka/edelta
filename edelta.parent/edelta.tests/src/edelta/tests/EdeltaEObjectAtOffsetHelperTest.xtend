package edelta.tests

import com.google.inject.Inject
import edelta.edelta.EdeltaProgram
import org.eclipse.emf.ecore.EObject
import org.eclipse.xtext.resource.EObjectAtOffsetHelper
import org.eclipse.xtext.resource.XtextResource
import org.eclipse.xtext.testing.InjectWith
import org.eclipse.xtext.testing.XtextRunner
import org.eclipse.xtext.util.TextRegion
import org.junit.Test
import org.junit.runner.RunWith

import static extension org.junit.Assert.*

@RunWith(XtextRunner)
@InjectWith(EdeltaInjectorProviderCustom)
class EdeltaEObjectAtOffsetHelperTest extends EdeltaAbstractTest {

	@Inject extension EObjectAtOffsetHelper

	@Test def void testWithoutManipulationExpressions() {
		'''
		metamodel "foo"
		
		ecoreref(FooClass)
		'''.resolveAtOffset[
			it, linked |
			metamodels.head.getEClassifier("FooClass").
				assertSame(linked)
		]
	}

	@Test def void testWithManipulationExpressions() {
		'''
		metamodel "foo"
		
		createEClass NewClass in foo {}
		
		ecoreref(FooClass)
		'''.resolveAtOffset[
			it, linked |
			metamodels.head.getEClassifier("FooClass").
				assertSame(linked)
		]
	}

	@Test def void testUnresolved() {
		'''
		metamodel "foo"
		
		ecoreref(NonExistant)
		'''.resolveAtOffset[
			it, linked |
			linked.eIsProxy.assertTrue
		]
	}

	@Test def void testNonEcoreReference() {
		'''
		metamodel "foo"
		
		val String s = null
		'''.resolveAtOffset("Str")[
			it, linked |
			lastExpression.variableDeclaration.
				type.type.eIsProxy.assertFalse
		]
	}

	def private resolveAtOffset(CharSequence input, (EdeltaProgram, EObject)=>void tester) {
		resolveAtOffset(input, "ecoreref(", tester)
	}

	def private resolveAtOffset(CharSequence input, String stringRegion, (EdeltaProgram, EObject)=>void tester) {
		val prog = input.parseWithTestEcore
		val offset = input.toString.lastIndexOf(stringRegion) + stringRegion.length + 1
		val crossRefNode = (prog.
			eResource as XtextResource).
			getCrossReferenceNode(new TextRegion(offset, 0));
		val crossLinkedEObject = crossRefNode.
			crossReferencedElement
		tester.apply(prog, crossLinkedEObject)
	}
}
