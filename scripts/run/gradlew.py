import subprocess
import sys

from scripts.files import PROJECT_ROOT
from scripts import env

def run(args: list[str] | None = None):
    env()

    if not args:
        args = sys.argv[1:]

    if sys.platform == "win32":
        command = ["gradlew.bat", *args]
    else:
        command = ["bash", "./gradlew", *args]

    proc = subprocess.run(command, cwd=PROJECT_ROOT)
    if proc.returncode != 0:
        raise Exception(f"Gradle command returned {proc.returncode}")
