from pathlib import Path
from shutil import copy

def install(src: Path, dest: Path) -> None:
    for file in src.iterdir():
        if file.is_file():
            copy(file, dest)
