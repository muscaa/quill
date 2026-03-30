from pathlib import Path

from quill.package import Package
from quill.setup import uninstall

def main(package: Package, args: list[str]):
    if len(args) != 1:
        raise Exception("Arguments length != 1")
    
    p = Package.find(args[0])
    if not p:
        raise Exception("Package not found")
    
    uninstall(p)
