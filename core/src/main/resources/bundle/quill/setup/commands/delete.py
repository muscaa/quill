from pathlib import Path
import shutil

from quill.setup import Command

class Delete(Command):
    def __init__(self, path: Path):
        super().__init__()
        self.path = path
    
    def can_execute(self):
        return True
    
    def execute(self):
        if self.path.is_dir():
            shutil.rmtree(self.path, ignore_errors=True)
        else:
            self.path.unlink(missing_ok=True)
