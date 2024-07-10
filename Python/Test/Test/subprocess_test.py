#!/usr/bin/env python
# -*- coding: utf-8 -*-
# coding: utf-8

import datetime
import os
import re
import subprocess
from threading import Timer


def saveLog(deviceID, timeout=3):
    current_datetime = datetime.datetime.now()
    print(current_datetime.__format__('%Y-%m-%d %H:%M:%S'))
    time_stamp = current_datetime.timestamp()
    print(time_stamp)
    utc_time_tuple = datetime.datetime.now().utctimetuple()
    print(utc_time_tuple)
    # time_str = re.sub(r'[^0-9]', '', str(utc_time_tuple[0:6]))
    time_str = current_datetime.__format__('%Y%m%d-%H%M%S')
    print(time_str)
    savepath = os.path.join(os.path.dirname(os.path.dirname(os.path.dirname(os.path.dirname(__file__)))), "log",
                            time_str)
    print(savepath)
    cmd = 'adb -s %s logcat > %s' % (deviceID, savepath)
    p = subprocess.Popen(cmd, stdout=subprocess.PIPE, stderr=subprocess.PIPE)
    timer = Timer(timeout, lambda process: process.kill(), [p])
    result = False
    try:
        timer.start()
    finally:
        p.terminate()
        p.communicate()
        timer.cancel()
    return result


saveLog('a12345f', 5)
subprocess.run(["dir", "d:"])
subprocess.run("ls -l", shell=True)
subprocess.run("exit 1", shell=True, check=True)
