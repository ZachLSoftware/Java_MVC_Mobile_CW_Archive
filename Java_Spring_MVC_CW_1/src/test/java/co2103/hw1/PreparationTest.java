package co2103.hw1;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class PreparationTest {

	@Test
	public void userDefined() throws Exception {
		assertNotNull("No username was specified in application.properties.", GenerateTasks.getUser());
	}
}
