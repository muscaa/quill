version = System.getenv("GITHUB_REF_NAME")?.removePrefix("v") ?: "0.0.1-SNAPSHOT"

val generatePackageJson = tasks.register("generatePackageJson") {
	val outputFile = layout.buildDirectory.file("quill/generated/package.json")
	outputs.file(outputFile)
	
	doLast {
		val file = outputFile.get().asFile
		file.parentFile.mkdirs()
		file.writeText(
			"""
			{
				"id": "${project.name}",
				"author": "muscaa",
				"version": "${project.version}",
				"description": "..."
			}
			""".trimIndent()
		)
	}
}

tasks.register<Zip>("bundle") {
    group = "quill"
    description = "Bundles the project into an installable quill java package."

    destinationDirectory.set(layout.buildDirectory.dir("quill/bundle"))
    archiveFileName.set("${project.name}-bundle.zip")
    
    from(generatePackageJson)

    subprojects {
        val preBundleTask = tasks.named("preBundle")
        dependsOn(preBundleTask)

        from(preBundleTask.map { it.outputs.files })
    }
}
