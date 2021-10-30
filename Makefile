postgres:
	docker run --rm  --name postgres -d -p 5432:5432 -e POSTGRES_PASSWORD=dupa.8 postgres:12.8

push:
	docker build . --tag jbosak98/dockerhub:stock-market-game_backend-secure
	docker image push jbosak98/dockerhub:stock-market-game_backend-secure
pull:
	docker pull jbosak98/dockerhub:stock-market-game_backend-secure
	docker pull jbosak98/dockerhub:stock-market-game_backend-insecure
