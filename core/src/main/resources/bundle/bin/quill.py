from quill.package import Package
from quill.bootstrap import java

def main(package: Package, args: list[str]):
    java.run(package, "quill.Quill", args.copy())
