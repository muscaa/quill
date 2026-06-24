import subprocess
import sys

def execute():
    from core import ARGS

    args = ARGS.copy()
    ARGS.clear()

    proc = subprocess.run([sys.executable, *args])
    if proc.returncode != 0:
        print(f"Subprocess {args} returned {proc.returncode}")
