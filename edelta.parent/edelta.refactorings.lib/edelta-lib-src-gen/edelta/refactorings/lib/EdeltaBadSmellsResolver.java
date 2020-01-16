package edelta.refactorings.lib;

import edelta.lib.AbstractEdelta;
import edelta.refactorings.lib.EdeltaBadSmellsFinder;
import edelta.refactorings.lib.EdeltaRefactorings;
import java.util.List;
import java.util.function.Consumer;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EStructuralFeature;

@SuppressWarnings("all")
public class EdeltaBadSmellsResolver extends AbstractEdelta {
  private EdeltaRefactorings refactorings;
  
  private EdeltaBadSmellsFinder finder;
  
  public EdeltaBadSmellsResolver() {
    refactorings = new EdeltaRefactorings(this);
    finder = new EdeltaBadSmellsFinder(this);
  }
  
  public EdeltaBadSmellsResolver(final AbstractEdelta other) {
    super(other);
  }
  
  /**
   * Extracts superclasses in the presence of duplicate features
   * considering all the classes of the given package.
   * 
   * @param ePackage
   */
  public void resolveDuplicatedFeatures(final EPackage ePackage) {
    final Consumer<List<EStructuralFeature>> _function = (List<EStructuralFeature> it) -> {
      this.refactorings.extractSuperclass(it);
    };
    this.finder.findDuplicateFeatures(ePackage).values().forEach(_function);
  }
}
