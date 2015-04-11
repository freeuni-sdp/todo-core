package ge.edu.freeuni.sdp.todo.core;

import java.net.URISyntaxException;
import java.security.InvalidKeyException;

import com.microsoft.azure.storage.*;
import com.microsoft.azure.storage.table.*;

public class TaskRepository {

	private CloudTable table;

	public TaskRepository(CloudTable table) {
		this.table = table;
	}

	public static TaskRepository create() throws StorageException {
		return new TaskRepository(getTable());
	}
	
	private static CloudTable getTable() throws StorageException {

		final String storageConnectionString = "DefaultEndpointsProtocol=http;"
				+ "AccountName=freeunisdptodo;"
				+ "AccountKey=+UKHsHFQUWDjoHT1S7q4Ivc1phivLmXwWESvpcRCCJwhs1BnShkaFOOQs+BmI4XWtNnyg78S6ovbD2J5QCKxsQ==";

		CloudStorageAccount storageAccount;
		try {
			storageAccount = CloudStorageAccount.parse(storageConnectionString);
		} catch (InvalidKeyException | URISyntaxException e) {
			e.printStackTrace();
			return null;
		}
		
		CloudTableClient tableClient = storageAccount.createCloudTableClient();
		final String tableName = "tasks";
		CloudTable cloudTable;
		try {
			cloudTable = new CloudTable(tableName, tableClient);
		} catch (URISyntaxException e) {
			e.printStackTrace();
			return null;
		}
		cloudTable.createIfNotExists();
		return cloudTable;
	}

	public void insertOrUpdate(TaskEntity task) throws StorageException {
		TableOperation operation = TableOperation.insertOrReplace(task);
		table.execute(operation);
	}

	public void delete(String id) throws StorageException {
		TaskEntity task = find(id);
		TableOperation operation = TableOperation.delete(task);
		table.execute(operation);
	}

	public TaskEntity find(String id) throws StorageException {
		TaskEntityId tskId = new TaskEntityId(id);
		TableOperation operation = 
				TableOperation.retrieve(
					tskId.getPartitionKey(), 
					tskId.getRowKey(), 
					TaskEntity.class);
		return table.execute(operation).getResultAsType();
	}
	
	public Iterable<TaskEntity> getAll() {
		 TableQuery<TaskEntity> query =
			       TableQuery.from(TaskEntity.class);
		 return table.execute(query);
	}
}