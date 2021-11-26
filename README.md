# SOEN 423 Project

## `util` package

### `Constants` class 

TIMEOUT, NUMBER_OF_REPLICAS and FRONTEND_PORT added for reference.

## `Frontend` package 

### `Frontend` class 

Implemented: listening for incoming UDP messages

Implemented: placing messages in a holding queue while waiting for replies from the replicas

Implemented: determine if a valid response is received before the timeout, or if the response has timed out

_**TODO:**_ Return results to the client once a valid response is determined

_**TODO:**_ Signal the Primary RM in case of a timeout 

_**TODO:**_ Forward client requests to the sequencer


## `mock` package 

Used for testing

### `MockRM` class 

Example of sending a response from the RM to the Frontend: 

```java
Response re = new Response(responseId, new RoomRecord(123, "12-12-2020", "timeslotstring"));
Udp.send(Constants.FRONTEND_PORT, re);
```
