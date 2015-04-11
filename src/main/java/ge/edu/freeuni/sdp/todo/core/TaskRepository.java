package ge.edu.freeuni.sdp.todo.core;

import com.microsoft.azure.storage.StorageException;

public interface TaskRepository {

	public abstract void insertOrUpdate(TaskEntity task)
			throws StorageException;

	public abstract void delete(String id) throws StorageException;

	public abstract TaskEntity find(String id) throws StorageException;

	public abstract Iterable<TaskEntity> getAll();

}