package ge.edu.freeuni.sdp.todo.data;

import com.microsoft.azure.storage.*;
import com.microsoft.azure.storage.table.*;

public class CloudRepository implements Repository {

	private CloudTable table;

	public CloudRepository(CloudTable table) {
		this.table = table;
	}

	@Override
	public void insertOrUpdate(TaskEntity task) throws StorageException {
		TableOperation operation = TableOperation.insertOrReplace(task);
		table.execute(operation);
	}

	
	@Override
	public TaskEntity delete(String id) throws StorageException {
		TaskEntity task = find(id);
		if (task==null) return null;
		TableOperation operation = TableOperation.delete(task);
		table.execute(operation);
		return task;
	}

	@Override
	public TaskEntity find(String id) throws StorageException {
		TaskEntityId tskId = new TaskEntityId(id);
		TableOperation operation = 
				TableOperation.retrieve(
					tskId.getPartitionKey(), 
					tskId.getRowKey(), 
					TaskEntity.class);
		return table.execute(operation).getResultAsType();
	}
	
	@Override
	public Iterable<TaskEntity> getAll() {
		 TableQuery<TaskEntity> query =
			       TableQuery.from(TaskEntity.class);
		 return table.execute(query);
	}
}