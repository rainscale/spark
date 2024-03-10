sdk下载：go.dev/dl/
go run hello.go

tar -xzvf go1.22.1.linux-amd64.tar.gz

vim ~/.bashrc或者~/.profile
export GOROOT=/home/spark/Tools/go
export PATH=$PATH:$GOROOT/bin

go version
go version go1.22.1 linux/amd64

go build hello.go
或者go build -o hello hello.go
./hello
