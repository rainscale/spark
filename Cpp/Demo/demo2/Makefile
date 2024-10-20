# 定义编译器
CC = gcc

# 定义编译选项
CFLAGS = -Wall $(INCLUDES)

# 定义链接选项
LDFLAGS = -L ./lib

# 定义目标文件的搜索路径
VPATH = ./tmp1 ./tmp2

INCLUDES = $(patsubst %, -I %, $(VPATH))

# 定义所有源文件
# SRCS = $(foreach dirs, $(VPATH), $(wildcard $(dirs)/*.c))
SRCS := $(shell find $(VPATH) -name '*.c')

# 从源文件生成对象文件
OBJS = $(patsubst %.c, %.o, $(SRCS))

# 从源文件生成依赖项文件
DEPS = $(patsubst %.c, %.d, $(SRCS))

# 定义最终的可执行文件
TARGET = demo2

# 默认目标
all: $(TARGET)

# 链接目标
$(TARGET): $(OBJS)
	echo "building $@..."
	$(CC) $(LDFLAGS) -o $@ $^

# 模式规则：从.c文件生成.o文件
%.o: %.c
	$(CC) $(CFLAGS) -c $< -o $@

%.d: %.c
	rm -f $@; \
	$(CC) -MM $(CFLAGS) $< > $@.$$$$; \
	sed 's,\($*\)\.o[ :]*,\1.o $@ : ,g' < $@.$$$$ > $@; \
	rm -f $@.$$$$

# 清理编译生成的文件
clean:
	@echo "clean..."
	rm -f $(DEPS) $(OBJS) $(TARGET)

print:
	@echo $(INCLUDES)
	@echo $(SRCS)
	@echo $(DEPS)
	@echo $(OBJS)

# 引入所有的依赖文件
-include $(DEPS)

# 伪目标，确保make按顺序执行
.PHONY: all clean print
