plugins {
    id("quill.about")
    id("quill.subproject")
    id("quill.publishing")
}

val about: Map<String, String> by extra

dependencies {
	bootstrap(project(":bootstrap"))
	
    api("dev.musca:fluff-commons:2.0.0")
    api("dev.musca:fluff-commander:2.0.0")
    api("dev.musca:fluff-platform:2.0.1")
    api("org.fusesource.jansi:jansi:2.4.3")
}

mavenPublishing {
	pom {
		name.set("${about["name"]} Core")
		description.set("${about["description"]} core")
	}
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
}
