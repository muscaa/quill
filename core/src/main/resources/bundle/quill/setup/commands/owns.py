from pathlib import Path

from quill.setup import Command

SUFFIX = ".owns"

def _find_parent(root: Path, path: Path) -> tuple[Path | None, Path]:
    system = [
        (root / "bin", 0),
        (root / "dist", 0),
        (root / "config", 0),
        (root / "packages", 2),
        (root, 0),
    ]

    for parent, offset in system:
        if path.is_relative_to(parent):
            child = path.relative_to(parent)
            if len(child.parts) <= offset:
                continue
            if offset != 0:
                parent = parent / Path(*child.parts[:offset])
                child = Path(*child.parts[offset:])
            return (parent, child)

    return (None, path)

def get_owns_path(root: Path, path: Path, direct: bool = False) -> Path | None:
    parent, child = _find_parent(root, path)
    if not parent:
        return None
    
    db_dir = root / "db" / "owns"
    parent = parent.relative_to(root)
    
    if not direct:
        file = child
        while len(file.parts) > 0:
            file_parent = file.parent
            file_name = file.name

            owns = db_dir / parent / file_parent / (file_name + SUFFIX)
            if owns.exists():
                return owns

            file = file_parent

    return db_dir / parent / child.parent / (child.name + SUFFIX)

def owns(root: Path, path: Path, tag: str):
    owns_path = get_owns_path(root, path)
    if not owns_path:
        return True
    
    if not owns_path.exists():
        return True
    
    with open(owns_path, "r") as f:
        lines = f.readlines()
        return tag in lines

class Owns(Command):
    def __init__(self, path: Path):
        super().__init__()
        self.path = path
    
    def can_execute(self):
        return owns(self.wizard.root_dir, self.path, self.wizard.info.tag)
    
    def execute(self):
        owns_path = get_owns_path(self.wizard.root_dir, self.path)
        if owns_path and not owns_path.exists():
            owns_path.parent.mkdir(parents=True, exist_ok=True)
            with open(owns_path, "w") as f:
                f.write(self.wizard.info.tag)
