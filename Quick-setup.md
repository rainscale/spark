[Back](https://github.com/rainscale/spark)

# Quick setup

## Config
```sh
git config --global user.name rainscale
git config --global user.email rainscale@163.com
```

## Create a Repository
### 1. HTTPS

* or create a new repository on the command line
```sh
echo "# spark" >> README.md
git init
git add README.md
git commit -m "first commit"
git remote add origin https://github.com/rainscale/spark.git
git push -u origin master
```

* or push an existing repository from the command line
```sh
git remote add origin https://github.com/rainscale/spark.git
git push -u origin master
```

### 2. SSH

* or create a new repository on the command line
```sh
echo "# spark" >> README.md
git init
git add README.md
git commit -m "first commit"
git remote add origin git@github.com:rainscale/spark.git
git push -u origin master
```

* or push an existing repository from the command line
```sh
git remote add origin git@github.com:rainscale/spark.git
git push -u origin master
```