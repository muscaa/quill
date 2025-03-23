import os
import sys

from quill import globals

globals.DOT_QUILL = sys.argv[1]
globals.QUILL = os.path.dirname(os.path.abspath(__file__))

command = sys.argv[2]

if command == "run":
    from quill.commands.run import run
    from quill.package import Package

    package = Package(sys.argv[3], sys.argv[4])

    run(package, sys.argv[5], sys.argv[6:])
