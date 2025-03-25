import sys
from pathlib import Path

from run import QUILL_HOME
from qrun import QUILL_PACKAGE_SCOPE, QUILL_PACKAGE_NAME

from quill import globals
from quill.package import Package

globals.QUILL_HOME = QUILL_HOME
globals.PACKAGE_QUILL = Package(QUILL_PACKAGE_SCOPE, QUILL_PACKAGE_NAME)

def runCommand(args: list[str]):
    command = args[1]

    if command == "run":
        from quill.commands.run import run

        package = Package(args[2], args[3])

        run(package, args[4], args[5:])

def main():
    runCommand(sys.argv)
