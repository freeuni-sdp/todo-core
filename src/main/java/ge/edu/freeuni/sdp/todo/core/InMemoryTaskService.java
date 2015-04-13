package ge.edu.freeuni.sdp.todo.core;


import javax.ws.rs.Path;

import ge.edu.freeuni.sdp.todo.data.InMemoryRepository;
import ge.edu.freeuni.sdp.todo.data.Repository;

import com.microsoft.azure.storage.StorageException;

@Path("memtodos")
public class InMemoryTaskService extends TaskService {
	
	@Override
	public Repository getRepository() throws StorageException {
		return InMemoryRepository.instance();
	}

}
