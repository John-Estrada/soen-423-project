package util

import java.io.*
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress
import java.net.SocketTimeoutException
import kotlin.concurrent.thread


class Udp {
    companion object {

        private fun toByteArray(obj: Serializable): ByteArray {
            val bos = ByteArrayOutputStream()
            val out = ObjectOutputStream(bos)
            out.writeObject(obj)
            out.flush()
            return bos.toByteArray()
        }

        @JvmStatic
        fun <T> toObject(bytes: ByteArray): T {
            val stream = ObjectInputStream(ByteArrayInputStream(bytes))
            val obj = stream.readObject() as T
            stream.close()
            return obj;
        }

        @JvmStatic
        fun <T> send(port: Int, obj: Serializable): T {
            return send<T>(port, obj, null);
        }

        @JvmStatic
        fun <T> send(port: Int, obj: Serializable, timeout: Int? = null): T {
            try {
                val clientSocket = DatagramSocket()

                if (timeout != null) {
                    clientSocket.soTimeout = timeout
                }

                val ipAddress = InetAddress.getByName("localhost")

                val sendingBytes = toByteArray(obj);
                val receivingDataBuffer = ByteArray(16 * 1024)
                val sendingPacket = DatagramPacket(sendingBytes, sendingBytes.size, ipAddress, port)
                clientSocket.send(sendingPacket)

                val receivingPacket = DatagramPacket(receivingDataBuffer, receivingDataBuffer.size)
                clientSocket.receive(receivingPacket)
                clientSocket.close()

                return toObject(receivingPacket.data)
            } catch (e: SocketTimeoutException) {
                throw e;
            } catch (e: Exception) {
                e.printStackTrace()
                throw e
            }
        }

        @JvmStatic
        fun <T> listen(port: Int, handler: (request: T) -> Serializable) {
            thread { listenSync<T>(port, handler) }
        }

        @JvmStatic
        fun <T> listenSync(port: Int, handler: (request: T) -> Serializable) {
            val socket = DatagramSocket(port)
            socket.soTimeout = 1000;
            while (true) {
                // Get request
                var bytes = ByteArray(16 * 1024)
                val inputPacket = DatagramPacket(bytes, bytes.size)
                try {
                    socket.receive(inputPacket)
                } catch (e: SocketTimeoutException) {
                    if (Thread.interrupted()) {
                        socket.close();
                        break;
                    } else {
                        Thread.yield();
                        continue;
                    }
                }
                val request = toObject<T>(inputPacket.data)

                // Handle request
                val response = handler.invoke(request)

                // Send response
                bytes = toByteArray(response);
                val outputPacket = DatagramPacket(bytes, bytes.size, inputPacket.address, inputPacket.port)
                socket.send(outputPacket);
            }
        }
    }
}