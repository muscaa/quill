from quill.utils import library

def run(scope: str, package: str, file: str, args: list[str]) -> None:
    library.addToPath(scope, package)

    exec(f"from bin.{file} import main")
    exec(f"main({args})")
