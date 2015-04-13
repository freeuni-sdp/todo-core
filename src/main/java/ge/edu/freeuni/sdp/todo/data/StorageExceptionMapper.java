package ge.edu.freeuni.sdp.todo.data;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import com.microsoft.azure.storage.StorageException;

@Provider
public class StorageExceptionMapper implements ExceptionMapper<StorageException> {

	    @Override
	    public Response toResponse(StorageException ex) {
	        return Response
	        		.status(400)
	        		.entity(ex.getMessage())
	        		.type("text/plain")
	                .build();
	    }
}
