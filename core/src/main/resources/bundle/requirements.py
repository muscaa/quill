import sysconfig

from quill.setup.versions import V1

def resolve():
    v1 = V1(__file__)

    pip_deps = [line.strip() for line in v1.resolve("libs/pip.txt").read_text().split("\n") if line and not line.strip().startswith("#")]
    v1.download(f"libs/{sysconfig.get_platform()}", "pip", pip_deps)
