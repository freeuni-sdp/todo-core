todo-core microservice webapi
------------------------------

[![Build Status](https://travis-ci.org/freeuni-sdp/todo-core.svg?branch=master)](https://travis-ci.org/freeuni-sdp/todo-core)


Base IRI: http://freeuni-sdp-todo-core.azurewebsites.net/todo-core/

Resource (URI)	| POST (create) |	GET (read) |	PUT (update) |	DELETE (destroy)|
----------------|---------------|------------|---------------|
/todos |	create new task |	list tasks |	N/A (update all) |	N/A (destroy all)
/todos/1 |	error |	show task ID 1 |	update task ID 1 |	destroy task ID 1
