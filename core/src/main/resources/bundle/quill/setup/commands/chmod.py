from pathlib import Path
import os
import stat

from quill.setup import Command
from quill.setup.commands import owns

class Chmod(Command):
    def __init__(self, path: Path, read: bool | None = None, write: bool | None = None, exec: bool | None = None):
        super().__init__()
        self.path = path
        self.read = read
        self.write = write
        self.exec = exec
    
    def can_execute(self):
        return owns.owns(self.wizard.root_dir, self.path, self.wizard.info.tag)
    
    def execute(self):
        mode = os.stat(self.path).st_mode
        mappings = [
            (self.read, stat.S_IRUSR | stat.S_IRGRP | stat.S_IROTH),
            (self.write, stat.S_IWUSR | stat.S_IWGRP | stat.S_IWOTH),
            (self.exec, stat.S_IXUSR | stat.S_IXGRP | stat.S_IXOTH)
        ]

        for flag, mask in mappings:
            if flag is True:
                mode |= mask
            elif flag is False:
                mode &= ~mask
                
        os.chmod(self.path, mode)
