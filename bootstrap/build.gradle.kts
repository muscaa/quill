plugins {
    id("quill.about")
    id("quill.subproject")
    id("quill.publishing")
	id("application")
}

val about: Map<String, String> by extra
val mainClassName = "quill.bootstrap.QuillBootstrap"

dependencies {
	shade("dev.musca:fluff-loader:2.0.1") {
		exclude(group = "dev.musca", module = "fluff-core")
	}
}

mavenPublishing {
	pom {
		name.set("${about["name"]} Bootstrap")
		description.set("${about["description"]} bootstrap")
	}
}

tasks.register<Sync>("preBundle") {
	dependsOn("jar")
    into(layout.buildDirectory.dir("quill/pre-bundle"))

	into("bootstrap4j") {
		from(tasks.jar) {
			rename {
				"bootstrap.jar"
			}
		}
		into("libs") {
			from(configurations.runtimeClasspath.get().minus(configurations.shade.get()))
		}
	}
}

application {
	mainClass = mainClassName
}

tasks.jar {
	manifest {
		attributes(
			"Main-Class" to mainClassName
		)
	}
}
