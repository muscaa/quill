from __future__ import annotations
from pathlib import Path

from quill.files import PACKAGES, CONFIG

class Repository:
    def __init__(self, namespace: str, url: str):
        self.namespace = namespace
        self.url = url

    def get_path(self) -> Path:
        return PACKAGES / self.namespace
    
    def __str__(self) -> str:
        return f"Repository(namespace=\"{self.namespace}\", url=\"{self.url}\")"
    
    def __eq__(self, value: object) -> bool:
        if not isinstance(value, Repository):
            return False
        return (self.namespace, self.url) == (value.namespace, value.url)
    
    def __hash__(self) -> int:
        return hash((self.namespace, self.url))
    
    @classmethod
    def repositories(cls, file: Path) -> list[Repository]:
        repositories: list[Repository] = []

        with open(file, "r") as f:
            for raw_line in f:
                line = raw_line.strip()

                if not line or line.startswith("#"):
                    continue

                namespace, url = line.split(" ", 1)
                repositories.append(Repository(namespace, url))

        return repositories
    
REPOSITORIES = Repository.repositories(CONFIG / "repositories.txt")
