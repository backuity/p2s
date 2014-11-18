package org.p2s;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
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

        String interfaceName = element.getSimpleName().toString();
        String packageName = element.getEnclosingElement().toString();
        String simpleName = element.getSimpleName().toString() + "Properties";

        System.out.println("Processing " + packageName + "." + simpleName);

        Set<Element> recursiveElements = new HashSet<>();
        List<Setting> settings = new ArrayList<>();

        for( Element enclosedElement : interf.getEnclosedElements() ) {
            if( enclosedElement instanceof ExecutableElement) {

                ExecutableElement method = (ExecutableElement)enclosedElement;
                if( method.getParameters().size() > 0 ) {
                    throw new RuntimeException("Method " + method + " of " + simpleName + " has parameters : " + method.getParameters());
                }

                Setting setting = makeSetting(method, recursiveElements);
                settings.add(setting);
            }
        }

        classes.add(new SettingsClass(packageName, interfaceName, simpleName, settings));

        for( Element recursiveElement: recursiveElements) {
            processClass(recursiveElement, classes);
        }
    }

    private Setting makeSetting(ExecutableElement method, Set<Element> recursiveElements) {
        TypeMirror methodType = method.getReturnType();

        TypeElement elem;
        boolean isOptional = false;

        if( isOptionalType(methodType) ) {
            TypeMirror firstTypeParam = ((DeclaredType)methodType).getTypeArguments().get(0);
            elem = toTypeElement(firstTypeParam).orElseThrow(() ->
                    new RuntimeException("Cannot extract type parameter " + firstTypeParam + " of optional element " + methodType));
            isOptional = true;
        } else {
            elem = toTypeElement(methodType).orElseThrow( () -> new RuntimeException("Not a valid type " + methodType) );
        }

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

        return new Setting(propName, type, pkg, isOptional, isNestedType);
    }

    private static List<Class<?>> supportedBasicTypes = Arrays.asList(
        Integer.class,
        String.class,
        Boolean.class,
        Long.class
    );

    private Optional<Class<?>> findSupportedBasicType(TypeElement elem) {
        return supportedBasicTypes.stream()
                .filter( supportedType -> typeEqualsClass(elem, supportedType))
                .findFirst();
    }

    private boolean isSupportedBasicType(TypeElement elem) {
        return findSupportedBasicType(elem).isPresent();
    }
}
