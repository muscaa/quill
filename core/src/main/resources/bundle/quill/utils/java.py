import os
import subprocess
from pathlib import Path

from quill.package import Package

def run(main_class: str, packages: list[Package], args: list[str]):
    classpath_entries: list[Path] = []
    
    for package in packages:
        java_dir = package.get_path() / "java"

        for dir in java_dir.iterdir():
            if dir.is_dir():
                classpath_entries.append(dir)

    classpath = os.pathsep.join(str(e) for e in classpath_entries)

    subprocess.run(["java", "-cp", classpath, main_class, *args])
