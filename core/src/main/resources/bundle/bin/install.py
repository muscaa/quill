from pathlib import Path

from quill.package import Package
from quill.setup import SetupWizard

def main(package: Package, args: list[str]):
    if len(args) != 2:
        raise Exception("Arguments length != 2")
    
    namespace = args[0]
    dir = Path(args[1])
    if not dir.exists() or not dir.is_dir():
        raise Exception("Invalid directory")

    wizard = SetupWizard.load(dir, namespace)
    if not wizard:
        raise Exception(f"Directory '{dir}' is not an installable package")
    
    print(f"Installing {wizard.info.tag}...")
    result = wizard.install()
    if not result:
        raise Exception(f"Failed to install package '{wizard.info.tag}'")
    print(f"Done!")
