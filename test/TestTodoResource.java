import ge.edu.freeuni.sdp.todo.core.TaskDo;
import ge.edu.freeuni.sdp.todo.core.TaskEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;

import static org.junit.Assert.*;
import org.junit.Test;

import com.microsoft.azure.storage.StorageException;

public class TestTodoResource extends JerseyTest {
	
	@Override
	protected Application configure() {
		return new ResourceConfig(FakeResource.class);
	}
	
	@Test
	public void create_and_ensure_contained_in_repository() {

		FakeRepository.instance().clear();

		TaskDo task = new TaskDo();
		task.setText("Hello, world.");

		Response actual = target("todos").request().post(
				Entity.entity(task, MediaType.APPLICATION_JSON_TYPE));

		assertEquals(Response.Status.CREATED.getStatusCode(),
				actual.getStatus());

		String[] segments = actual.getLocation().getPath().split("/");
		String id = segments[segments.length - 1];

		assertTrue(FakeRepository.instance().contains(id));
	}

	@Test
	public void add_to_repository_and_get() throws StorageException {

		FakeRepository.instance().clear();

		TaskDo expected = new TaskDo();
		expected.setId(UUID.randomUUID().toString());
		expected.setText("Hello, world!");

		TaskEntity entity = TaskEntity.fromDo(expected);
		FakeRepository.instance().insertOrUpdate(entity);

		TaskDo actual = target("todos/" + expected.getId()).request().get(
				TaskDo.class);

		assertEquals(expected, actual);
	}

	@Test
	public void add_to_repository_and_delete() throws StorageException {

		FakeRepository.instance().clear();

		TaskDo expected = new TaskDo();
		expected.setId(UUID.randomUUID().toString());
		expected.setText("Hello, world!");

		TaskEntity entity = TaskEntity.fromDo(expected);
		FakeRepository.instance().insertOrUpdate(entity);

		Response actual = target("todos/" + expected.getId()).request()
				.delete();

		assertEquals(Response.Status.OK.getStatusCode(),
				actual.getStatus());
	}

	@Test
	public void add_three_in_repository_get_all() throws StorageException {

		FakeRepository.instance().clear();
		ArrayList<TaskDo> expected = new ArrayList<TaskDo>();
		for (int i = 0; i < 3; i++) {
			TaskDo task = new TaskDo();
			task.setId(UUID.randomUUID().toString());
			task.setText(Integer.toString(i));
			expected.add(task);
			
			TaskEntity entity = TaskEntity.fromDo(task);
			FakeRepository.instance().insertOrUpdate(entity);
		}

		TaskDo[] result = target("todos").request().get(TaskDo[].class);
		List<TaskDo> actual = Arrays.asList(result);
	
		assertTrue(expected.containsAll(actual) && actual.containsAll(expected));
	}

}
