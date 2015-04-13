import ge.edu.freeuni.sdp.todo.core.TaskDo;
import ge.edu.freeuni.sdp.todo.data.TaskEntity;

import java.util.*;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.*;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;

import static org.junit.Assert.*;

import org.junit.Test;

import com.microsoft.azure.storage.StorageException;

public class TestTaskService extends JerseyTest {

	/*
	HTTP  |              /todos   	            	|            /todos/{ID}
	------|-----------------------------------------|----------------------------------------
	GET   | 200 (OK), list of tasks. 				| 200 (OK), single task. 404 (Not Found).
	PUT   | 404 (Not Found), N/A 					| 200 (OK). 404 (Not Found).
	POST  | 201 (Created),  Location: /todos/{ID} 	| 404 (Not Found).
	DELETE| 404 (Not Found) 						| 200 (OK). 404 (Not Found)
    */	
	
	@Override
	protected Application configure() {
		return new ResourceConfig(FakeTaskService.class);
	}

	// +++ GET List +++ 
	
	@Test
	public void get_list_expect_200_empty() throws StorageException {

		FakeRepository.instance().clear();

		TaskDo[] result = 
				target("todos")
				.request()
				.get(TaskDo[].class);
		assertEquals(0, result.length);
	}

	@Test
	public void get_list_expect_200_1_item() throws StorageException {

		FakeRepository.instance().clear();
		TaskDo task = createRandomTaskDo();
		TaskEntity entity = TaskEntity.fromDo(task);
		FakeRepository.instance().insertOrUpdate(entity);

		TaskDo[] result = 
				target("todos")
				.request()
				.get(TaskDo[].class);
		
		assertEquals(1, result.length);
		assertEquals(task, result[0]);
	}

	@Test
	public void get_list_expect_200_3_items() throws StorageException {

		FakeRepository.instance().clear();
		ArrayList<TaskDo> expected = new ArrayList<TaskDo>();
		for (int i = 0; i < 3; i++) {
			TaskDo task = createRandomTaskDo();
			expected.add(task);

			TaskEntity entity = TaskEntity.fromDo(task);
			FakeRepository.instance().insertOrUpdate(entity);
		}

		TaskDo[] result = 
				target("todos")
				.request()
				.get(TaskDo[].class);
		
		List<TaskDo> actual = Arrays.asList(result);

		assertTrue(expected.containsAll(actual) && actual.containsAll(expected));
	}
	
	// +++GET Item+++
	@Test
	public void get_item_expect_404() throws StorageException {

		FakeRepository.instance().clear();

		String id = UUID.randomUUID().toString();
		
		Response actual = 
				target("todos/" + id)
					.request()
					.get();

		assertEquals(
				Response.Status.NOT_FOUND.getStatusCode(),
				actual.getStatus());
	}



	@Test
	public void get_item_expect_200_and_content() throws StorageException {

		FakeRepository.instance().clear();

		TaskDo expected = createRandomTaskDo();

		TaskEntity entity = TaskEntity.fromDo(expected);
		FakeRepository.instance().insertOrUpdate(entity);

		TaskDo actual = 
				target("todos/" + expected.getId())
				.request()
				.get(TaskDo.class);

		assertEquals(expected, actual);
	}

	
	// +++PUT List+++
	public void put_list_expect_404() {

		TaskDo task = createRandomTaskDo();
		
		Response actual = 
				target("todos")
					.request()
					.put(Entity.entity(task, MediaType.APPLICATION_JSON_TYPE));

		assertEquals(
				Response.Status.NOT_FOUND.getStatusCode(),
				actual.getStatus());
	}

	@Test
	public void put_item_expect_200_and_content() throws StorageException {

		FakeRepository.instance().clear();

		TaskDo expected = createRandomTaskDo();
		TaskEntity entity = TaskEntity.fromDo(expected);
		FakeRepository.instance().insertOrUpdate(entity);
		
		expected.setText(getRandomString());

		TaskDo actual = 
				target("todos/" + expected.getId())
				.request()
				.put(Entity.entity(expected, MediaType.APPLICATION_JSON_TYPE))
				.readEntity(TaskDo.class);

		assertEquals(expected, actual);
	}

	@Test
	public void put_item_expect_404() throws StorageException {

		FakeRepository.instance().clear();

		TaskDo expected = createRandomTaskDo();
		TaskEntity entity = TaskEntity.fromDo(expected);
		FakeRepository.instance().insertOrUpdate(entity);
		
		String missingId = UUID.randomUUID().toString(); 

		Response actual = 
				target("todos/" + missingId)
				.request()
				.put(Entity.entity(expected, MediaType.APPLICATION_JSON_TYPE));

		assertEquals(
				Response.Status.NOT_FOUND.getStatusCode(),
				actual.getStatus());
	}

	
	//+++POST list+++
	@Test
	public void post_list_expect_201_and_location_link() {

		FakeRepository.instance().clear();

		TaskDo expected = createRandomTaskDo();

		Response actual = 
				target("todos")
				.request()
				.post(Entity.entity(expected, MediaType.APPLICATION_JSON_TYPE));

		assertEquals(
				Response.Status.CREATED.getStatusCode(),
				actual.getStatus());

		String[] segments = actual.getLocation().getPath().split("/");
		String id = segments[segments.length - 1];

		assertTrue(FakeRepository.instance().contains(id));
	}

	public void post_item_expect_404() {
		FakeRepository.instance().clear();

		TaskDo expected = createRandomTaskDo();

		Response actual = 
				target("todos" + expected.getId())
				.request()
				.post(Entity.entity(expected, MediaType.APPLICATION_JSON_TYPE));

		assertEquals(
				Response.Status.NOT_FOUND.getStatusCode(),
				actual.getStatus());
	
	}

	//+++DELETE list+++
	
	public void delete_list_expect_404() {
		Response actual = 
				target("todos")
				.request()
				.delete();

		assertEquals(
				Response.Status.NOT_FOUND.getStatusCode(),
				actual.getStatus());
	
	}

	@Test
	public void delete_missing_item_expect_404() throws StorageException {

		FakeRepository.instance().clear();

		String missingId = UUID.randomUUID().toString();

		Response actual = target("todos/" + missingId).request().delete();

		assertEquals(
				Response.Status.NOT_FOUND.getStatusCode(),
				actual.getStatus());
	}

	
	@Test
	public void delete_item_expect_200_and_content() throws StorageException {

		FakeRepository.instance().clear();

		TaskDo expected = createRandomTaskDo();
		TaskEntity entity = TaskEntity.fromDo(expected);
		FakeRepository.instance().insertOrUpdate(entity);

		Response actual = 
				target("todos/" + expected.getId())
				.request()
				.delete();

		assertEquals(Response.Status.OK.getStatusCode(), actual.getStatus());
		
		TaskDo content = actual.readEntity(TaskDo.class);
		assertEquals(expected, content);
	}


	public TaskDo createRandomTaskDo() {
		return createRandomTaskDo(getRandomString());
	}

	public TaskDo createRandomTaskDo(String text) {
		TaskDo task = new TaskDo();
		task.setId(UUID.randomUUID().toString());
		task.setText(text);
		return task;
	}

	private static String getRandomString() {
		return getRandomString(32);
	}

	private static String getRandomString(int maxlength) {
		String result = "";
		int i = 0, n = 0, min = 33, max = 122;
		while (i < maxlength) {
			n = (int) (Math.random() * (max - min) + min);
			if (n >= 33 && n < 123) {
				result += (char) n;
				++i;
			}
		}
		return (result);
	}
}
