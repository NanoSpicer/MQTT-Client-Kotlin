import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import java.util.*




suspend fun main(args: Array<String>) {
    val client = MqttClient { "Seconds" }
    Calendar.getInstance() // It's quite heavy to initialize

    val secondsFlow =
        flow {
            while(true) { emit(currentSecond) }
        }
        .map { second -> second.toString() }
        .onEach { delay(1_000) }


    client.useSuspending {

        secondsFlow.collect { msg ->
            println("Sending $msg to ${Constants.SECONDS_TOPIC}")
            send(Constants.SECONDS_TOPIC, msg)
        }
    }
}