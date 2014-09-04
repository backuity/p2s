package org.p2s;

import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import java.util.Optional;

public class ReflectionUtil {

    public static boolean isOptionalType(TypeMirror type) {
        return toTypeElement(type).map( elem -> typeEqualsClass(elem, Optional.class) ).orElse(false);
    }

    public static boolean typeEqualsClass(TypeElement type, Class<?> clazz) {
        return type.getQualifiedName().toString().equals(clazz.getCanonicalName());
    }

    public static Optional<TypeElement> toTypeElement(TypeMirror type) {
        if( type instanceof DeclaredType && ((DeclaredType)type).asElement() instanceof TypeElement) {
            return Optional.of((TypeElement)((DeclaredType)type).asElement());
        } else {
            return Optional.empty();
        }
    }

    public static boolean isInterface(TypeElement type) {
        return type.getKind() == ElementKind.INTERFACE;
    }
}
