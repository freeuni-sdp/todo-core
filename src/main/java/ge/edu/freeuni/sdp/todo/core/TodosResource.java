package ge.edu.freeuni.sdp.todo.core;

import java.net.URI;
import java.util.ArrayList;
import java.util.UUID;

import javax.ws.rs.*;
import javax.ws.rs.core.*;

import com.microsoft.azure.storage.StorageException;

@Path("todos")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class TodosResource {

    @Context
    private UriInfo uriInfo;
    
    public TaskRepository getRepository() throws StorageException {
		return RepositoryFactory.create();
	}
    
	@POST
    public Response create(TaskDo task) throws StorageException {
		String uniqueId = UUID.randomUUID().toString();
		task.setId(uniqueId);
		TaskEntity entity = TaskEntity.fromDo(task);
		getRepository()
			.insertOrUpdate(entity);
		
		URI createdUri = 
				uriInfo
					.getAbsolutePathBuilder()
					.path(uniqueId)
					.build();
		
		return Response.created(createdUri).build();
	}

	@GET
	public TaskDo[] read() throws StorageException {
		final ArrayList<TaskDo> result = new ArrayList<TaskDo>();
		for(TaskEntity entity : getRepository().getAll()) {
			result.add(entity.toDo());
		}
		TaskDo[] array = new TaskDo[result.size()]; 
		return result.toArray(array);
	}

	@GET
	@Path("{id}")
	public TaskDo read(@PathParam("id") String id) throws StorageException {
		return  getRepository()
					.find(id)
					.toDo();
	}

	@PUT
	public Response update(@PathParam("id") String id, TaskDo task) throws StorageException {
		task.setId(id);
		TaskEntity entity = TaskEntity.fromDo(task);
		getRepository()
			.insertOrUpdate(entity);
		return Response.ok().build();
	}

	@DELETE
	@Path("{id}")
	public Response delete(@PathParam("id") String id) throws StorageException {
		getRepository()
				.delete(id);
		return Response.ok().build();
	}
}