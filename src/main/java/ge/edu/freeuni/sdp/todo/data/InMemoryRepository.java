package ge.edu.freeuni.sdp.todo.data;

import java.util.HashMap;
import java.util.Map;

import com.microsoft.azure.storage.StorageException;

public class InMemoryRepository implements Repository {

	private static InMemoryRepository instance;
	protected Map<String, TaskEntity> map;

	public static InMemoryRepository instance() {
		if (instance==null) {
			instance = new InMemoryRepository(new HashMap<String, TaskEntity>());
		}
		return instance;
	}

	public InMemoryRepository(Map<String, TaskEntity> map) {
		super();
		this.map = map;
	}

	@Override
	public void insertOrUpdate(TaskEntity task) throws StorageException {
	    TaskEntityId id = new TaskEntityId(task.getPartitionKey(), task.getRowKey());
		map.put(id.getId(), task);
	}

	@Override
	public TaskEntity delete(String id) throws StorageException {
		return map.remove(id);
	}

	@Override
	public TaskEntity find(String id) throws StorageException {
		return map.get(id);
	}

	@Override
	public Iterable<TaskEntity> getAll() {
		return map.values();
	}

}