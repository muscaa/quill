import os
import sys

from quill import globals

globals.DOT_QUILL = sys.argv[1]
globals.QUILL = os.path.dirname(os.path.abspath(__file__))

command = sys.argv[2]

if command == "run":
    from quill.tools.run import run

    run(scope = sys.argv[3],
        package = sys.argv[4],
        file = sys.argv[5],
        args = sys.argv[6:])
