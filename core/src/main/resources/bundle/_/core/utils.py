import sys
from pathlib import Path
from importlib import util

def add_library(path: Path):
    path_str = str(path)
    if sys.path.count(path_str) == 0:
        sys.path.insert(0, path_str)

def load_module(package_path: Path, module_name: str):
    if module_name in sys.modules:
        return sys.modules[module_name]

    add_library(package_path)
    
    module_path = package_path / f"{"/".join(module_name.split("."))}.py"
    spec = util.spec_from_file_location(module_name, module_path)

    if spec and spec.loader:
        module = util.module_from_spec(spec)
        sys.modules[module_name] = module
        spec.loader.exec_module(module)
        return module
    
    return None
