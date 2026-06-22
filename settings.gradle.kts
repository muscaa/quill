rootProject.name = System.getenv("QUILL_ID") ?: throw GradleException("QUILL_ID environment variable is required")

include("bootstrap")
include("core")
