import sys

def run_command(args: list[str]) -> None:
    command = args[0]

    if command == "base-install":
        from pathlib import Path

        from quill.commands.install import install

        base = Path(__file__).resolve().parent / "base"
        if len(args) >= 2:
            home = Path(args[1])
        else:
            home = Path(__file__).resolve().parent.parent.parent.parent

        install(base, home)

    elif command == "run":
        from quill import globals
        from quill.package import Package
        from quill.commands.run import run

        package = Package(args[1], args[2])

        run(package, args[3], args[4:])

def main() -> None:
    run_command(sys.argv[1:])

if __name__ == "__main__":
    main()
