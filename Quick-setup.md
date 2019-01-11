# Quick setup

## Config
git config --global user.name rainscale
git config --global user.email rainscale@163.com

## Create a Repository
### 1.HTTPS
https://github.com/rainscale/spark.git

…or create a new repository on the command line
echo "# spark" >> README.md
git init
git add README.md
git commit -m "first commit"
git remote add origin https://github.com/rainscale/spark.git
git push -u origin master

…or push an existing repository from the command line
git remote add origin https://github.com/rainscale/spark.git
git push -u origin master

### 2.SSH
git@github.com:rainscale/spark.git

…or create a new repository on the command line
echo "# spark" >> README.md
git init
git add README.md
git commit -m "first commit"
git remote add origin git@github.com:rainscale/spark.git
git push -u origin master

…or push an existing repository from the command line
git remote add origin git@github.com:rainscale/spark.git
git push -u origin master