# Scheduling

Campsite scheduling application.

## Startup

mvn spring-boot:run

## Concurrency testing

Concurrency tests throw that when firing 10 simultaneous reservations requests for the same dates, 1 of them gets created but the rest (9) get an error.

## Backend Endpoints / Usage

### GET: localhost:8080/campsite-availability/

### GET: localhost:8080/campsite-availability/{start}/{end}

### POST: localhost:8080/reservation

```json
{
    "startDate": "2020-04-29",
    "endDate": "2020-04-30",
    "arrivalDate": "2020-04-17",
    "departureDate": "2020-04-22",
    "email": "test@email.com"
}
```

### PATCH: localhost:8080/reservation

```json
{
    "id": 1,
    "startDate": "2020-04-29",
    "endDate": "2020-04-30",
    "arrivalDate": "2020-04-17",
    "departureDate": "2020-04-22",
    "email": "test@email.com"
}
```
### DELETE: localhost:8080/reservation/{id}



