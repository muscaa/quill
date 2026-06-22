from typing import Literal
from pathlib import Path
import shutil

from quill.setup import Command
from quill.setup.commands import owns

type NewLine = Literal["auto", "lf", "crlf"]

class Copy(Command):
    def __init__(self, src: Path, dest: Path, overwrite: bool, newline: NewLine):
        super().__init__()
        self.src = src
        self.dest = dest
        self.overwrite = overwrite
        self.newline = newline

    def can_execute(self):
        return owns.owns(self.manager.root_dir, self.dest, self.manager.info.tag)

    def execute(self):
        if not self.overwrite and self.dest.exists():
            return

        dir = self.dest if self.src.is_dir() or self.dest.is_dir() else self.dest.parent
        dir.mkdir(parents=True, exist_ok=True)

        if self.src.is_dir():
            shutil.copytree(self.src, self.dest, dirs_exist_ok=True, copy_function=lambda src, dest: self._copy_file(Path(src), Path(dest)))
        else:
            self._copy_file(self.src, self.dest)
    
    def _copy_file(self, src: Path, dest: Path):
        if self.newline == "auto":
            shutil.copy(src, dest)
        elif self.newline == "lf":
            with open(src, "r") as f:
                content = f.read()
            with open(dest, "w", newline="\n") as f:
                f.write(content)
        elif self.newline == "crlf":
            with open(src, "r") as f:
                content = f.read()
            with open(dest, "w", newline="\r\n") as f:
                f.write(content)
