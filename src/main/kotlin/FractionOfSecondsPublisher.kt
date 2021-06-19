import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import java.text.DecimalFormat
import java.util.*




suspend fun main(args: Array<String>) {
    val client = MqttClient { "Fractions" }
    val formatter = DecimalFormat("00.00")
    Calendar.getInstance() // It's quite heavy to initialize

    val fractionFlow =
        flow {
            while(true) { emit(currentSecond) }
        }
        .map { second -> second / 2f }
        .map { second -> formatter.format(second).replace(",", ".") }
        .onEach { delay(5_000) }


    client.useSuspending {
        fractionFlow.collect { msg ->
            println("Sending $msg to ${Constants.SECONDS_FRACTION_TOPIC}")
            send(Constants.SECONDS_FRACTION_TOPIC, msg)
        }
    }
}