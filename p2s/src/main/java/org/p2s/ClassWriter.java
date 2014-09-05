package org.p2s;

import javax.annotation.processing.Filer;
import java.io.IOException;
import java.io.Writer;
import java.util.Date;
import java.util.List;

public class ClassWriter {
    private Filer filer;

    public ClassWriter(Filer filer) {
        this.filer = filer;
    }

    public void writeClass(SettingsClass settingsClass) throws IOException {

        try (Writer writer = filer.createSourceFile(settingsClass.getClassName()).openWriter()) {

            writer.write("/* Generated on " + new Date() + " */\n");

            writer.write("package " + settingsClass.getPackageName() + ";\n");
            writer.write("\n");
            writeImports(settingsClass, writer);
            writer.write("\n");
            writer.write("public class " + settingsClass.getSimpleClassName() +
                    " extends SettingsPropertiesSupport implements " + settingsClass.getInterfaceName() + " {\n");

            writeFields(settingsClass.getSettings(), writer);
            writer.write("\n");

            writeConstructors(settingsClass.getSimpleClassName(), settingsClass.getSettings(), writer);
            writer.write("\n");

            writeMethods(settingsClass.getSettings(), writer);
            writer.write("}\n");
        }
    }

    private void writeImports(SettingsClass settingsClass, Writer writer) throws IOException {
        writer.write("import java.util.Optional;\n");
        writer.write("import java.util.Properties;\n");
        writer.write("import org.p2s.SettingsPropertiesSupport;\n");

        for( Setting setting : settingsClass.getSettings()) {
            if((!setting.getPkg().isEmpty()) && (!setting.getPkg().equals(settingsClass.getPackageName()))) {
               writer.write("import " + setting.getPkg() + "." + setting.getType() + ";\n");
               writer.write("import " + setting.getPkg() + "." + setting.getType() + "Properties;\n");
            }
        }
    }

    private void writeConstructors(String className, List<Setting> settings, Writer writer) throws IOException {
        writer.write("  public " + className + "(Properties properties) {\n");
        writer.write("     this(\"\", properties);\n");
        writer.write("  }\n");
        writer.write("\n");
        writer.write("  public " + className + "(String prefix, Properties properties) {\n");

        for( Setting setting : settings ) {
            String dotCaseName = CaseUtil.camelCaseToDotCase(setting.getName());
            writer.write("      " + setting.getName() + " = ");
            if( setting.isNestedType() ) {
                if( setting.isOptional() ) {
                    throw new RuntimeException("Optional nested type isn't yet supported!");
                }
                writer.write("new " + setting.getType() + "Properties(prefix + \"" + dotCaseName + ".\", properties);\n");
            } else {
                writer.write("load");
                writer.write(setting.isOptional() ? "Optional" : "Mandatory");
                if( !"String".equals(setting.getType()) ) {
                    // special handling for non-string types
                    writer.write(setting.getType());
                }
                writer.write("(prefix + \"" + dotCaseName + "\", properties);\n");
            }
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
}
