import sys
import core
from quill.package import Package
from quill.bootstrap import java
from quill.files import TEMP
from quill.setup import install
from quill.globals import PACKAGE_QUILL

def main(package: Package, args: list[str]):
    exit = java.run(package, "quill.Quill", args.copy())
    
    if exit == 10: # install update & restart
        install(TEMP / "quill-update", PACKAGE_QUILL.namespace)

        with open(TEMP / f"post-{core.ENV_QPID}/{exit}", "w") as f:
            f.write(f"""
                    export POST_QUILL_UPDATE="true"
                    source $@
                    POST_COMMAND=$?
                    unset POST_QUILL_UPDATE
                    """)
        with open(TEMP / f"post-{core.ENV_QPID}/{exit}.cmd", "w") as f:
            f.write(f"""
                    set "POST_QUILL_UPDATE=true"
                    call %*
                    set "POST_COMMAND=%errorlevel%"
                    set "POST_QUILL_UPDATE="
                    """)
        sys.exit(exit)
