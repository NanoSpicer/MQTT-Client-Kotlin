import kotlinx.coroutines.flow.collect


suspend fun main(args: Array<String>) {
    val topic = args.firstOrNull() ?: return println("Provide 1 topic as an argument")
    val client = MqttClient { "Debug Subscriber" }

    client.useSuspending {
        println("Listening to $topic")
        subscribeAsFlow(topic).collect { (top, msg) ->
            val payload = String(msg.payload)
            println("$top $payload")
        }
    }

}