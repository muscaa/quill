plugins {
    id("quill.java-conventions")
}

dependencies {
	shade("dev.musca:fluff-loader:2.0.0") {
		exclude(group = "dev.musca", module = "fluff-core")
	}
	
	api("dev.musca:fluff-functions:2.0.0") {
		exclude(group = "dev.musca", module = "fluff-core")
	}
	
	implementation("dev.musca:fluff-vecmath:2.0.0") {
		exclude(group = "dev.musca", module = "fluff-core")
	}
}

tasks.register<Copy>("preBundle") {
    val jarTask = tasks.named<Jar>("jar")

    into(layout.buildDirectory.dir("quill/pre-bundle"))

    from(jarTask.map { it.archiveFile })

    into("libs") {
        from(configurations.runtimeClasspath)
        
        from(configurations.runtimeClasspath.incoming.files.filter { file ->
	        file !in shade.resolve()
	    })
    }
}
