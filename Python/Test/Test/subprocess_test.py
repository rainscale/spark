#!/usr/bin/env python
# -*- coding: utf-8 -*-
# coding: utf-8

import subprocess

subprocess.run(["dir", "d:"])
subprocess.run("ls -l", shell=True)
subprocess.run("exit 1", shell=True, check=True)