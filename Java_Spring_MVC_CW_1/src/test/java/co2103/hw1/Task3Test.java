package co2103.hw1;

import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class Task3Test {

	@Test
	public void classesCreated() throws Exception {
		checkExists("src/main/java/co2103/hw1/controller/MainController.java");
	}

	private void checkExists(String path) {
		assertTrue(path + " is missing", Files.exists(Paths.get(path)));
	}

	@Autowired
	private MockMvc mockMvc;

	@Test
	public void greetingGETShouldReturnDefaultMessage() throws Exception {
		this.mockMvc.perform(get("/")).andDo(print()).andExpect(status().isOk()).andExpect(view().name("start"));
	}
	@Test
	public void greetingPOSTShouldReturnDefaultMessage() throws Exception {
		this.mockMvc.perform(post("/")).andDo(print()).andExpect(status().isOk()).andExpect(view().name("start"));
	}

}
