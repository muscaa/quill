from __future__ import annotations
from abc import ABC, abstractmethod
from pathlib import Path
from types import ModuleType

from core.utils import load_module
from quill.package import PackageInfo
from quill.files import TEMP

class Command(ABC):
    def __init__(self):
        super().__init__()
        self._wizard: SetupWizard | None = None

    @property
    def wizard(self):
        if not self._wizard:
            raise AttributeError("The 'wizard' field has not been set yet!")
        return self._wizard
    
    @abstractmethod
    def can_execute(self) -> bool:
        pass

    @abstractmethod
    def execute(self) -> None:
        pass

_SETUP_WIZARDS: dict[str, SetupWizard] = {}

class SetupWizard:
    def __init__(self, info: PackageInfo, module: ModuleType):
        self._commands: list[Command] | None = None
        self.info = info
        self.module = module
        self.dir = info.dir
        self.root_dir = TEMP / "_"
        self.package_dir = TEMP / "_/packages" / info.tag

    def _begin(self):
        if self._commands is not None:
            raise Exception("Can't run a setup command inside another setup command")
        
        self._commands = []

    def _add(self, command: Command):
        if self._commands is None:
            raise Exception("No setup command running")
        if command._wizard:
            raise Exception(f"Command {str(command)} already has a setup wizard")
        command._wizard = self

        if not command.can_execute():
            raise Exception(f"Command {str(command)} can't execute")
        
        self._commands.append(command)
        
    def _end(self):
        if self._commands is None:
            raise Exception("No setup command running")
        
        commands = self._commands
        self._commands = None

        # shutil.rmtree(self.root_dir, ignore_errors=True) # TODO remove

        for command in commands:
            command.execute()

    def install(self) -> bool:
        try:
            if hasattr(self.module, "install"):
                print(f"Installing {self.info.tag}...")
                self._begin()
                self.module.install()
                self._end()
                print(f"Done!")
                return True
        except Exception as e:
            print(str(e))
        return False

    def uninstall(self) -> bool:
        try:
            if hasattr(self.module, "uninstall"):
                self._begin()
                self.module.uninstall()
                self._end()
                return True
        except Exception as e:
            print(str(e))
        return False

    @classmethod
    def get(cls, path: str | Path) -> SetupWizard | None:
        return _SETUP_WIZARDS.get(str(Path(path).resolve().absolute()))

    @classmethod
    def load(cls, dir: Path, namespace: str) -> SetupWizard | None:
        path = dir / "wizard.py"
        wizard = SetupWizard.get(path)
        if wizard:
            return wizard
        
        info = PackageInfo.from_dir(dir, namespace)
        if not info:
            return None

        module = load_module(dir, "wizard", False)
        if not module:
            return None

        wizard = SetupWizard(info, module)
        _SETUP_WIZARDS[str(Path(path).resolve().absolute())] = wizard
        return wizard
