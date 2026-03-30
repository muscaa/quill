from pathlib import Path
import shutil

from quill.setup import Command
from quill.setup.commands import owns

class Delete(Command):
    def __init__(self, path: Path, unowns: bool):
        super().__init__()
        self.path = path
        self.unowns = unowns
    
    def can_execute(self):
        return owns.owns(self.wizard.root_dir, self.path, self.wizard.info.tag)
    
    def execute(self):
        if self.path.is_dir():
            shutil.rmtree(self.path, ignore_errors=True)
        else:
            self.path.unlink(missing_ok=True)
        
        if self.unowns:
            owns_path = owns.get_owns_path(self.wizard.root_dir, self.path, direct=True)
            if owns_path:
                owns_path.unlink(missing_ok=True)
