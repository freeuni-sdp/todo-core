

import ge.edu.freeuni.sdp.todo.core.TaskRepository;
import ge.edu.freeuni.sdp.todo.core.TodosResource;

import com.microsoft.azure.storage.StorageException;

public class FakeResource extends TodosResource {
	
	@Override
	public TaskRepository getRepository() throws StorageException {
		return FakeRepository.instance();
	}

}
