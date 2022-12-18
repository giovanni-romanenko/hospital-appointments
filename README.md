# Description

Look into `./Description.odt`

## How to run

- Create container:
```
docker run -d --name postgres_ha -p 5432:5432 -e POSTGRES_USER=username -e POSTGRES_PASSWORD=password -v /local/path:/postgres postgres
```
- Run `cz.cvut.fit.tjv.hospital_appointments.RestServer`

## How to generate documentation

- Turn on rest server
- Open http://localhost:8080/v3/api-docs.yaml