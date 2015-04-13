import com.microsoft.azure.storage.StorageException;

import ge.edu.freeuni.sdp.todo.core.TaskService;
import ge.edu.freeuni.sdp.todo.data.Repository;


public class FakeTaskService extends TaskService {
	@Override
	public Repository getRepository() throws StorageException {
		return FakeRepository.instance();
	}
}
