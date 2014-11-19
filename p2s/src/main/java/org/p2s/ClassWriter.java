package org.p2s;

import javax.annotation.processing.Filer;
import java.io.IOException;
import java.io.Writer;
import java.util.Date;
import java.util.List;

class ClassWriter {
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
                    " implements " + settingsClass.getInterfaceName() + " {\n");

            writeFields(settingsClass.getSettings(), writer);
            writer.write("\n");

            writeConstructors(settingsClass.getSimpleClassName(), settingsClass.getSettings(), writer);
            writer.write("\n");

            writeMethods(settingsClass.getSettings(), writer);
            writer.write("}\n");
        }
    }

    private void writeImports(SettingsClass settingsClass, Writer writer) throws IOException {
        writer.write("import java.util.Optional;\n"); // TODO do not import them if they are not used
        writer.write("import java.util.List;\n");
        writer.write("import org.p2s.DotCaseProperties;\n");

        for( Setting setting : settingsClass.getSettings()) {
            if((!setting.getPkg().isEmpty()) && (!setting.getPkg().equals(settingsClass.getPackageName()))) {
               writer.write("import " + setting.getPkg() + "." + setting.getType() + ";\n");
               writer.write("import " + setting.getPkg() + "." + setting.getType() + "Properties;\n");
            }
        }
    }

    private void writeConstructors(String className, List<Setting> settings, Writer writer) throws IOException {
        writer.write("  public " + className + "(DotCaseProperties<?> properties) {\n");
        writer.write("     this(\"\", properties);\n");
        writer.write("  }\n");
        writer.write("\n");
        writer.write("  public " + className + "(String prefix, DotCaseProperties<?> properties) {\n");

        for( Setting setting : settings ) {
            String dotCaseName = CaseUtil.camelCaseToDotCase(setting.getName());

            if( setting.isNestedType() && !setting.isList() ) dotCaseName += ".";

            String name = "prefix + \"" + dotCaseName + "\"";
            writer.write("      " + setting.getName() + " = ");
            if( setting.isList() ) {
                if( setting.isNestedType() ) {
                    writer.write("properties.getProperties(" + name + ", " + setting.getType() + "Properties::new);\n");
                } else {
                    // a list cannot be optional
                    writer.write("properties.getProperties(" + name + ", " + setting.getType() + ".class);\n");
                }
            } else if( setting.isNestedType() ) {
                if( setting.isOptional() ) {
                    throw new RuntimeException("Optional nested type isn't yet supported!");
                }

                writer.write("new " + setting.getType() + "Properties(" + name + ", properties);\n");
            } else {
                writer.write("properties.load");
                writer.write(setting.isOptional() ? "Optional" : "Mandatory");
                writer.write("(" + name + ", " + setting.getType() + ".class);\n");
            }
        }
        writer.write("  }\n");
    }

    private void writeMethods(List<Setting> settings, Writer writer) throws IOException {
        for (Setting setting: settings) {
            writer.write("  public ");
            writeType(writer, setting);
            writer.write(" " + setting.getName() + "() { return " + setting.getName() + "; }\n");
        }
    }

    private void writeFields(List<Setting> settings, Writer writer) throws IOException {
        for( Setting setting : settings) {
            writer.write("  private ");
            writeType(writer, setting);
            writer.write(" " + setting.getName() + ";\n");
        }
    }

    private void writeType(Writer writer, Setting setting) throws IOException {
        if( setting.isOptional() ) {
            writer.write("Optional<");
            writer.write(setting.getType());
            writer.write(">");
        } else if( setting.isList() ) {
            writer.write("List<");
            writer.write(setting.getType());
            writer.write(">");
        } else {
            writer.write(setting.getType());
        }
    }
}
