package maximemeire.phantom.util;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * This class represents a map of attributes that each contain a list of attribute components.
 * @author Maxime Meire
 *
 */
public class MappedAttributeList {
	
	/**
	 * The attribute map.
	 */
	private final Map<String, LinkedList<?>> attributes = new HashMap<String, LinkedList<?>>();
	
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
     * @param <T> The generic object.
     * @param name The attribute name key.
     * @param value The value of the attribute.
     */
    @SuppressWarnings("unchecked")
	public <T> void put(String name, T t) {
    	LinkedList<T> list = (LinkedList<T>) attributes.get(name);
    	if (list == null)
    		list = new LinkedList<T>();
    	list.add(t);
    	attributes.put(name, list);
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
    public Map<String, LinkedList<?>> getAttributes() {
    	return attributes;
    }

}

