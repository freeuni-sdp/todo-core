package ge.edu.freeuni.sdp.todo.data;

import java.net.URISyntaxException;
import java.security.InvalidKeyException;

import com.microsoft.azure.storage.*;
import com.microsoft.azure.storage.table.*;

public class RepositoryFactory {
	
	public static Repository create() throws StorageException {
		return new CloudRepository(getTable());
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
}
