from pathlib import Path
from core.utils import load_module

def execute():
    from core import HOME, ARGS

    package_path = Path(ARGS.pop(0))
    if not package_path.is_absolute():
        package_path = HOME / package_path
    
    module_name = ARGS.pop(0)
    
    run(package_path, module_name)
    pass

def run(package_path: Path, module_name: str):
    module = load_module(package_path, module_name)
    if module:
        module.main()
    else:
        print(f"Could not load module '{module_name}'")
