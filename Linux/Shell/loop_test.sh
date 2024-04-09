#!/bin/bash

function killProcess() {
    # ps -ef | grep $1 | grep -v grep | grep -v PPID | awk '{print $2}' | xargs kill -9
    pids=$(ps -ef | grep $1 | grep -v grep | grep -v PPID | awk '{print $2}')
    for pid in $pids; do
        kill -9 $pid
    done
}

function showProcess() {
    timeout=60
    for name in $@; do
        echo $name
        pids=$(ps -ef | grep $name | grep -v grep | grep -v PPID | awk '{print $2}')
        for pid in $pids; do
            echo $pid
            ./loop_run.sh $pid &
            dpid=$!
            echo $dpid
            i=1
            while [[ -d /proc/$dpid && $i -le $timeout ]]; do
                echo "sleep ${i}s"
                sleep 1
                let i++
            done
            while [[ -d /proc/$dpid && $(echo -n yes) ]]; do
                echo "kill -9 $dpid"
                kill -9 $dpid
                sleep 1
            done
        done
    done
}

showProcess google