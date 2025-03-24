from quill.utils import library
from quill.package import Package

def run(package: Package, file_name: str, args: list[str]) -> None:
    library.add_to_path(package)

    exec(f"from bin.{file_name} import main")
    exec(f"main(package, args)")
