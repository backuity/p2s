package org.p2s;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.function.Function;
import java.util.stream.Collectors;

public class JulDotCaseProperties extends DotCaseProperties<JulDotCaseProperties> {
    private Properties properties;

    public JulDotCaseProperties(Properties properties) {
        this.properties = properties;
    }

    @Override
    public Optional<String> getProperty(String name) {
        return Optional.ofNullable(properties.getProperty(name));
    }

    @Override
    public boolean hasProperty(String name) {
        return properties.keySet().stream().filter( k -> k.toString().startsWith(name)).findAny().isPresent();
    }

    @Override
    public List<String> getProperties(String name) {
        HashMap<Integer,String> valueIndexes = new HashMap<>();
        for( Object keyObj : properties.keySet() ) {
            String key = keyObj.toString();
            if( key.startsWith(name + ".") ) {
                String idStr = key.substring(name.length() + 1);
                int id;
                try {
                    id = Integer.parseInt(idStr);
                } catch(NumberFormatException e) {
                    throw new IllegalStateException("Cannot parse id " + idStr + " of list key " + key);
                }
                valueIndexes.put(id, properties.get(key).toString());
            }
        }

        return valueIndexes.entrySet().stream()
                .sorted(Comparator.comparing(Map.Entry::getKey)) // sort by Id
                .map(Map.Entry::getValue) // extract the values
                .collect(Collectors.toList());
    }

    @Override
    public <T> List<T> getProperties(String name, Function<DotCaseProperties<?>, T> producer) {
        HashMap<Integer,Properties> valueIndexes = new HashMap<>();
        for( Object keyObj : properties.keySet() ) {
            String key = keyObj.toString();
            if( key.startsWith(name + ".") ) {
                String rightHandSide = key.substring(name.length() + 1);
                if( !rightHandSide.contains(".")) {
                    throw new IllegalStateException("Cannot parse key " + key + " it has no right hand side : " + rightHandSide);
                }
                String idStr = rightHandSide.substring(0, rightHandSide.indexOf('.'));
                String subKey = rightHandSide.substring(rightHandSide.indexOf('.') + 1);

                int id;
                try {
                    id = Integer.parseInt(idStr);
                } catch(NumberFormatException e) {
                    throw new IllegalStateException("Cannot parse id " + idStr + " of list key " + key);
                }
                Properties props;
                if( !valueIndexes.containsKey(id) ) {
                    props = new Properties();
                    valueIndexes.put(id, props);
                } else {
                    props = valueIndexes.get(id);
                }
                props.put(subKey, properties.get(key));
            }
        }

        return valueIndexes.entrySet().stream()
                .sorted(Comparator.comparing(Map.Entry::getKey)) // sort by Id
                .map(Map.Entry::getValue) // extract the values
                .map(JulDotCaseProperties::new)
                .map(producer)
                .collect(Collectors.toList());
    }

    @Override
    public void setProperty(String key, String value) {
        properties.setProperty(key, value);
    }

    @Override
    public JulDotCaseProperties withFallback(JulDotCaseProperties other) {
        Properties mergedProperties = new Properties();
        mergedProperties.putAll(other.properties);
        mergedProperties.putAll(this.properties);
        return new JulDotCaseProperties(mergedProperties);
    }
}
