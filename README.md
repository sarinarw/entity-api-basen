# entity-api-basen
Simple REST API programming exercise for BaseN using Vert.x

The data is stored per instance of the service. If the service is reset, the data is also reset.
This temporary "database" always has 2 pre-inserted entities. One is the sub-entity of the other.

Circular entity relationships are not allowed.

### Run
`cd` into `entity-api-basen` folder

then: `mvn compile exec:java`

### API Examples
The only concrete type of Entity is PERSON.

- list Entity entries
```
curl -X GET "http://localhost:8080/entity"
```
- read an Entity
```
curl -X GET "http://localhost:8080/entity?id=<entityId>"
```
- send an Entity (to root, or attached to another entity)
  - create a new entity with nothing attached
  ```
  curl -X POST "http://localhost:8080/entity" --data '{"data":{"BIRTHDATE":{"year":2010,"month":"JANUARY","chronology":{"id":"ISO","calendarType":"iso8601"},"era":"CE","dayOfYear":1,"dayOfWeek":"FRIDAY","leapYear":false,"monthValue":1,"dayOfMonth":1},"FIRST_NAME":"BobbyV2"}, "entityType": "PERSON"}'
  ```
  - create a new entity with some pre-existing entities attached
  ```
  curl -X POST "http://localhost:8080/entity" --data '{"data":{"subEntityIds":["..."], "BIRTHDATE":{"year":2010,"month":"JANUARY","chronology":{"id":"ISO","calendarType":"iso8601"},"era":"CE","dayOfYear":1,"dayOfWeek":"FRIDAY","leapYear":false,"monthValue":1,"dayOfMonth":1},"FIRST_NAME":"BobbyV2"}, "entityType": "PERSON"}'
  ```
  - attach pre-existing entities to another pre-existing entity
  ```
  curl -X POST "http://localhost:8080/entity" --data '{"entityId":"...", "subEntityIds":["..."]}'
  ```

