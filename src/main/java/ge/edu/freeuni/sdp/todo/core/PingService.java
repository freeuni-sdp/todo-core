package ge.edu.freeuni.sdp.todo.core;

import ge.edu.freeuni.sdp.todo.data.RepositoryFactory;
import ge.edu.freeuni.sdp.todo.data.TaskEntity;
import ge.edu.freeuni.sdp.todo.data.Repository;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.ws.rs.*;
import javax.ws.rs.core.*;

@Path("ping")
@Consumes( { MediaType.APPLICATION_JSON})
@Produces( { MediaType.APPLICATION_JSON})
public class PingService {

  @GET
  public Response get() {
    return Response.ok().build();
  }

}
