package org.p2s;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import java.io.IOException;
import java.io.Writer;
import java.util.*;

@SupportedAnnotationTypes({"org.p2s.Settings"})
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class SettingsProcessor extends AbstractProcessor {
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment env) {
        try {
            for (TypeElement element : annotations) {

                for (Element e : env.getElementsAnnotatedWith(element)) {
                    process(processingEnv.getFiler(), e, e.getEnclosingElement().toString());
                }
            }
            return true;
        } catch(Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private String typeToString(TypeMirror type) {
        if( isOptionalType(type) ) {
            TypeMirror firstTypeParam = ((DeclaredType)type).getTypeArguments().get(0);
            TypeElement param = toTypeElement(firstTypeParam).orElseThrow(() ->
                    new RuntimeException("Cannot extract type parameter " + firstTypeParam + " of optional element " + type));
            return typeElementToString(param);
        } else {
            TypeElement elem = toTypeElement(type).orElseThrow( () -> new RuntimeException("Not a valid type " + type) );
            return typeElementToString(elem);
        }
    }

    private String typeElementToString(TypeElement elem) {
        if( typeEqualsClass(elem, Integer.class)) {
            return "Integer";
        } else if( typeEqualsClass(elem, String.class)) {
            return "String";
        } else {
            throw new RuntimeException(elem + " is not a supported type.");
        }
    }

    private void process(Filer filer, Element element, String packageName) throws IOException {
        String interfaceName = element.getSimpleName().toString();
        String simpleName = element.getSimpleName().toString() + "Properties";
        String newClass = packageName + "." + simpleName;

        System.out.println("Processing " + newClass);

        List<Setting> settings = extractSettings(element, simpleName);

        try (Writer writer = filer.createSourceFile(newClass).openWriter()) {

            writer.write("/* Generated on " + new Date() + " */\n");

            writer.write("package " + packageName + ";\n");
            writer.write("\n");
            writeImports(writer);
            writer.write("\n");
            writer.write("public class " + simpleName + " extends SettingsPropertiesSupport implements " + interfaceName + " {\n");

            writeFields(settings, writer);
            writer.write("\n");

            writeMethods(settings, writer);
            writer.write("\n");

            writeLoadProperties(settings, writer);
            writer.write("}\n");
        }
    }

    private void writeImports(Writer writer) throws IOException {
        writer.write("import java.util.Optional;\n");
        writer.write("import java.util.Properties;\n");
        writer.write("import org.p2s.SettingsPropertiesSupport;\n");
    }

    private void writeLoadProperties(List<Setting> settings, Writer writer) throws IOException {
        writer.write("  public void loadProperties(Properties properties) {\n");
        for( Setting setting : settings ) {
            writer.write("      " + setting.getName() + " = load");
            writer.write(setting.isOptional() ? "Optional" : "Mandatory");
            if( "Integer".equals(setting.getType()) ) {
                writer.write("Int");
            }
            writer.write("(\"" + CaseUtil.camelCaseToDotCase(setting.getName()) + "\", properties);\n");
        }
        writer.write("  }\n");
    }

    private void writeMethods(List<Setting> settings, Writer writer) throws IOException {
        for( Setting setting: settings) {
            writer.write("  public ");
            if( setting.isOptional() ) writer.write("Optional<");
            writer.write(setting.getType());
            if( setting.isOptional() ) writer.write(">");
            writer.write(" " + setting.getName() + "() { return " + setting.getName() + "; }\n");
        }
    }

    private void writeFields(List<Setting> settings, Writer writer) throws IOException {
        for( Setting setting : settings) {
            writer.write("  private ");
            if( setting.isOptional() ) writer.write("Optional<");
            writer.write(setting.getType());
            if( setting.isOptional()) writer.write(">");
            writer.write(" " + setting.getName() + ";\n");
        }
    }

    private List<Setting> extractSettings(Element element, String simpleName) {
        if( ! (element instanceof TypeElement) ) {
            throw new IllegalArgumentException("Not an interface!");
        }
        TypeElement interf = (TypeElement)element;
        List<Setting> settings = new ArrayList<>();

        for( Element enclosedElement : interf.getEnclosedElements() ) {
            if( enclosedElement instanceof ExecutableElement) {
                ExecutableElement method = (ExecutableElement)enclosedElement;
                if( method.getParameters().size() > 0 ) {
                    throw new RuntimeException("Method " + method + " of " + simpleName + " has parameters : " + method.getParameters());
                }
                String propName = method.getSimpleName().toString();
                String type = typeToString(method.getReturnType());
                boolean isOptional = isOptionalType(method.getReturnType());
                settings.add(new Setting(propName, type, isOptional));
            }
        }
        return settings;
    }

    private boolean isOptionalType(TypeMirror type) {
        return toTypeElement(type).map( elem -> typeEqualsClass(elem, Optional.class) ).orElse(false);
    }

    private boolean typeEqualsClass(TypeElement type, Class<?> clazz) {
        return type.getQualifiedName().toString().equals(clazz.getCanonicalName());
    }

    private Optional<TypeElement> toTypeElement(TypeMirror type) {
        if( type instanceof DeclaredType && ((DeclaredType)type).asElement() instanceof TypeElement) {
            return Optional.of((TypeElement)((DeclaredType)type).asElement());
        } else {
            return Optional.empty();
        }
    }
}
