import java.util.UUID;

import ge.edu.freeuni.sdp.todo.core.*;

import javax.ws.rs.client.*;
import javax.ws.rs.core.*;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.*;

import com.microsoft.azure.storage.StorageException;

public class TestTodoResource extends JerseyTest  {

	@Override
    protected Application configure() {
        return new ResourceConfig(FakeResource.class);
    }
	
	@Test
	public void create_and_ensure_contained_in_repository() {
	
		TaskDo task = new TaskDo();
		task.setText("Hello, world.");

		Response actual = 
					target("todos")
					.request()
					.post(Entity.entity(task, MediaType.APPLICATION_JSON_TYPE));

		Assert.assertEquals(Response.Status.CREATED.getStatusCode(), actual.getStatus());
		
		String[] segments = 
				actual
					.getLocation()
					.getPath()
					.split("/");
		String id = segments[segments.length-1];
		
		Assert.assertTrue(FakeRepository.instance().contains(id));
	}
	
	
	@Test
	public void add_to_repository_and_get() throws StorageException {
	
		TaskDo expected = new TaskDo();
		expected.setId(UUID.randomUUID().toString());
		expected.setText("Hello, world!");
		
		TaskEntity entity = TaskEntity.fromDo(expected);
		FakeRepository.instance().insertOrUpdate(entity);
		
		TaskDo actual = 
					target("todos/"+expected.getId())
					.request()
					.get(TaskDo.class);

		Assert.assertEquals(expected, actual);
	}
	
	
	@Test
	public void add_to_repository_and_delete() throws StorageException {
	
		TaskDo expected = new TaskDo();
		expected.setId(UUID.randomUUID().toString());
		expected.setText("Hello, world!");
		
		TaskEntity entity = TaskEntity.fromDo(expected);
		FakeRepository.instance().insertOrUpdate(entity);
		
		Response actual = 
					target("todos/"+expected.getId())
					.request()
					.delete();

		Assert.assertEquals(Response.Status.OK.getStatusCode(), actual.getStatus());
	}
}
