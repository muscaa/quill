from core import ARGS
from quill.package import Package

def main():
    if len(ARGS) == 0:
        print("Missing package command")
        return
    
    split = ARGS.pop(0).split(":")
    if len(split) > 2:
        print("Invalid package command")
        return
    
    package = Package.find(split[0])
    if not package:
        print(f"Package '{split[0]}' not found")
        return

    package_command = split[1] if len(split) == 2 else package.id

    result = package.run(package_command, ARGS.copy())
    if not result:
        print("Failed to execute package command")
