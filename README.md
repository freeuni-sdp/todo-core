todo-core microservice webapi
------------------------------

[![Build Status](https://travis-ci.org/freeuni-sdp/todo-core.svg?branch=master)](https://travis-ci.org/freeuni-sdp/todo-core)


Base IRI: http://freeuni-sdp-todo.herokuapp.com/webapi/todos

HTTP  |  /todos   |  /todos/{ID}
------|-----------|---------
GET   | 200 (OK), list of tasks. | 200 (OK), single task. 404 (Not Found), if ID not found or invalid.
PUT   | 404 (Not Found), N/A | 200 (OK). 404 (Not Found), if ID not found or invalid.
POST  | 201 (Created), 'Location' header with link to /todos/{ID} containing new ID. | 404 (Not Found).
DELETE| 404 (Not Found) | 200 (OK). 404 (Not Found), if ID not found or invalid.
