#!/usr/bin/env python3
# -*- coding: utf-8 -*-

import subprocess
import sys

if __name__ == "__main__":

    if len(sys.argv) < 5:
        print("\nUsage: ./target_runner.py <configuration_id> <instance_id> <seed> <instance_path_name> <list of parameters>\n")
        sys.exit(1)

    # path to the software:
    exe = "java pmpc.Main"

    # get the parameters as command line arguments.
    configuration_id = sys.argv[1]
    instance_id = sys.argv[2]
    seed = sys.argv[3]
    instance = sys.argv[4]
    conf_params = sys.argv[5:]

    # build the command, and then run it.
    command = exe.split() + [instance] + conf_params + ["-seed"] + [seed]
    # command = java pmpc.Main SJC100_10.txt -iter 500 -numav 100 -seed 50

    print(' '.join(command))
    
    obj = float(subprocess.check_output(command))

    print(obj)

    sys.exit(0)
