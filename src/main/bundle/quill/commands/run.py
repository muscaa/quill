from quill.utils import library
from quill.package import Package

def run(package: Package, fileName: str, args: list[str]) -> None:
    library.add_to_path(package)

    exec(f"from bin.{fileName} import main")
    exec(f"main(package, args)")
