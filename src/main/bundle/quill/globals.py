from pathlib import Path

from run import HOME
from qrun import QUILL_PACKAGE_SCOPE, QUILL_PACKAGE_NAME

from quill.package import Package

QUILL_HOME: Path = HOME
PACKAGE_QUILL: Package = Package(QUILL_PACKAGE_SCOPE, QUILL_PACKAGE_NAME)
