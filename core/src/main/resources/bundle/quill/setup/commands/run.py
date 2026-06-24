from collections.abc import Callable

from quill.setup import Command

class Run(Command):
    def __init__(self, func_execute: Callable[[], None], func_can_execute: Callable[[], bool] | None):
        super().__init__()
        self.func_execute = func_execute
        self.func_can_execute = func_can_execute
    
    def can_execute(self):
        if self.func_can_execute is None:
            return True
        return self.func_can_execute()
    
    def execute(self):
        self.func_execute()
