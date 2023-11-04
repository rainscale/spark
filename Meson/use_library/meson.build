#project(
#	'use_lib',
#	'c'
#)

# Builds a static library with the given sources
libs=static_library(
	'static_library',	    #库文件名称
	'src/lib/lib.c',		#库source文件
	include_directories:['src/include'],	#库依赖头文件
	install:true,		#生成库完整安装，可做为第三方依赖的库使用，否则linker这个三方库会报错
	install_dir:'lib'  #install目录
)

executable(
	'use_lib',	    #目标程序文件名
	'src/main.c',	#源代码文件
	link_with:libs,	#库文件
	include_directories: 'src/include' #可以使用相对路径
)