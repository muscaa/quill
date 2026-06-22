import sys
sys.dont_write_bytecode = True
import shutil
from pathlib import Path
shutil.rmtree(Path(__file__).parent / "__pycache__")

from scripts.run.init import run as init
from scripts.run.env import run as env
from scripts.run.generate import run as generate
from scripts.run.gradlew import run as gradlew
from scripts.run.dev import run as dev
from scripts.run.build import run as build
