from pathlib import Path
from core.utils import add_library

def run(package_path: Path, module_name: str):
    add_library(package_path)

    exec(f"from {module_name} import main")
    exec(f"main()")
