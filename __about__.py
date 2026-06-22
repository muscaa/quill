import os

ID = "quill"
NAME = "Quill"
AUTHOR_ID = "muscaa"
AUTHOR_NAME = "musca"
VERSION = os.environ.get("GITHUB_REF_NAME", "v0.0.1-SNAPSHOT").removeprefix("v")
DESCRIPTION = "Quill CLI"
