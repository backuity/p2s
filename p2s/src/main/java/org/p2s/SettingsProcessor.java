package org.p2s;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.PrimitiveType;
import javax.lang.model.type.TypeMirror;
import java.io.IOException;
import static org.p2s.ReflectionUtil.*;
import java.util.*;

@SupportedAnnotationTypes({"org.p2s.Settings"})
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class SettingsProcessor extends AbstractProcessor {

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment env) {
        try {
            final ClassWriter writer = new ClassWriter(processingEnv.getFiler());
            SettingsClasses classes = new SettingsClasses();

            for (TypeElement element : annotations) {
                for (Element e : env.getElementsAnnotatedWith(element)) {
                    processClass(e, classes);
                }
            }

            for(SettingsClass settingsClass : classes.getClasses()) {
                writer.writeClass(settingsClass);
            }
            return true;
        } catch(Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private void processClass(Element element, SettingsClasses classes) throws IOException {
        if( ! (element instanceof TypeElement) ) {
            throw new IllegalArgumentException(element + " is not an interface!");
        }
        TypeElement interf = (TypeElement)element;

        String interfaceName = getInterfaceName(element);

        Element enclosingElement = element.getEnclosingElement();
        String packageName = getPackageName(enclosingElement);
        String simpleName = getClassName(element) + "Properties";

        System.out.println("Processing " + packageName + "." + simpleName);
        try {

            Set<Element> recursiveElements = new HashSet<>();
            List<Setting> settings = new ArrayList<>();

            for (Element enclosedElement : interf.getEnclosedElements()) {
                if (enclosedElement instanceof ExecutableElement) {

                    ExecutableElement method = (ExecutableElement) enclosedElement;
                    if (method.getParameters().size() > 0) {
                        throw new RuntimeException("Method " + method + " of " + simpleName + " has parameters : " + method.getParameters());
                    }

                    Setting setting = makeSetting(method, recursiveElements);
                    settings.add(setting);
                }
            }

            classes.add(new SettingsClass(packageName, interfaceName, simpleName, settings));

            for (Element recursiveElement : recursiveElements) {
                processClass(recursiveElement, classes);
            }
        } catch( Exception e ) {
            throw new RuntimeException("Failed to process " + packageName + "." + simpleName, e);
        }
    }

    private String getInterfaceName(Element element) {
        String simpleName = element.getSimpleName().toString();
        Element enclosingElement = element.getEnclosingElement();
        if( enclosingElement instanceof TypeElement ) {
            return getInterfaceName(enclosingElement) + "." + simpleName;
        } else {
            return simpleName;
        }
    }

    private String getClassName(Element element) {
        String simpleName = element.getSimpleName().toString();
        Element enclosingElement = element.getEnclosingElement();
        if( enclosingElement instanceof TypeElement ) {
            return getClassName(enclosingElement) + "$" + simpleName;
        } else {
            return simpleName;
        }
    }

    private String getPackageName(Element element) {
        if( element instanceof PackageElement ) {
            return element.toString();
        } else {
            return getPackageName(element.getEnclosingElement());
        }
    }

    private Setting makeSetting(ExecutableElement method, Set<Element> recursiveElements) {
        TypeMirror methodType = method.getReturnType();

        if( isOptionalType(methodType) ) {
            TypeMirror firstTypeParam = ((DeclaredType)methodType).getTypeArguments().get(0);
            TypeElement elem = toTypeElement(firstTypeParam).orElseThrow(() ->
                    new RuntimeException("Cannot extract optional type parameter " + firstTypeParam + " of method " + method));
            return makeSetting(method, recursiveElements, elem, true, false);
        } else if( isListType(methodType) ) {
            TypeMirror firstTypeParam = ((DeclaredType)methodType).getTypeArguments().get(0);
            TypeElement elem = toTypeElement(firstTypeParam).orElseThrow(() ->
                    new RuntimeException("Cannot extract list type parameter " + firstTypeParam + " of method " + method));
            return makeSetting(method, recursiveElements, elem, false, true);
        } else if( isPrimitiveType(methodType) ) {
            if( isSupportedPrimitiveType(methodType)) {
                return makePrimitiveSetting(method, methodType.toString());
            } else {
                throw new RuntimeException("Unsupported primitive type " + methodType + " for method " + method);
            }
        } else {
            TypeElement elem = toTypeElement(methodType).orElseThrow(() -> new RuntimeException("Method " + method +
                    " return type " + methodType + " (" + methodType.getClass() + ") isn't supported"));
            return makeSetting(method, recursiveElements, elem, false, false);
        }
    }

    private Setting makePrimitiveSetting(ExecutableElement method, String primitiveType) {
        String propName = method.getSimpleName().toString();
        return new Setting(propName, primitiveType, "", false, false, false);
    }

    private Setting makeSetting(ExecutableElement method, Set<Element> recursiveElements, TypeElement elem, boolean isOptional, boolean isList) {
        boolean isNestedType = false;
        String pkg = "";

        if( ! isSupportedBasicType(elem) ) {
            if( isInterface(elem) ) {
                pkg = elem.getEnclosingElement().toString();
                recursiveElements.add(elem);
                isNestedType = true;
            } else {
                throw new RuntimeException(elem + " is not supported, it should either be an interface or one of " + supportedBasicTypes);
            }
        }

        String type = elem.getSimpleName().toString();
        String propName = method.getSimpleName().toString();

        return new Setting(propName, type, pkg, isOptional, isNestedType, isList);
    }

    private static List<Class<?>> supportedBasicTypes = Arrays.asList(
        Integer.class,
        String.class,
        Boolean.class,
        Long.class
    );

    private static List<String> supportedPrimitiveTypes =
        Arrays.asList("long","int","boolean");

    private boolean isSupportedBasicType(TypeElement elem) {
        return supportedBasicTypes.stream()
                .filter( supportedType -> typeEqualsClass(elem, supportedType))
                .findFirst().isPresent();
    }

    private boolean isSupportedPrimitiveType(TypeMirror tpe) {
        return supportedPrimitiveTypes.contains(tpe.toString());
    }
}
