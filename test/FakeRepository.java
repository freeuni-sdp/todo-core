
import ge.edu.freeuni.sdp.todo.core.TaskEntity;
import ge.edu.freeuni.sdp.todo.core.TaskEntityId;
import ge.edu.freeuni.sdp.todo.core.TaskRepository;

import java.util.HashMap;
import java.util.Map;

import com.microsoft.azure.storage.StorageException;

public class FakeRepository implements TaskRepository {

	private static FakeRepository instance;
	private Map<String, TaskEntity> map;

	public static FakeRepository instance() {
		if (instance==null) {
			instance = new FakeRepository(new HashMap<String, TaskEntity>());
		}
		return instance;
	}
	
	public FakeRepository(Map<String, TaskEntity> map) {
		this.map = map;
	}
	
	public boolean contains(String id) {
		return map.containsKey(id);
	}
	
	@Override
	public void insertOrUpdate(TaskEntity task) throws StorageException {
	    TaskEntityId id = new TaskEntityId(task.getPartitionKey(), task.getRowKey());
		map.put(id.getId(), task);
	}

	@Override
	public void delete(String id) throws StorageException {
		map.remove(id);
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
