import sys
import core
from core.utils import load_module
from quill.package import Package
from quill.bootstrap import java
from quill.files import TEMP

def main(package: Package, args: list[str]):
    exit = java.run(package, "quill.Quill", args.copy())
    exit = 0 if core.ENV_QPOST else 10 # TODO remove
    
    if exit == 10: # install update & restart
        module = load_module(TEMP / "quill-update", "setup", False)
        if not module:
            sys.exit(1)

        module.install()

        with open(TEMP / f"post-{core.ENV_QPID}/{exit}", "w") as f:
            f.write(f"""
                    source $@
                    POST_COMMAND=$?
                    """)
        sys.exit(exit)
