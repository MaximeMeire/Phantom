package maximemeire.phantom.util;

import java.util.HashMap;
import java.util.Map;

/**
 * This class represents a list that can store temporary objects by name.
 *
 */
public class AttributeMap {
	
	/**
	 * The map with attributes.
	 */
	private final Map<String, Object> attributes = new HashMap<String, Object>();
	
    /**
     * Gets the attribute value.
     * @param <T> The generic object.
     * @param name The attribute name.
     * @return the Attribute value.
     */
    @SuppressWarnings("unchecked")
    public <T> T get(String name) {
    	return (T) attributes.get(name);
    }
    
    /**
     * Set an attribute.
     * @param name The attribute name key
     * @param value The value of the attribute
     */
    public void put(String name, Object value) {
    	attributes.put(name, value);
    }
    
    /**
     * Checks if the attributes contains data or not.
     * @return If the map is empty or not.
     */
    public boolean isEmpty() {
    	return attributes.isEmpty();
    }
    /**
     * Gets the attribute map
     * @return the attribute map
     */
    public Map<String, Object> getAttributes() {
    	return attributes;
    }

}

