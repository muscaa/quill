from pathlib import Path
import sys

import __about__ as a

PROJECT_ROOT = Path(a.__file__).parent
SRC = PROJECT_ROOT / "core" / "src" / "main" / "resources" / "bundle"
SRC_QUILL_HOME = SRC / "_"
SRC_QUILL_PACKAGE = SRC
BUILD = PROJECT_ROOT / "build"
BUILD_GENERATED = BUILD / "quill" / "generated"
BUILD_PREBUNDLE = BUILD / "quill" / "pre-bundle"
