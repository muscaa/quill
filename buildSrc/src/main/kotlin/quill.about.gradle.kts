extra["about"] = mapOf(
    "id" to System.getenv("QUILL_ID") ?: throw GradleException("QUILL_ID environment variable is required"),
    "name" to System.getenv("QUILL_NAME") ?: throw GradleException("QUILL_NAME environment variable is required"),
    "authorId" to System.getenv("QUILL_AUTHOR_ID") ?: throw GradleException("QUILL_AUTHOR_ID environment variable is required"),
    "authorName" to System.getenv("QUILL_AUTHOR_NAME") ?: throw GradleException("QUILL_AUTHOR_NAME environment variable is required"),
    "version" to System.getenv("QUILL_VERSION") ?: throw GradleException("QUILL_VERSION environment variable is required"),
    "description" to System.getenv("QUILL_DESCRIPTION") ?: throw GradleException("QUILL_DESCRIPTION environment variable is required"),
)
val about: Map<String, String> by extra

group = "dev.musca"
version = about.getValue("version")
