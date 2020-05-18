package edelta.lib;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.function.Supplier;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EEnumLiteral;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtext.xbase.lib.Extension;

import edelta.lib.exception.EdeltaPackageNotLoadedException;

/**
 * Base class for code generated by the Edelta DSL
 * 
 * @author Lorenzo Bettini
 *
 */
public abstract class AbstractEdelta {

	private static final Logger LOG = Logger.getLogger(AbstractEdelta.class);

	/**
	 * For loading ecores and all other runtime {@link EPackage} management.
	 */
	private EdeltaEPackageManager packageManager;

	/**
	 * Initializers for EClassifiers which will be executed later, after
	 * all EClassifiers have been created.
	 */
	private List<Runnable> eClassifierInitializers = new LinkedList<>();

	/**
	 * Initializers for EStructuralFeatures which will be executed later, after
	 * all EStructuralFeatures have been created.
	 */
	private List<Runnable> eStructuralFeaturesInitializers = new LinkedList<>();

	/**
	 * This will be used in the generated code with extension methods.
	 */
	@Extension
	protected EdeltaLibrary lib = new EdeltaLibrary();

	public AbstractEdelta() {
		packageManager = new EdeltaEPackageManager();
	}

	/**
	 * Reuses the {@link EdeltaEPackageManager} from the other object.
	 * 
	 * @param other
	 */
	public AbstractEdelta(AbstractEdelta other) {
		this(other.packageManager);
	}

	/**
	 * Uses the passed {@link EdeltaEPackageManager}.
	 * 
	 * @param packageManager
	 */
	public AbstractEdelta(EdeltaEPackageManager packageManager) {
		this.packageManager = packageManager;
	}

	/**
	 * Performs the actual execution of the transformation, to be
	 * called by clients.
	 * 
	 * @throws Exception
	 */
	public void execute() throws Exception {
		performSanityChecks();
		doExecute();
		runInitializers();
	}

	/**
	 * This is meant to be implemented by the code generated by
	 * the Edelta DSL, in order to perform sanity checks, such as if
	 * all EPackages (their ecores) are loaded.
	 * 
	 * @throws Exception
	 */
	protected void performSanityChecks() throws Exception {
		// to be implemented by the generated code
	}

	/**
	 * Actual implementation of the transformation, to be generated
	 * by the Edelta DSL compiler.
	 * 
	 * @throws Exception
	 */
	protected void doExecute() throws Exception {
		// to be implemented by the generated code
	}

	/**
	 * Executes the initializers previously saved.
	 */
	protected void runInitializers() {
		eClassifierInitializers.forEach(Runnable::run);
		eStructuralFeaturesInitializers.forEach(Runnable::run);
	}

	/**
	 * Throws an {@link EdeltaPackageNotLoadedException} if the specified
	 * EPackage (its Ecore) has not been loaded.
	 * 
	 * @param packageName
	 * @throws EdeltaPackageNotLoadedException
	 */
	protected void ensureEPackageIsLoaded(String packageName) throws EdeltaPackageNotLoadedException {
		if (getEPackage(packageName) == null) {
			throw new EdeltaPackageNotLoadedException(packageName);
		}
	}

	public void loadEcoreFile(String path) {
		packageManager.loadEcoreFile(path);
	}

	/**
	 * Saves the modified EPackages as Ecore files in the specified
	 * output path.
	 * 
	 * The final path of the generated Ecore files is made of the
	 * specified outputPath and the original loaded Ecore
	 * file names.
	 * 
	 * @param outputPath
	 * @throws IOException 
	 */
	public void saveModifiedEcores(String outputPath) throws IOException {
		for (Entry<String, Resource> entry : packageManager.getResourceMapEntrySet()) {
			Path p = Paths.get(entry.getKey());
			final String fileName = p.getFileName().toString();
			LOG.info("Saving " + outputPath + "/" + fileName);
			File newFile = new File(outputPath, fileName);
			FileOutputStream fos = new FileOutputStream(newFile);
			entry.getValue().save(fos, null);
			fos.flush();
			fos.close();
		}
	}

	public Logger getLogger() {
		return Logger.getLogger(getClass());
	}

