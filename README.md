# SOEN 423 Project

## `util` package

### `Udp` class

The `util.Udp` class lets you send and receive UDP messages. The messages must
implement `Serializable`.

```java
import util.Udp;

// Sending a message
String response = Udp.send(8080, "Hello");

// Receiving messages
Udp.listen(9000, (request) -> {
    return handleRequest((String) request);
});
```

## `rm` package

The rm package contains what you need to initialize a new replica managers and
send messages to it.

### `ReplicaManager` class

The `rm.ReplicaManager` class lets you start RM processes. The RM needs a port
and an ID.

```java
import rm.ReplicaManager;

// Starting 4 RMs
int port = 9000;
int id = 0;
Thread[] threads = new Thread[4];
while (id < 4) {
    threads[id] = ReplicaManager.startProcess(port++, id++);
}
```

### `Request` class

You can send commands to the Replica Manager by using a `rm.Request` class and
the other classes from the `rm` package:

```java
import udp.Util;
import rm.Request; 

// Create request with unique ID
int requestId = 0;
Request request = new Request(requestId++, new CreateRoom(
    "KVLA1234",
    123,
    "12-12-2012",
    new String[]{"10:00-11:00"}));

// Send request to rm
Udp.send(8080, request);
```

_**TODO:**_ RM should return response to the FE.

_**TODO:**_ Complete process ressurection sequence.

_**TODO:**_ Figure out how to create booking ID (I'm tempted to create a global
repository that generates booking ids)

_**TODO:**_ Figure out how to avoid port collision (I'm tempted to YOLO it, use
ephemeral ports and cross fingers for no collisions)?
