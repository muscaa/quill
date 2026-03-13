import sys
from pathlib import Path

def add_library(path: Path):
    path_str = str(path)
    if sys.path.count(path_str) == 0:
        sys.path.insert(0, path_str)
