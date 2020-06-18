# Spring WebFlux demo

Accompanying project for Spring WebFlux introduction [blog post](https://mister11.github.io/posts/spring_webflux/).

Project shows how to migrate Spring MVC app to a Spring WebFlux one and code for each step is in corresponding branch
linked in the blog post.

Master branch contains basic Spring MVC application.

## Database

To run a database, execute:

```bash
# requires Docker Compose installed locally
docker-compose up
```

This will run a Postgres database on port 5432 with username `postgres` and password `postgres`. To fill database
with data used in blog post, execute:

```bash
# requires PostgreSQL database installed locally
psql -h localhost -p 5432 -U postgres -d webflux-demo-database < webflux-demo-database.sql
```

in the root folder of the project.
