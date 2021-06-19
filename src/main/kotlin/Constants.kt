import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import org.eclipse.paho.client.mqttv3.MqttClient
import org.eclipse.paho.client.mqttv3.MqttMessage
import java.util.*


val currentSecond: Int
    get() = Calendar.getInstance()[Calendar.SECOND]

inline fun MqttClient(idProvider: () -> String) = MqttClient(Constants.BROKER_URI, idProvider())

fun MqttClient.subscribeAsFlow(topic: String): Flow<Pair<String, MqttMessage>> = callbackFlow {
    subscribe(topic) { top, msg -> offer(top to msg) }
    awaitClose { unsubscribe(topic) }
}

inline fun <R> MqttClient.use(crossinline operations: MqttClient.() -> R): R =
    kotlin.run { connect(); operations() }.also { disconnect() }

suspend inline fun <R> MqttClient.useSuspending(crossinline operations: suspend MqttClient.() -> R): R =
    kotlin.run { connect(); operations() }.also { disconnect() }


fun MqttClient.send(topic: String, msg: Any) =
    kotlin
        .runCatching {
            val mqttMsg = MqttMessage(msg.toString().toByteArray())
            publish(topic, mqttMsg)
        }
        .isSuccess





object Constants {
    const val BROKER_URI = "tcp://raspi4.local:1883"
    const val SECONDS_TOPIC = "sensors/virtual1"
    const val SECONDS_FRACTION_TOPIC = "sensors/virtual2"
}