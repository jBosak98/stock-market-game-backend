postgres:
	docker run --rm  --name postgres -d -p 5432:5432 -e POSTGRES_PASSWORD=password postgres
backend:
	docker run --rm -u gradle -v "${PWD}":/home/gradle/project -w /home/gradle/project gradle:6.6.1-jdk11 /bin/bash -c  "cd /home/gradle/project && gradle build"

