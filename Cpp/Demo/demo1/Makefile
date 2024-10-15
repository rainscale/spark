CROSS_COMPILE ?= arm-linux-gnueabihf-

default: all

TARGET := demo1

ifndef OUT_DIR
	OUT_DIR := ./
endif

# C语言编译工具
CC := $(CROSS_COMPILE)gcc
# C++编译工具
CXX := $(CROSS_COMPILE)g++
# 将目标文件打包为静态链接库的工具
AR := $(CROSS_COMPILE)ar
# 将目标文件与库链接为可执行文件的工具
LD := $(CROSS_COMPILE)ld
# 裁剪调试信息与符号表的工具
STRIP := $(CROSS_COMPILE)strip
# AR子工具，用来为归档文件创建符号表索引，符号表文件可以让连接器快速查找函数符合在相应对应文件的位置
RANLIB := $(CROSS_COMPILE)ranlib
# 用来将目标文件拷贝到另一个目标文件中，可以指定格式
OBJCOPY := $(CROSS_COMPILE)objcopy
# 用于查看目标文件或可执行文件的组成信息
OBJDUMP := $(CROSS_COMPILE)objdump

CFLAGS = -Wall -fPIC -Wl, -soname -w -D_REENTRANT -fpermissive -O2 -std=gnu++11

CFLAGS += $(XCFLAGS)
CPPFLAGS
LDFLAGS = -shared
INCLUDES := include

INCLUDE := $(patsubst %, -I %, $(INCLUDES))

ifneq ($(verbose), yes)
	QUIET_AR = @echo "	AR $@";
	QUIET_RANLIB = @echo "	RANLIB $@";
	QUIET_CC = @echo "	CC $@";
	QUIET_CXX = @echo "	CXX $@";
	QUIET_GEN = @echo "	GEN $@";
	QUIET_LINK = @echo "	LINK $@";
	QUIET_RM = @echo "	RM $@";
	QUIET_TAGS = @echo "	TAGS $@";
	QUIET_WINDRES = @echo "	WINDRES $@";
	QUIET_OBJCOPY = @echo "	OBJCOPY $@";
	QUIET_DLLTOOL = @echo "	DLLTOOL $@";
	QUIET_GENDEF = @echo "	GENDEF $@";
endif

MKTGTDIR = mkdir -p $(dir $@)
LINK_CMD = $(QUITE_LINK) $(MKTGTDIR); $(CC) $(LDFLAGS) -o $@ $^ $(CFLAGS) $(INCLUDES)
AR_CMD = $(QUIET_AR) $(MKTGTDIR); $(AR) cr $@ $^

$(OUT_DIR)/%.a:
	$(AR_CMD)

$(OUT_DIR)/%.so:
	$(LINK_CMD)


SRC_DIRS := src

OBJS_DIR := obj

SFILES := $(foreach dir, $(SRC_DIRS), $(wildcard $(dir)/*.S))
CFILES := $(foreach dir, $(SRC_DIRS), $(wildcard $(dir)/*.c))
CXXFILES := $(foreach dir, $(SRC_DIRS), $(wildcard $(dir)/*.cpp))

SFILESNDIR := $(notdir $(SFILES))
CFILESNDIR := $(notdir $(CFILES))
CXXFILESNDIR := $(notdir $(CXXFILES))

SOBJS := $(patsubst %, obj/%, $(SFILESNDIR:.S=.o))
COBJS := $(patsubst %, obj/%, $(CFILESNDIR:.c=.o))
CXXOBJS := $(patsubst %, obj/%, $(SFILESNDIR:.cpp=.o))

OBJS := $(SOBJS) $(COBJS) $(CXXOBJS)

$(SOBJS): $(OBJDIR)/%.o:%.S
	$(CC) $(CFLAGS) $(INCLUDE) -o $@ $<

$(COBJS): $(OBJDIR)/%.o:%.c
	$(CC) $(CFLAGS) $(INCLUDE) -o $@ $<

$(MAIN_APP): $(SOURCES)

$(UTILS_LIB): $(SOURCES)
	$(CC) -o $@ $^ $(INCLUDES) $(CFLAGS) --shared
$(DEMO): $(DEMO_SRCS)
	$(CC) -o -$@ -$^ $(INCLUDES) $(CFLAGS) $(LIBS)

libs: $(UTILS_LIB)
apps: $(DEMO)

all: libs apps

clean:
	rm -rf $(OBJ_DIR) $(TARGET_EXEXC) $(TARGET_LIB)

rebuild: clean build

.PHONY: all clean  libs apps

print:
	@echo INCLUDE = $(INCLUDE)
	@echo SFILES = $(SFILES)
	@echo CFILES = $(CFILES)
	@echo CXXFILES = $(CXXFILES)
	@echo SFILESNDIR = $(SFILESNDIR)
	@echo CFILESNDIR = $(CFILESNDIR)
	@echo CXXFILESNDIR = $(CXXFILESNDIR)
	@echo SOBJS = $(SOBJS)
	@echo COBJS = $(COBJS)
	@echo CXXOBJS = $(CXXOBJS)
	@echo OBJS = $(OBJS)
