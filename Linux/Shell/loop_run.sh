#!/bin/bash
 
pid=$1
process="xxx"
output_file="memory_info_${pid}.txt"
 
# 如果有这个文件就先删除
if [ -f $output_file ]; then
    rm -rf $output_file
fi
 
while true; do
    date=$(date +"%Y-%m-%d %H:%M:%S")
    echo "$date" >> "$output_file"
 
    # 获取进程ID
    # pid=$(pgrep $process)
 
    # if [ -n "$pid" ]; then
    if [ -d /proc/$pid ]; then
        # 获取进程的CPU和内存占用率
        cpu=$(ps -p $pid -o %cpu --no-headers)
        mem=$(ps -p $pid -o %mem --no-headers)
        # 获取系统内存使用情况
        memory_info=$(free -m)
 
        # 打印CPU和内存占用率
        echo " $pid进程的CPU占用率: $cpu%，内存占用率: $mem%" >> "$output_file"
        echo " 系统的内存使用情况：" >> "$output_file"
        echo " $memory_info" >> "$output_file"
        echo "--------------------------------------------" >> "$output_file"
    else
        echo "$pid进程不存在" >> "$output_file"
        echo "--------------------------------------------" >> "$output_file"
        exit 1
    fi
 
    # 等待15秒
    sleep 15
done
 
