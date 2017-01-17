/*
 * generated by Xtext 2.10.0
 */
package edelta.tests

import com.google.inject.Inject
import com.google.inject.Provider
import edelta.edelta.EdeltaEcoreCreateEAttributeExpression
import edelta.edelta.EdeltaEcoreCreateEClassExpression
import edelta.edelta.EdeltaProgram
import edelta.tests.input.Inputs
import org.eclipse.emf.common.util.URI
import org.eclipse.emf.ecore.EClass
import org.eclipse.emf.ecore.EObject
import org.eclipse.emf.ecore.EPackage
import org.eclipse.emf.ecore.EcoreFactory
import org.eclipse.emf.ecore.resource.ResourceSet
import org.eclipse.xtext.junit4.InjectWith
import org.eclipse.xtext.junit4.XtextRunner
import org.eclipse.xtext.junit4.util.ParseHelper
import org.eclipse.xtext.junit4.validation.ValidationTestHelper
import org.eclipse.xtext.resource.XtextResourceSet
import org.eclipse.xtext.xbase.XExpression
import org.junit.runner.RunWith

@RunWith(XtextRunner)
@InjectWith(EdeltaInjectorProvider)
abstract class EdeltaAbstractTest {

	@Inject
	Provider<XtextResourceSet> resourceSetProvider

	@Inject protected extension ParseHelper<EdeltaProgram>
	@Inject protected extension ValidationTestHelper
	protected extension Inputs = new Inputs

	def protected parseWithTestEcore(CharSequence input) {
		input.parse(resourceSetWithTestEcore)
	}

	def protected parseWithTestEcores(CharSequence input) {
		input.parse(resourceSetWithTestEcores)
	}

	def protected resourceSetWithTestEcore() {
		val resourceSet = resourceSetProvider.get
		addEPackageForTests(resourceSet)
	}

	def protected addEPackageForTests(ResourceSet resourceSet) {
		val resource = resourceSet.createResource(URI.createURI("foo.ecore"))
		resource.contents += EPackageForTests
		resourceSet
	}

	def protected resourceSetWithTestEcores() {
		val resourceSet = resourceSetWithTestEcore
		addEPackageForTests2(resourceSet)
	}

	def protected addEPackageForTests2(ResourceSet resourceSet) {
		val resource = resourceSet.createResource(URI.createURI("bar.ecore"))
		resource.contents += EPackageForTests2
		resourceSet
	}

	def protected EPackageForTests() {
		val fooPackage = EcoreFactory.eINSTANCE.createEPackage => [
			name = "foo"
			nsPrefix = "foo"
			nsURI = "http://foo"
		]
		fooPackage.EClassifiers += EcoreFactory.eINSTANCE.createEClass => [
			name = "FooClass"
			EStructuralFeatures += EcoreFactory.eINSTANCE.createEAttribute => [
				name = "myAttribute"
			]
			EStructuralFeatures += EcoreFactory.eINSTANCE.createEReference => [
				name = "myReference"
			]
		]
		fooPackage.EClassifiers += EcoreFactory.eINSTANCE.createEDataType => [
			name = "FooDataType"
		]
		fooPackage
	}

	def protected EPackageForTests2() {
		val fooPackage = EcoreFactory.eINSTANCE.createEPackage => [
			name = "bar"
			nsPrefix = "bar"
			nsURI = "http://bar"
		]
		fooPackage.EClassifiers += EcoreFactory.eINSTANCE.createEClass => [
			name = "BarClass"
			EStructuralFeatures += EcoreFactory.eINSTANCE.createEAttribute => [
				name = "myAttribute"
			]
			EStructuralFeatures += EcoreFactory.eINSTANCE.createEReference => [
				name = "myReference"
			]
		]
		fooPackage.EClassifiers += EcoreFactory.eINSTANCE.createEDataType => [
			name = "BarDataType"
		]
		fooPackage
	}

	def protected lastExpression(EdeltaProgram p) {
		p.main.expressions.last
	}

	def protected getCreateEClassExpression(XExpression e) {
		e as EdeltaEcoreCreateEClassExpression
	}

	def protected getCreateEAttributExpression(XExpression e) {
		e as EdeltaEcoreCreateEAttributeExpression
	}

	def protected getDerivedStateLastEClass(EObject context) {
		val derivedEPackage = context.eResource.contents.last as EPackage
		derivedEPackage.EClassifiers.last as EClass
	}
}
