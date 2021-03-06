/*
 * generated by Xtext 2.21.0
 */
package edelta.ui.contentassist;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.Assignment;
import org.eclipse.xtext.CrossReference;
import org.eclipse.xtext.resource.IEObjectDescription;
import org.eclipse.xtext.ui.editor.contentassist.ContentAssistContext;
import org.eclipse.xtext.ui.editor.contentassist.ICompletionProposalAcceptor;

/**
 * See
 * https://www.eclipse.org/Xtext/documentation/310_eclipse_support.html#content-assist
 * on how to customize the content assistant.
 * 
 * @author Lorenzo Bettini
 */
public class EdeltaProposalProvider extends AbstractEdeltaProposalProvider {
	/**
	 * Avoids proposing subpackages since in Edelta they are not allowed
	 * to be directly imported.
	 */
	@Override
	public void completeEdeltaProgram_Metamodels(EObject model, Assignment assignment, ContentAssistContext context,
			ICompletionProposalAcceptor acceptor) {
		lookupCrossReference(
			((CrossReference) assignment.getTerminal()),
			context,
			acceptor,
			// EPackage are not loaded at this point, so we cannot rely
			// on super package relation.
			// Instead we rely on the fact that subpackages have segments
			(IEObjectDescription desc) ->
				desc.getQualifiedName().getSegmentCount() == 1
		);
	}
}