	public void logError(Supplier<String> messageSupplier) {
		internalLog(Level.ERROR, messageSupplier);
	}

	public void logWarn(Supplier<String> messageSupplier) {
		internalLog(Level.WARN, messageSupplier);
	}

	public void logInfo(Supplier<String> messageSupplier) {
		internalLog(Level.INFO, messageSupplier);
	}

	public void logDebug(Supplier<String> messageSupplier) {
		internalLog(Level.DEBUG, messageSupplier);
	}

	private void internalLog(Level level, Supplier<String> messageSupplier) {
		Logger logger = getLogger();
		if (logger.isEnabledFor(level)) {
			logger.log(level, messageSupplier.get());
		}
	}

	public void showError(String message) {
		logError(() -> message);
	}

	public void showWarning(String message) {
		logWarn(() -> message);
	}

	/**
	 * Retrieves an {@link EPackage} given its fully qualified name, that is, for
	 * subpackages, the string is expected to contain the names of the full EPackage
	 * hierarchy separated by dots. For example,
	 * "rootpackage.subpackage.subsubpackage".
	 * 
	 * @param packageName
	 * @return the found {@link EPackage} or null
	 */
	public EPackage getEPackage(String packageName) {
		String[] packageNames = packageName.split("\\.");
		EPackage currentPackage = getEPackageFromPackageManager(packageNames[0]);
		int packageNameIndex = 1;
		while (currentPackage != null && packageNameIndex < packageNames.length) {
			String currentPackageName = packageNames[packageNameIndex];
			currentPackage = currentPackage.getESubpackages().stream()
					.filter(p -> currentPackageName.equals(p.getName()))
					.findFirst()
					.orElse(null);
			packageNameIndex++;
		}
		return currentPackage;
	}

	private EPackage getEPackageFromPackageManager(String packageName) {
		return packageManager.getEPackage(packageName);
	}

	public EClassifier getEClassifier(String packageName, String classifierName) {
		EPackage p = getEPackage(packageName);
		if (p == null) {
			return null;
		}
		return p.getEClassifier(classifierName);
	}

	public EClass getEClass(String packageName, String className) {
		EClassifier c = getEClassifier(packageName, className);
		if (c instanceof EClass) {
			return (EClass) c;
		}
		return null;
	}

	public EDataType getEDataType(String packageName, String datatypeName) {
		EClassifier c = getEClassifier(packageName, datatypeName);
		if (c instanceof EDataType) {
			return (EDataType) c;
		}
		return null;
	}

	public EEnum getEEnum(String packageName, String enumName) {
		EClassifier c = getEClassifier(packageName, enumName);
		if (c instanceof EEnum) {
			return (EEnum) c;
		}
		return null;
	}

	public EStructuralFeature getEStructuralFeature(String packageName, String className, String featureName) {
		EClass c = getEClass(packageName, className);
		if (c == null) {
			return null;
		}
		return c.getEStructuralFeature(featureName);
	}

	public EAttribute getEAttribute(String packageName, String className, String attributeName) {
		EStructuralFeature f = getEStructuralFeature(packageName, className, attributeName);
		if (f instanceof EAttribute) {
			return (EAttribute) f;
		}
		return null;
	}

	public EReference getEReference(String packageName, String className, String referenceName) {
		EStructuralFeature f = getEStructuralFeature(packageName, className, referenceName);
		if (f instanceof EReference) {
			return (EReference) f;
		}
		return null;
	}

	public EEnumLiteral getEEnumLiteral(String packageName, String enumName, String enumLiteralName) {
		EEnum eenum = getEEnum(packageName, enumName);
		if (eenum == null) {
			return null;
		}
		return eenum.getEEnumLiteral(enumLiteralName);
	}

	public void removeEClassifier(String packageName, String name) {
		EdeltaEcoreUtil.removeEClassifier(getEClassifier(packageName, name));
	}

	public EClassifier copyEClassifier(String packageName, String classifierName) {
		EClassifier eClassifier = getEClassifier(packageName, classifierName);
		return EdeltaEcoreUtil.copyENamedElement(eClassifier);
	}
}
