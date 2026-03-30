from __future__ import annotations
from abc import ABC, abstractmethod
from pathlib import Path
from types import ModuleType
import shutil

from core.utils import load_module
from quill.package import Package, PackageInfo
from quill.files import HOME, PACKAGES

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
        self.root_dir = HOME
        self.package_dir = PACKAGES / info.tag

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

        for command in commands:
            command.execute()

    def install(self) -> bool:
        try:
            if hasattr(self.module, "install"):
                self._begin()
                self.module.install()
                self._end()
                return True
        except Exception as e:
            print(str(e))
        return False

    def uninstall(self, remove: bool) -> bool:
        try:
            if hasattr(self.module, "uninstall"):
                self._begin()
                self.module.uninstall()
                self._end()
            if remove:
                shutil.rmtree(self.dir, ignore_errors=True)
                # TODO also remove every owned file/dir
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

def install(dir: Path, namespace: str):
    if not dir.exists() or not dir.is_dir():
        raise Exception("Invalid install directory")

    wizard = SetupWizard.load(dir, namespace)
    if not wizard:
        raise Exception(f"Directory '{dir}' is not an installable package")
    
    package = Package.find(wizard.info.tag)
    if package:
        uninstall(package, remove=False)

    print(f"Installing '{wizard.info.tag}'...")
    result = wizard.install()
    if not result:
        raise Exception(f"Failed to install package '{wizard.info.tag}'")
    print(f"Done!")

def uninstall(package: Package, remove: bool = True):
    dir = package.get_path()
    if not dir.exists() or not dir.is_dir():
        raise Exception("Invalid package")

    wizard = SetupWizard.load(dir, package.namespace)
    if not wizard:
        raise Exception(f"Directory '{dir}' is not an uninstallable package")
    
    print(f"Uninstalling '{wizard.info.tag}'...")
    result = wizard.uninstall(remove)
    if not result:
        raise Exception(f"Failed to uninstall package '{wizard.info.tag}'")
    print(f"Done!")
