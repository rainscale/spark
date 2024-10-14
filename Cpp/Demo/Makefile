CROSS_COMPILE ?= arm-linux-gnueabihf-

default: all

TARGET := demo

ifndef OUT_DIR
	OUT_DIR := ./
endif

CC := $(CROSS_COMPILE)g++
LD := $(CROSS_COMPILE)ld
OBJCOPY := $(CROSS_COMPILE)objcopy
OBJDUMP := $(CROSS_COMPILE)objdump

CFLAGS = -Wall -fPIC -Wl, -soname -w -D_REENTRANT -fpermissive -O2 -std=gnu++11
CFLAGS += $(XCFLAGS)

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


SRCDIRS := src \
		   3rdparty

OBJDIR := obj

SFILESS := $(foreach dir, $(SRCDIRS), $(wildcard $(dir)/*.S))
CFILESS := $(foreach dir, $(SRCDIRS), $(wildcard $(dir)/*.c))
CXXFILESS := $(foreach dir, $(SRCDIRS), $(wildcard $(dir)/*.cpp))

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
