import sys

from quill.package import Package

def add_to_path(package: Package) -> None:
    sys.path.insert(0, package.get_dir())
