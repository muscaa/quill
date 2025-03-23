import sys

from quill.globals import *

def run(scope: str, package: str, file: str, args: list[str]) -> None:
    sys.path.insert(0, f"{DOT_QUILL}/packages/{scope}/{package}/")

    exec(f"from bin.{file} import main")
    exec(f"main({args})")
