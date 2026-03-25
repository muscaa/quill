from pathlib import Path
import shutil

from quill.setup import Command

class Owns(Command):
    def __init__(self, paths: list[Path]):
        super().__init__()
        self.paths = paths
    
    def can_execute(self):
        # TODO check if another package owns those paths
        return True
    
    def execute(self):
        # TODO write owned paths to a file
        pass
