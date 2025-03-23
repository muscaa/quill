import sys

from quill.globals import *

def addToPath(scope: str, package: str) -> None:
    sys.path.insert(0, f"{DOT_QUILL}/packages/{scope}/{package}/")