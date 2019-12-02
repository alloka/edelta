/*
 * generated by Xtext 2.10.0
 */
package edelta.ui.outline

import com.google.inject.Inject
import edelta.edelta.EdeltaMain
import edelta.edelta.EdeltaOperation
import edelta.edelta.EdeltaProgram
import edelta.services.IEdeltaEcoreModelAssociations
import org.eclipse.xtext.ui.editor.outline.IOutlineNode
import org.eclipse.xtext.ui.editor.outline.impl.DefaultOutlineTreeProvider
import edelta.edelta.EdeltaModifyEcoreOperation

/**
 * Customization of the default outline structure.
 * 
 * See https://www.eclipse.org/Xtext/documentation/304_ide_concepts.html#outline
 */
class EdeltaOutlineTreeProvider extends DefaultOutlineTreeProvider {

	@Inject extension IEdeltaEcoreModelAssociations

	def protected void _createChildren(IOutlineNode parentNode, EdeltaProgram p) {
		for (o : p.operations) {
			createNode(parentNode, o)
		}
		for (o : p.modifyEcoreOperations) {
			createNode(parentNode, o)
		}
		createNode(parentNode, p.main)
		for (derived : p.eResource.copiedEPackages) {
			// the cool thing is that we don't need to provide
			// customization in the label provider for EPackage and EClass
			// since Xtext defaults to the .edit plugin :)
			createNode(parentNode, derived)
		}
	}

	def _isLeaf(EdeltaOperation m) {
		true
	}

	def _isLeaf(EdeltaModifyEcoreOperation m) {
		true
	}

	def _isLeaf(EdeltaMain m) {
		true
	}
}
