from pathlib import Path
import shutil

from quill.setup import Command

class Copy(Command):
    def __init__(self, src: Path, dest: Path):
        super().__init__()
        self.src = src
        self.dest = dest

    def can_execute(self):
        return True

    def execute(self):
        dir = self.dest if self.src.is_dir() or self.dest.is_dir() else self.dest.parent
        dir.mkdir(parents=True, exist_ok=True)

        if self.src.is_dir():
            shutil.copytree(self.src, self.dest, dirs_exist_ok=True)
        else:
            shutil.copy(self.src, self.dest)
