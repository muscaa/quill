import sys
from pathlib import Path

HOME: Path = Path(__file__).resolve().parent

def run(package_path: Path, module_name: str) -> None:
    sys.path.insert(0, str(package_path))
    
    exec(f"from {module_name} import main")
    exec(f"main()")

if __name__ == "__main__":
    package_path = HOME / sys.argv[1]
    module_name = sys.argv[2]
    
    run(package_path, module_name)
