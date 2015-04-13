package ge.edu.freeuni.sdp.todo.core;

import ge.edu.freeuni.sdp.todo.data.RepositoryFactory;
import ge.edu.freeuni.sdp.todo.data.TaskEntity;
import ge.edu.freeuni.sdp.todo.data.Repository;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.ws.rs.*;
import javax.ws.rs.core.*;

import com.microsoft.azure.storage.StorageException;

@Path("todos")
@Consumes( { MediaType.APPLICATION_JSON})
@Produces( { MediaType.APPLICATION_JSON})
public class TaskService {

	/*
	HTTP  |              /todos   	            	|            /todos/{ID}
	------|-----------------------------------------|----------------------------------------
	GET   | 200 (OK), list of tasks. 				| 200 (OK), single task. 404 (Not Found).
	PUT   | 404 (Not Found), N/A 					| 200 (OK). 404 (Not Found).
	POST  | 201 (Created),  Location: /todos/{ID} 	| 404 (Not Found).
	DELETE| 404 (Not Found) 						| 200 (OK). 404 (Not Found)
    */	

	
	@Context
	private UriInfo uriInfo;

	public Repository getRepository() throws StorageException {
		return RepositoryFactory.create();
	}

	/**
	 * 
	 * @return 200 (OK), list of tasks.
	 * @throws StorageException
	 */
	@GET
	public List<TaskDo> read() throws StorageException {
		final ArrayList<TaskDo> result = new ArrayList<TaskDo>();
		for (TaskEntity entity : getRepository().getAll()) {
			result.add(entity.toDo());
		}
		return result;
	}

	/**
	 * 
	 * @param id
	 * @return 200 (OK), single task. 404 (Not Found).
	 * @throws StorageException
	 */
	@GET
	@Path("{id}")
	public TaskDo read(@PathParam("id") String id) throws StorageException {
		final TaskEntity entity = getRepository().find(id);
		if (entity==null) throw new WebApplicationException(404);
		return entity.toDo();
	}

	/**
	 * 
	 * @param id
	 * @param task
	 * @return 200 (OK). 404 (Not Found).
	 * @throws StorageException
	 */
	@PUT
	@Path("{id}")
	public TaskDo update(@PathParam("id") String id, TaskDo task) throws StorageException {
		final TaskEntity entity = getRepository().find(id);
		if (entity==null) throw new WebApplicationException(404);
		task.setId(id);
		getRepository().insertOrUpdate(TaskEntity.fromDo(task));
		return task;
	}

	/**
	 * 
	 * @param task
	 * @return 201 (Created),  Location: /todos/{ID}
	 * @throws StorageException
	 */
	@POST
	public Response create(TaskDo task) throws StorageException {
		final String uniqueId = UUID.randomUUID().toString();
		task.setId(uniqueId);
		final TaskEntity entity = TaskEntity.fromDo(task);
		getRepository().insertOrUpdate(entity);

		final URI createdUri = uriInfo.getAbsolutePathBuilder().path(uniqueId)
				.build();

		return Response.created(createdUri).build();
	}


	/**
	 * 200 (OK). 404 (Not Found)
	 * @param id
	 * @return
	 * @throws StorageException
	 */
	@DELETE
	@Path("{id}")
	public Response delete(@PathParam("id") String id) throws StorageException {
		final TaskEntity entity = getRepository().delete(id);
		if (entity==null) throw new WebApplicationException(404);
		return Response.ok(entity.toDo()).build();
	}
}