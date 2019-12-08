/**
 * 
 */
package edelta.lib;

import java.util.function.Consumer;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EEnumLiteral;
import org.eclipse.emf.ecore.ENamedElement;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcoreFactory;

/**
 * Library functions to be reused in Edelta programs.
 * 
 * @author Lorenzo Bettini
 *
 */
public class EdeltaLibrary {

	private EcoreFactory ecoreFactory = EcoreFactory.eINSTANCE;

	private <T extends ENamedElement> void safeRunInitializer(Consumer<T> initializer, T e) {
		if (initializer != null) {
			initializer.accept(e);
		}
	}

	public EClass newEClass(String name) {
		return newEClass(name, null);
	}

	public EClass newEClass(String name, Consumer<EClass> initializer) {
		EClass c = ecoreFactory.createEClass();
		c.setName(name);
		safeRunInitializer(initializer, c);
		return c;
	}

	public EAttribute newEAttribute(String name) {
		return newEAttribute(name, null);
	}

	public EAttribute newEAttribute(String name, Consumer<EAttribute> initializer) {
		EAttribute e = ecoreFactory.createEAttribute();
		e.setName(name);
		safeRunInitializer(initializer, e);
		return e;
	}

	public EAttribute newEAttribute(String name, EDataType dataType, Consumer<EAttribute> initializer) {
		EAttribute e = ecoreFactory.createEAttribute();
		e.setName(name);
		e.setEType(dataType);
		safeRunInitializer(initializer, e);
		return e;
	}

	public EReference newEReference(String name) {
		return newEReference(name, null);
	}

	public EReference newEReference(String name, Consumer<EReference> initializer) {
		EReference e = ecoreFactory.createEReference();
		e.setName(name);
		safeRunInitializer(initializer, e);
		return e;
	}

	public EReference newEReference(String name, EClass referenceType, Consumer<EReference> initializer) {
		EReference e = ecoreFactory.createEReference();
		e.setName(name);
		e.setEType(referenceType);
		safeRunInitializer(initializer, e);
		return e;
	}

	public EEnum newEEnum(String name) {
		return newEEnum(name, null);
	}

	public EEnum newEEnum(String name, Consumer<EEnum> initializer) {
		EEnum e = ecoreFactory.createEEnum();
		e.setName(name);
		safeRunInitializer(initializer, e);
		return e;
	}

	public EEnumLiteral newEEnumLiteral(String name) {
		return newEEnumLiteral(name, null);
	}

	public EEnumLiteral newEEnumLiteral(String name, Consumer<EEnumLiteral> initializer) {
		EEnumLiteral e = ecoreFactory.createEEnumLiteral();
		e.setName(name);
		safeRunInitializer(initializer, e);
		return e;
	}

	/**
	 * Returns a String representation based on the containment relation.
	 * 
	 * @param e
	 * @return
	 */
	public String getEObjectRepr(EObject e) {
		String info = e.toString();
		if (e instanceof ENamedElement) {
			info = ((ENamedElement) e).getName();
		}
		return e.eContainer() != null ? getEObjectRepr(e.eContainer()) + ":" + info : info;
	}

	public void addEStructuralFeature(EClass eClass, EStructuralFeature eStructuralFeature) {
		eClass.getEStructuralFeatures().add(eStructuralFeature);
	}

	public void addEClass(EPackage ePackage, EClass eClass) {
		ePackage.getEClassifiers().add(eClass);
	}

	public EClass addNewEClass(EPackage ePackage, String name) {
		return addNewEClass(ePackage, name, null);
	}

	public EClass addNewEClass(EPackage ePackage, String name, Consumer<EClass> initializer) {
		EClass c = ecoreFactory.createEClass();
		c.setName(name);
		addEClass(ePackage, c);
		safeRunInitializer(initializer, c);
		return c;
	}

	public void addEAttribute(EClass eClass, EAttribute eAttribute) {
		addEStructuralFeature(eClass, eAttribute);
	}

	public EAttribute addNewEAttribute(EClass eClass, String name, EDataType dataType) {
		return addNewEAttribute(eClass, name, dataType, null);
	}

	public EAttribute addNewEAttribute(EClass eClass, String name, EDataType dataType, Consumer<EAttribute> initializer) {
		EAttribute e = ecoreFactory.createEAttribute();
		e.setName(name);
		e.setEType(dataType);
		addEAttribute(eClass, e);
		safeRunInitializer(initializer, e);
		return e;
	}

	public void addEReference(EClass eClass, EReference eReference) {
		addEStructuralFeature(eClass, eReference);
	}

	public EReference addNewEReference(EClass eClass, String name, EClass referenceType) {
		return addNewEReference(eClass, name, referenceType, null);
	}

	public EReference addNewEReference(EClass eClass, String name, EClass referenceType, Consumer<EReference> initializer) {
		EReference e = ecoreFactory.createEReference();
		e.setName(name);
		e.setEType(referenceType);
		addEReference(eClass, e);
		safeRunInitializer(initializer, e);
		return e;
	}

}
