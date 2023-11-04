#project(
#	'use_third_lib',
#	'c'
#)

libs=meson.get_compiler('c').find_library(
		'libstatic_library',
#		dirs:join_paths(meson.source_root(), 'libs'), #指定库文件必须使用绝对路径
		dirs:join_paths(meson.source_root(), 'use_third_library/libs'), #指定库文件必须使用绝对路径
		required:true
)

executable(
	'use_third_lib',	#目标程序文件名
	'src/main.c',	    #源代码文件
	dependencies: libs,	#三方库依赖项
	include_directories: 'src/include' #可以使用相对路径
)