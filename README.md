# Description

Look into `./Description.odt`

## How to run

- Create container:
```
docker run -d --name postgres_ha -p 5432:5432 -e POSTGRES_USER=username -e POSTGRES_PASSWORD=password -v /home/cosiska/Postgres:/postgres postgres
```
- Run `cz.cvut.fit.tjv.hospital_appointments.RestServer`