from pathlib import Path

from quill.package import Package
from quill.setup import wizard_install

def main(package: Package, args: list[str]):
    if len(args) != 2:
        raise Exception("Arguments length != 2")
    
    wizard_install(Path(args[0]), args[1])
