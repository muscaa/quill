import sys
import os
from pathlib import Path
from core.commands import version, run

HOME: Path = Path(__file__).resolve().parent.parent
ENV_QPID = int(os.getenv("QPID", -1))
ENV_QPOST = bool(os.getenv("QPOST", False))
VERSION: str
SCHEDULE: list[list[str]]
ARGS: list[str]

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
        if command == "version":
            version.execute()
        elif command == "run":
            run.execute()
        else:
            print("Unknown command")
