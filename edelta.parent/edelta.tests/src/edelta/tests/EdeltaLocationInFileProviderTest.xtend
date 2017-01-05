/*
 * generated by Xtext 2.10.0
 */
package edelta.tests

import com.google.inject.Inject
import edelta.edelta.EdeltaEcoreCreateEClassExpression
import org.eclipse.xtext.junit4.InjectWith
import org.eclipse.xtext.junit4.XtextRunner
import org.eclipse.xtext.resource.ILocationInFileProvider
import org.junit.Test
import org.junit.runner.RunWith

import static org.junit.Assert.*

@RunWith(XtextRunner)
@InjectWith(EdeltaInjectorProviderCustom)
class EdeltaLocationInFileProviderTest extends EdeltaAbstractTest {

	@Inject extension ILocationInFileProvider

	@Test
	def void testDerivedEClass() {
		val input = '''
		package test
		
		metamodel "foo"
		
		createEClass First in foo
		'''
		val program = input.parseWithTestEcore
		val e = program.lastExpression as EdeltaEcoreCreateEClassExpression
		val derived = program.getDerivedStateLastEClass
		val originalTextRegion = getSignificantTextRegion(e)
		val derivedTextRegion = getSignificantTextRegion(derived)
		// the derived EClass is mapped to the original creation expression
		assertEquals(originalTextRegion, derivedTextRegion)
	}

	@Test
	def void testDerivedAttribute() {
		val input = '''
		package test
		
		metamodel "foo"
		
		createEClass First in foo {
			createEAttribute newAttribute
		}
		'''
		val program = input.parseWithTestEcore
		val e = (program.lastExpression as EdeltaEcoreCreateEClassExpression).
			body.expressions.last
		val derived = program.getDerivedStateLastEClass.
			EStructuralFeatures.last
		val originalTextRegion = getSignificantTextRegion(e)
		val derivedTextRegion = getSignificantTextRegion(derived)
		// the derived EAttribute is mapped to the original creation expression
		assertEquals(originalTextRegion, derivedTextRegion)
	}
}
