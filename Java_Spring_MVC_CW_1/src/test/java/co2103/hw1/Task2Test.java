package co2103.hw1;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Map;

import org.junit.Test;

public class Task2Test {
	
	public Class<?> loadClass(String name) {
	    ClassLoader classLoader = this.getClass().getClassLoader();

	    try {
	        Class<?> aClass = classLoader.loadClass(name);
	        return aClass;
	    } catch (ClassNotFoundException e) {
	    }
		return null;
	}

	@Test
	public void classesAndFieldsCreated() throws Exception {
		Map<String, String> c = GenerateTasks.getConcepts();

		Class<?> parent = loadClass("co2103.hw1.domain." + c.get("Parent"));
		assertNotNull("Class " + "co2103.hw1.domain." + c.get("Parent") + " not found", parent);
		
		// fields
		assertField(parent, "private", "int", "id");
		assertField(parent, "private", "java.lang.String", c.get("pf2"));
		assertField(parent, "private", "java.lang.String", c.get("pf3"));
		assertField(parent, "private", "java.util.List", c.get("children"));		
		
		
		Class<?> child = loadClass("co2103.hw1.domain." + c.get("Child"));
		assertNotNull("Class " + "co2103.hw1.domain." + c.get("Child") + " not found", child);
		assertField(child, "private", "java.lang.String", c.get("cf1"));
		assertField(child, "private", "java.lang.String", c.get("cf2"));
		assertField(child, "private", "java.lang.String", c.get("cf3"));
		assertField(child, "private", "int", c.get("cf4"));
		
	}

	private void assertField(Class<?> parent, String modifier, String type, String name) throws Exception {
		Field f = parent.getDeclaredField(name);
		assertNotNull(f);
		assertEquals(type, f.getType().getName());
		assertEquals(Modifier.PRIVATE, f.getModifiers());				
	}

}
