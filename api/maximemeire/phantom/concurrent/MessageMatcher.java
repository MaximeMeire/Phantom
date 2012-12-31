package maximemeire.phantom.concurrent;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import maximemeire.phantom.concurrent.annotations.OnHandlerSwap;
import maximemeire.phantom.concurrent.annotations.OnKill;
import maximemeire.phantom.concurrent.annotations.OnMessage;

/**
 * @author Maxime Meire
 *
 */
public class MessageMatcher {
	
	/**
	 * The map that contains a mapping of a message objects to a method in the handler.
	 */
	private Map<Class<?>, Method> methodMessages = new HashMap<Class<?>, Method>();
	/**
	 * The class instance that contains the message handlers.
	 */
	private Object handler;
	
	/**
	 * Creates a new MessageInterpreter instance.
	 * @param handler The object containing all the methods mapped to messages.
	 */
	public MessageMatcher(Object handler) {
		this.handler = handler;
		cacheOnMessageMethods();
	}
	
	/**
	 * Called when a message is delivered by a {@link MessageMultiplexer}. This method calls
	 * the correct method mapped to the message.
	 * @param <T> The {@link Actor} type.
	 * @param message The {@link Message} sent to the {@link Actor} this interpreter belongs to.
	 * @return The return type of the method invoked.
	 */
	public <T extends Actor> Object onMessageDelivery(Message<T> message) {
		try {
			Method method = getMessageHandler(message);
			if (method != null) {
				Class<?>[] parameterTypes = method.getParameterTypes();
				Object[] args;
				if (parameterTypes.length == 1 && parameterTypes[0].isAssignableFrom(message.getClass())) 
					args = new Object[] { message };
				else
					args = new Object[0];
				method.setAccessible(true);
				return method.invoke(handler, args);
			} else {
				throw new Exception("No valid method was found for Message of type: " + message.getClass() + " in Handler of type: " + handler.getClass());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Called when the actor requires a handler swap and executes the {@link OnHandlerSwap} method.
	 * @param <T> The {@link Actor} type.
	 * @return The return type of the method invoked.
	 */
	public <T extends Actor> Object onHandlerSwap() {
		try {
			Method method = getOnHandlerSwapHandler();
			if (method != null) {
				method.setAccessible(true);
				return method.invoke(handler, new Object[0]);
			} else {
				throw new Exception("No valid OnHandlerSwap method was found in Handler of type: " + handler.getClass());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Called when the actor requires to be killed and executes the {@link OnKill} method.
	 * @param <T>
	 * @return
	 */
	public <T extends Actor> Object onKill() {
		try {
			Method method = getOnKillHandler();
			if (method != null) {
				method.setAccessible(true);
				return method.invoke(handler, new Object[0]);
			} else {
				throw new Exception("No valid OnKill method was found in Handler of type: " + handler.getClass());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Gets the method to which the specified {@link Message} is mapped.
	 * @param message The mapped {@link Message} object.
	 * @return The mapped method.
	 */
	private <T extends Actor> Method getMessageHandler(Message<T> message) {
		Method method;
		Class<?> type = message.getClass();
		do {
			method = methodMessages.get(type);
			if (method == null) 
				type = type.getSuperclass();
		} while (method == null && type != null);
		return method;
	}
	
	private Method getOnHandlerSwapHandler() {
		for (Method m : handler.getClass().getMethods()) {
			if (m.isAnnotationPresent(OnHandlerSwap.class) && m.getParameterTypes().length == 0)
				return m;
		}
		return null;
	}
	
	private Method getOnKillHandler() {
		for (Method m : handler.getClass().getMethods()) {
			if (m.isAnnotationPresent(OnKill.class) && m.getParameterTypes().length == 0)
				return m;
		}
		return null;
	}
	
	/**
	 * Map all methods of the current handler to their {@link Message} objects
	 * based on their annotation.
	 */
	private void cacheOnMessageMethods() {
		for (Method m : handler.getClass().getMethods()) {
			OnMessage annotation = m.getAnnotation(OnMessage.class);
			if (annotation != null) {
				methodMessages.put(annotation.type(), m);
				System.out.println("Cached method: " + m.getName() + " with message object: " + annotation.type().getName());
			}
		}
	}

}
