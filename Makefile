up:
	docker-compose -f docker-compose.yml up -d
	sleep 2
	docker cp data/ddl.sql postgrescredit:/var/lib/postgresql/data
	sleep 2
	docker exec postgrescredit psql -h localhost -U admin -d postgres -a -f ./var/lib/postgresql/data/ddl.sql

down:
	docker-compose down
reset:
	make down
	sleep 2
	make up
