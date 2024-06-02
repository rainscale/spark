#!/usr/bin/python3
# -*- coding: utf-8 -*-
from collections import deque

'''
队列是一种先进先出（FIFO, First-In-First-Out）的数据结构
'''


class Queue:
    def __init__(self):
        self.queue = []

    def enqueue(self, item):
        self.queue.append(item)

    def dequeue(self):
        if not self.is_empty():
            return self.queue.pop(0)
        else:
            raise IndexError("dequeue from empty queue")

    def peek(self):
        if not self.is_empty():
            return self.queue[0]
        else:
            raise IndexError("peek from empty queue")

    def is_empty(self):
        return len(self.queue) == 0

    def size(self):
        return len(self.queue)


if __name__ == '__main__':
    # 使用示例
    queue = Queue()
    queue.enqueue('a')
    queue.enqueue('b')
    queue.enqueue('c')

    print("队首元素:", queue.peek())  # 输出: 队首元素: a
    print("队列大小:", queue.size())  # 输出: 队列大小: 3

    print("移除的元素:", queue.dequeue())  # 输出: 移除的元素: a
    print("队列是否为空:", queue.is_empty())  # 输出: 队列是否为空: False
    print("队列大小:", queue.size())  # 输出: 队列大小: 2

    '''
    使用列表时，如果频繁地在列表的开头插入或删除元素，性能会受到影响，因为这些操作的时间复杂度是O(n)。
    为了解决这个问题，Python提供了collections.deque，它是双端队列，可以在两端高效地添加和删除元素。
    它提供了O(1)时间复杂度的添加和删除操作，非常适合队列这种数据结构。
    '''
    # 创建一个空队列
    queue = deque()

    # 向队尾添加元素
    queue.append('a')
    queue.append('b')
    queue.append('c')

    print("队列状态:", queue)  # 输出: 队列状态: deque(['a', 'b', 'c'])

    # 从队首移除元素
    first_element = queue.popleft()
    print("移除的元素:", first_element)  # 输出: 移除的元素: a
    print("队列状态:", queue)  # 输出: 队列状态: deque(['b', 'c'])

    # 查看队首元素（不移除）
    front_element = queue[0]
    print("队首元素:", front_element)  # 输出: 队首元素: b

    # 检查队列是否为空
    is_empty = len(queue) == 0
    print("队列是否为空:", is_empty)  # 输出: 队列是否为空: False

    # 获取队列大小
    size = len(queue)
    print("队列大小:", size)  # 输出: 队列大小: 2
