from __future__ import annotations
from pathlib import Path
from importlib import util
import sys

from core.utils import add_library, load_module

from quill.files import PACKAGES as DIR_PACKAGES
from quill.repository import Repository, REPOSITORIES

class Package:
    def __init__(self, namespace: str, author: str, id: str):
        self.namespace = namespace
        self.author = author
        self.id = id
        self.spec = f"{author}@{id}"
        self.tag = f"{namespace}/{self.spec}"

    def run(self, binary: str, args: list[str]) -> bool:
        if not self.include():
            return False

        try:
            module = load_module(self.get_path(), f"bin.{binary}")
            if module:
                module.main(self, args)
                return True
            
            raise Exception(f"Could not load module 'bin.{binary}'")
        except Exception as e:
            print(str(e))
            return False

    def include(self) -> bool:
        path = self.get_path()
        if not path.is_dir():
            return False
        
        add_library(path)

        return True

    def get_path(self) -> Path:
        return DIR_PACKAGES / self.namespace / self.spec
    
    def __str__(self) -> str:
        return f"Package(namespace=\"{self.namespace}\", author=\"{self.author}\", id=\"{self.id}\")"
    
    def __eq__(self, value: object) -> bool:
        if not isinstance(value, Package):
            return False
        return (self.namespace, self.author, self.id) == (value.namespace, value.author, value.id)
    
    def __hash__(self) -> int:
        return hash((self.namespace, self.author, self.id))
    
    @classmethod
    def split(cls, str: str) -> tuple[str | None, str | None, str | None]:
        split1 = str.split("/")
        if len(split1) > 2:
            return (None, None, None)
        
        split2 = split1[-1].split("@")
        if len(split2) > 2:
            return (None, None, None)

        namespace = split1[0] if len(split1) == 2 else None
        author = split2[0] if len(split2) == 2 else None
        id = split2[-1]

        return (namespace, author, id)

    @classmethod
    def packages(cls, dir: Path, repositories: list[Repository] | None = None) -> dict[str, Package]:
        packages: dict[str, Package] = {}

        priority_map = {
            repo.namespace: i for i, repo in enumerate(repositories or REPOSITORIES)
        }
        default_priority = len(priority_map)

        subdirs = [d for d in dir.iterdir() if d.is_dir()]
        sorted_subdirs = sorted(subdirs, key = lambda d: (priority_map.get(d.name, default_priority), d.name.lower()))

        for namespace_path in sorted_subdirs:
            namespace = namespace_path.name

            for spec_path in namespace_path.iterdir():
                if not spec_path.is_dir():
                    continue

                _, author, id = Package.split(spec_path.name)
                if not id or not author:
                    continue

                package = Package(namespace, author, id)
                packages[package.tag] = package
                
        return packages
    
    @classmethod
    def find(cls, str: str, packages: dict[str, Package] | None = None):
        namespace, author, id = Package.split(str)
        if not id:
            return None

        for key, value in (packages or PACKAGES).items():
            namespace0, author0, id0 = Package.split(key)
            if namespace and namespace != namespace0:
                continue
            if author and author != author0:
                continue

            if id == id0:
                return value

        return None

    @classmethod
    def from_file(cls, file: str):
        path = Path(file).resolve().relative_to(DIR_PACKAGES)
        parts = path.parts

        if len(parts) < 2 or path.is_absolute():
            raise Exception("Invalid install location")
        
        namespace = parts[0]
        _, author, id = Package.split(parts[1])
        if not id or not author:
            raise Exception("Invalid install location")
        
        return Package(namespace, author, id)
    
PACKAGES = Package.packages(DIR_PACKAGES)
