plugins {
    `java-platform`
    id("quill.about")
    id("quill.publishing")
}

val about: Map<String, String> by extra

javaPlatform {
    allowDependencies()
}

dependencies {
    api(project(":bootstrap"))
    api(project(":core"))
}

mavenPublishing {
	pom {
		name.set(about["name"])
		description.set(about["description"])
	}
}

tasks.register<Sync>("preBundle") {
    into(layout.buildDirectory.dir("quill/pre-bundle"))

    subprojects {
        val preBundleTask = tasks.named("preBundle")
        dependsOn(preBundleTask)

        from(preBundleTask.map { it.outputs.files })
    }
}
