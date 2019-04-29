**Step by Step Guide to run the program:**

 - Make sure that at the terminal you are in is at the root folder Shop/ before running the docker commands
 - At bin/mongod.cfg, change bindIp to 0.0.0.0 then change the mongo.server value at mongo.properties to host ip
 - If you encounter the error `docker /usr/bin/env 'sh\r'...` when at the gradlew command then use the `dos2unix gradlew` command to update the eol of the gradlew file

```
 docker build -f Docker/Dockerfile-platform -t platform:1.0 .
 docker build -f Docker/Dockerfile-update -t update .
 docker run -d -p 8080:8080 update
 # access http://localhost:8080
```
 - if you want to check the contents inside a container the use the following command from bash/powershell
	`docker exec -it <container id> bash`
 - you only need to run the first docker build once to create the platform image. The first image will contain all necessary tools (OS, Server, libraries) and will be used by the update image for every code changes.	

**Debugging Docker errors:**

1. 
If you encounter an error when running a container or creating a container/image the use the following command
`docker system prune`.

2. 
If the error encountered is caused by Docker itself (unable to start, error during restart process) or using the command from step 1 does not work. Close Docker Desktop and then restart the docker service from Task Manager > Services.
Find a service with a name or description that has 'docker' and restart it. You can now try to reopen Docker Desktop.

If error persists then restart the computer. There is most likely a related service or process from Docker that did not close/work correctly and a full system restart should address this

3. 
If the problem still persists after doing step 1 and 2 then you can now google and seek answers online