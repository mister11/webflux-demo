# General idea

Idea of this README is to list all the changes needed due to deprecations, API changes or
anything else that might break this application.

# Changelog

## Deprecation of `DatabaseClient`

Since version 1.2 of Spring Data R2DBC, `DatabaseClient` has been deprecated and
instead, we have to use `R2dbcEntityTemplate`. API has been slightly changes and improved,
but it requires us to have ID property in our database models.

Because of that, I've added ID property to `Language` class and added few annotations
to take advantage of new APIs which can be seen in `LanguagesIntegrationTest`.

Of course, since database model changed, I had to update `webflux-demo-database.sql` 
that's used in the [initial setup](https://github.com/mister11/webflux-demo#database).
