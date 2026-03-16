from pathlib import Path
from core.utils import load_module

def run(package_path: Path, module_name: str):
    module = load_module(package_path, module_name)
    if module:
        module.main()
    else:
        print(f"Could not load module '{module_name}'")
