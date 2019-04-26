Step by Step Guide to run the program:

 - Make sure that at the terminal you are in is at the root folder shop/ before running the docker commands
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

Debugging Docker errors:
1. If you encounter an error when running a container or creating a container/image the use the following command
`docker system prune`

2. If the error encountered is caused by Docker itself (unable to start, error during restart process) or using the command from step 1 does not work
Then restart the computer, there is most likely a service or process from Docker that did not close/work correctly and a full system restart should address this

3. If the problem still persists after doing step 1 and 2 then you can now google and seek answers online