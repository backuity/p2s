package org.p2s;

public class CaseUtil {
    public static String camelCaseToDotCase(String name) {
        return name.replaceAll(
                String.format("%s|%s|%s",
                        "(?<=[A-Z])(?=[A-Z][a-z])",
                        "(?<=[^A-Z])(?=[A-Z])",
                        "(?<=[A-Za-z])(?=[^A-Za-z])"
                ),
                "."
        ).toLowerCase();
    }
}
