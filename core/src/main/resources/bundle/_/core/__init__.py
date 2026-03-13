import sys
from pathlib import Path
from core.files import HOME

VERSION: str
ARGS: list[str]

def _init():
    global VERSION, ARGS

    with open(Path(__file__).resolve().parent / "version.txt", "r") as f:
        VERSION = f.read().strip()

    ARGS = sys.argv.copy()
    ARGS.pop(0)

def main():
    _init()

    command = ARGS.pop(0)
    if command == "run":
        _command_run()
    elif command == "install":
        _command_install()
    else:
        print("Unknown command")

def _command_run():
    from core.run import run

    package_path = Path(ARGS.pop(0))
    if not package_path.is_absolute():
        package_path = HOME / package_path
    
    module_name = ARGS.pop(0)
    
    run(package_path, module_name)

def _command_install():
    from core.install import install
