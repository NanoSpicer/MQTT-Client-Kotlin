import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.4.30-M1"
    application
}

group = "me.miquel"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
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
    implementation("org.eclipse.paho:org.eclipse.paho.client.mqttv3:1.2.5")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.4.3")
    testImplementation(kotlin("test-junit"))
}

tasks.test {
    useJUnit()
}

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "1.8"
}

application {
    mainClassName = "MainKt"
}