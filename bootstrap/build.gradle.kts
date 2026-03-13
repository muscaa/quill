plugins {
    id("quill.java-conventions")
	id("application")
}

val mainClassName = "quill.bootstrap.QuillBootstrap"

dependencies {
	shade("dev.musca:fluff-loader:2.0.1") {
		exclude(group = "dev.musca", module = "fluff-core")
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
