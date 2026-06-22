from typing import Literal
from pathlib import Path
import sys
import subprocess

from quill.setup import Command
from quill.setup.commands import owns

type Mode = Literal["pip"]

class Download(Command):
    def __init__(self, dir: Path, mode: Mode, resource: str):
        super().__init__()
        self.dir = dir
        self.mode = mode
        self.resource = resource
    
    def can_execute(self):
        return owns.owns(self.manager.root_dir, self.dir, self.manager.info.tag)
    
    def execute(self):
        self.dir.mkdir(parents=True, exist_ok=True)

        if self.mode == "pip":
            self._download_pip()
        else:
            raise Exception(f"Download mode '{self.mode}' not implemented")

    def _download_pip(self): # TODO resource needs to be more "specific", like {id}:{version} so i can get the id easier instead of reading output (which is very slow)
        proc = subprocess.run([sys.executable, "-m", "pip", "download", self.resource, "-d", self.dir, "--only-binary=:all:", "--no-deps"], check=True, capture_output=True, text=True)
        lines = proc.stdout.split("\n")
        line_saved = [line for line in lines if line.startswith("Saved")]
        if len(line_saved) == 0:
            return
        
        _, saved = line_saved[0].split(" ", 1)
        saved_path = Path(saved)
        print(f"Downloaded '{saved_path.name}'")

        # TODO extract to "{id}/"
