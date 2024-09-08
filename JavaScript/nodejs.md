## 1.下载
下载网址：https://url.nodejs.cn/download/

## 2.设置环境变量
```bash
$ vim ~/.bashrc
export NODE_HOME=/opt/node-v20.12.2-linux-x64
export PATH=$PATH:${NODE_HOME}/bin
```

## 3.确认版本
```bash
$ node -v
v20.12.2
$ npm -v
10.5.0
```

## 4.初始化npm项目
```bash
$ mkdir node-demo
$ cd node-demo
$ npm init -y
// 安装express框架
$ npm install express --save  // --save参数会将express添加到package.json的依赖项中
$ npm install cors --save
// 安装mysql的依赖
$ npm install mysql --save
```

## 5.编写服务器代码
配置数据库连接config/db_config.js
```javascript
// 引入mysql配置
const mysql = require('mysql')

// 创建db实例
const db = mysql.createPool({
    host: '127.0.0.1',
    user: 'root',
    password: 'yulinlin',
    database: 'demo',
});

// 导出
module.exports = db
```

编写数据库查询借口controller/students_controller.js
```javascript
// 引入db配置
const db = require("../config/db_config")

exports.student = (req, res) => {
    // 获取前端的参数
    let {name, page, size} = req.query
    console.log(`name=${name}, page=${page}, size=${size}`)
    page = (page -1) * size

    if (name.length == 0) {
        name = ""
    } else {
        name = `and name like '%${name}%'`
    }

    // 查询student列表sql
    const pageSql = `select * from students where del=0 ${name} order by id limit ${page}, ${size}`
    // 查询student总数的sql
    const totalSql = `select count(*) as total from students where del=0 ${name}`

    db.query(pageSql, (err, pageData) => {
        if (err) {
            throw new Error(err.message);
        }
        db.query(totalSql, (err, count) => {
            if (err) {
                throw new Error(err.message)
            }
            res.send({
                code: 200,
                msg: "",
                data: {
                    students: pageData,
                    total: count[0].total,
                    pages: 0
                },
            })
        })
    })
}
```

创建router路由router/students.js
```javascript
const express = require('express');
const router = express.Router()
const students_controller = require("../controller/students_controller")

// 查询student列表
router.get("/api/v1/student", students_controller.student)
// 导出路由
module.exports = router
```

项目根目录下创建server.js服务器主文件
```javascript
// 引入express，配置app
const express = require('express')
const app = express()

// 配置post请求的参数解析
const bodyParser = require('body-parser')
app.use(bodyParser.json())
app.use(bodyParser.urlencoded({ extended: false }))

// 配置跨域设置
const cors = require('cors')
app.use(cors())

// 错误中间件配置
app.use((err, req, res, next) => {
    console.log(err)
    res.send({ code: 500, msg: err.message, data: null })
})

// 配置路由
const studentsRouter = require("./router/students")
// 访问url: http://127.0.0.1:8090/student/api/v1/student?page=1&size=10&name
app.use("/student", studentsRouter)

// 启动8090端口监听的服务
app.listen(8090, () => {
    console.log('server running at http://127.0.0.1:8090')
})
```

6.启动服务器
```bash
$ node server.js
server running at http://127.0.0.1:8090
```
