plugins {
    id("quill.java-conventions")
    id("quill.publishing")
}

dependencies {
	bootstrap(project(":bootstrap"))
	
    api("dev.musca:fluff-commons:2.0.0")
    api("dev.musca:fluff-commander:2.0.0")
    api("dev.musca:fluff-platform:2.0.1")
    api("org.fusesource.jansi:jansi:2.4.3")
}

mavenPublishing {
	pom {
		name.set("Quill Core")
		description.set("Quill CLI app core")
	}
}

tasks.processResources {
    exclude("bundle/**")
}

val generateBundleResources = tasks.register("generateBundleResources") {
	val outputDir = layout.buildDirectory.dir("quill/generated/bundle")
	outputs.dir(outputDir)
	
	doLast {
		val dir = outputDir.get().asFile
		dir.mkdirs()
		
		val core = dir.resolve("_/core")
		core.mkdirs()
		core.resolve("version.txt").writeText("${project.version}")
	}
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
    from(generateBundleResources)
}
