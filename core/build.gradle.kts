plugins {
    id("quill.java-conventions")
}

dependencies {
    api("dev.musca:fluff-core:2.0.0")
}

tasks.register<Sync>("preBundle") {
    dependsOn("jar")
    into(layout.buildDirectory.dir("quill/pre-bundle"))

    into("java") {
        from(tasks.jar)
        into("libs") {
            from(configurations.runtimeClasspath.get().minus(configurations.shade.get()))
        }
    }
}
