tasks.register<Zip>("bundle") {
    group = "quill"
    description = "Bundles the project into an installable quill java package."

    destinationDirectory.set(layout.buildDirectory.dir("quill/bundle"))
    archiveFileName.set("${project.name}-bundle.zip")

    subprojects {
        val preBundleTask = tasks.named("preBundle")
        dependsOn(preBundleTask)

        from(preBundleTask.map { it.outputs.files })
    }
}
