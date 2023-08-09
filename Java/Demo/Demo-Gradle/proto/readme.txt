protobuf安装：
sudo apt install protobuf-compiler
spark@Spark:~$ protoc --version
libprotoc 3.12.4

#  在终端输入下列命令进行编译
protoc -I=$SRC_DIR --xxx_out=$DST_DIR   $SRC_DIR/addressbook.proto

# 参数说明
# 1. $SRC_DIR：指定需要编译的 .proto 文件目录 (如没有提供则使用当前目录)
# 2. --xxx_out：xxx 根据需要生成代码的类型进行设置
	"""
	对于 Java ，xxx =  java ，即 -- java_out
	对于 C++ ，xxx =  cpp ，即 --cpp_out
	对于 Python，xxx =  python，即 --python_out
	"""

# 3. $DST_DIR ：编译后代码生成的目录 (通常设置与$SRC_DIR相同)
# 4. 最后的路径参数：需要编译的 .proto 文件的具体路径

# 编译通过后，Protoco Buffer会根据不同平台生成对应的代码文件