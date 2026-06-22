from pathlib import Path
import sys

import __about__ as a

PROJECT_ROOT = Path(a.__file__).parent
VENV_LIB = PROJECT_ROOT / ".venv" / ("Lib" if sys.platform == "win32" else "lib")
_VENV_LIB_PYTHON = VENV_LIB if (VENV_LIB / "site-packages").is_dir() else next(p for p in Path(VENV_LIB).iterdir() if p.is_dir())
VENV_LIB_SITEPACKAGES = _VENV_LIB_PYTHON / "site-packages"
SRC = PROJECT_ROOT / "core" / "src" / "main" / "resources" / "bundle"
SRC_QUILL_HOME = SRC / "_"
SRC_QUILL_PACKAGE = SRC
BUILD = PROJECT_ROOT / "build"
BUILD_GENERATED = BUILD / "quill" / "generated"
BUILD_PREBUNDLE = BUILD / "quill" / "pre-bundle"
