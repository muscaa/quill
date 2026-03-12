plugins {
    id("quill.java-conventions")
}

repositories {
}

dependencies {
}

tasks.register<Copy>("preBundle") {
    val jarTask = tasks.named<Jar>("jar")

    into(layout.buildDirectory.dir("quill/pre-bundle"))

    from(jarTask.map { it.archiveFile })

    into("libs") {
        from(configurations.runtimeClasspath)
    }
}
