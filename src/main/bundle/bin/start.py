from quill.package import Package
from quill.utils import java

def main(package: Package, args: list[str]) -> None:
    print("quill start.py")
    print(package.scope, package.name)
    print(package.get_path())

    java.run("quill.Quill", [package], args)
