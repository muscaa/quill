tasks.register<Zip>("bundle") {
    group = "quill"
    description = "Bundles the project into an installable quill java package."

    archiveFileName.set("${project.name}-bundle.zip")
    destinationDirectory.set(layout.buildDirectory.dir("quill"))

    subprojects {
        val subproject = this
        val collectTask = subproject.tasks.named("preBundle")
        
        dependsOn(collectTask)
        
        from(collectTask.map { it.outputs.files }) {
            into(subproject.name)
        }
    }
}
