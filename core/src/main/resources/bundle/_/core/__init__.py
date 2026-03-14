import sys
from pathlib import Path
from core.files import HOME
from core.run import run
from core.install import install

VERSION: str
SCHEDULE: list[list[str]]
ARGS: list[str]

def _command_run():
    package_path = Path(ARGS.pop(0))
    if not package_path.is_absolute():
        package_path = HOME / package_path
    
    module_name = ARGS.pop(0)
    
    run(package_path, module_name)

def _command_install():
    pass

def _init():
    global VERSION, SCHEDULE

    with open(Path(__file__).resolve().parent / "version.txt") as f:
        VERSION = f.read().strip()

    argv = sys.argv.copy()
    argv.pop(0)

    SCHEDULE = [argv]

def main():
    global ARGS

    _init()

    while len(SCHEDULE) > 0:
        ARGS = SCHEDULE.pop(0)

        command = ARGS.pop(0)
        if command == "run":
            _command_run()
        elif command == "install":
            _command_install()
        else:
            print("Unknown command")
