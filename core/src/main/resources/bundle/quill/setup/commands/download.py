from __future__ import annotations
from abc import ABC, abstractmethod
from pathlib import Path
import sys
import subprocess
import zipfile

from quill.setup import Command
from quill.setup.commands import owns

class Download(Command):
    def __init__(self, dir: Path, resource: Resource):
        super().__init__()
        self.dir = dir
        self.resource = resource
    
    def can_execute(self):
        return owns.owns(self.manager.root_dir, self.dir, self.manager.info.tag)
    
    def execute(self):
        self.dir.mkdir(parents=True, exist_ok=True)
        self.resource.download(self.dir)

class Resource(ABC):
    def __init__(self):
        super().__init__()

    @abstractmethod
    def download(self, dir: Path) -> None:
        pass

class ResourcePip(Resource):
    def __init__(self, name: str, version: str):
        super().__init__()
        self.name = name
        self.version = version

    def download(self, dir: Path):
        package_dir = dir / self.name
        if package_dir.is_dir():
            return

        proc = subprocess.run([
            sys.executable, "-m", "pip", "download", f"{self.name}=={self.version}", "-d", dir, "--only-binary=:all:", "--no-deps"
            ], check=True, capture_output=True, text=True)
        lines = proc.stdout.split("\n")
        line_saved = [line for line in lines if line.startswith("Saved")]
        if len(line_saved) == 0:
            raise Exception(f"Couldn't download pip resource {self.name}:{self.version}")
        
        _, saved = line_saved[0].split(" ", 1)
        saved_path = Path(saved)
        
        with zipfile.ZipFile(saved_path, "r") as zf:
            zf.extractall(package_dir)
        saved_path.unlink()
