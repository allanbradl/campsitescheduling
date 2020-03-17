# Scheduling

Campsite scheduling application.

## Startup

mvn spring-boot:run

## Concurrency testing

Concurrency tests throw that when firing 10 simultaneous reservations requests for the same dates, 1 of them gets created but the rest (9) get an error.