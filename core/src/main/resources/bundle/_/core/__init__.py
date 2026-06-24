import sys
from pathlib import Path

from core.commands import run, subprocess

HOME: Path = Path(__file__).resolve().parent.parent
SCHEDULE: list[list[str]]
ARGS_INITIAL: list[str]
ARGS: list[str]

def _init():
    global SCHEDULE

    argv = sys.argv.copy()
    argv.pop(0)

    SCHEDULE = [argv]

def main():
    global ARGS_INITIAL, ARGS

    _init()

    while len(SCHEDULE) > 0:
        ARGS_INITIAL = SCHEDULE.pop(0)
        ARGS = ARGS_INITIAL.copy()

        command = ARGS.pop(0)
        if command == "run":
            run.execute()
        elif command == "subprocess":
            subprocess.execute()
        else:
            print("Unknown command")
