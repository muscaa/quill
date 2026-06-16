plugins {
    id("quill.java-conventions")
}

dependencies {
	bootstrap(project(":bootstrap"))
	
    api("dev.musca:fluff-commons:2.0.0")
    api("dev.musca:fluff-commander:2.0.0")
    api("dev.musca:fluff-platform:2.0.1")
    api("org.fusesource.jansi:jansi:2.4.3")
}

tasks.processResources {
    exclude("bundle/**")
}

tasks.register<Sync>("preBundle") {
    dependsOn("jar")
    into(layout.buildDirectory.dir("quill/pre-bundle"))

    into("java") {
        from(tasks.jar)
        into("libs") {
            from(configurations.runtimeClasspath.get()
                .minus(configurations.shade.get())
                .minus(configurations.bootstrap.get()))
        }
    }

    from("src/main/resources/bundle")
}
