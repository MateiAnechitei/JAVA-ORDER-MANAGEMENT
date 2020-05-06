package start;

import java.lang.reflect.Field;
/*(
 * Reflection Class is used as a generalized form for toString() method.
 */
public class Reflection {
	
	public static void retrieveProperties(Object object) {
		for (Field field : object.getClass().getDeclaredFields()) {
			field.setAccessible(true);
			Object value;
			try {
				value = field.get(object);
				System.out.println(field.getName() + "=" + value);

			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
	}
}
